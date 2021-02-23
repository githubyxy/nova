package com.yxy.nova.mwh.elasticsearch;

import com.yxy.nova.mwh.elasticsearch.admin.ElasticSearchIndexAndAlias;
import com.yxy.nova.mwh.elasticsearch.basic.where.WhereCondition;
import com.yxy.nova.mwh.elasticsearch.dto.SearchRequest;
import com.yxy.nova.mwh.elasticsearch.exception.ESExceptionType;
import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import com.yxy.nova.mwh.elasticsearch.facade.org.elasticsearch.client.Response;
import com.yxy.nova.mwh.elasticsearch.facade.org.elasticsearch.client.RestClient;
import com.yxy.nova.mwh.elasticsearch.util.AssistantUtil;
import com.yxy.nova.mwh.elasticsearch.util.ElasticseachClientThreadFactory;
import com.yxy.nova.mwh.elasticsearch.util.GenerateSearchRequest;
import com.yxy.nova.mwh.elasticsearch.util.GenerateURL;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 异步迭代方法
 * @author quyuanwen
 */
public class AsyncScrollIterator {
    private Logger logger = LoggerFactory.getLogger(ScrollIterator.class);

    /**
     * 查询表
     */
    protected String table;
    /**
     * 实际索引信息
     */
    protected List<String> actualIndices;
    /**
     * 查询条件
     */
    protected List<WhereCondition> conditions;
    /**
     * 每次从服务端查询数量
     */
    protected int fetchSize;
    /**
     * 查询是否结束
     */
    protected volatile boolean over = false;

    protected RestClient client;

    /**
     * 查询的总数
     */
    protected long total;
    /**
     * 当前游标id
     */
    protected String scrollId;
    protected ElasticSearchIndexAndAlias elasticSearchIndexAndAlias;
//    protected MonitorGenerator monitorGenerator;

    private ArrayBlockingQueue<JSONObject> dataCache;

    boolean first = true;
    private ThreadPoolExecutor  fetchDataPool = new ThreadPoolExecutor(0, 50, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
            new ElasticseachClientThreadFactory("es-async-scroll"));


    /**
     * 初始化异步迭代对象
     *
     * @param table
     * @param client
     * @param actualIndices 实际索引信息
     * @param conditions
     * @param fetchSize 每次从服务端拉取的数据量
     */
    public AsyncScrollIterator(String table, RestClient client, List<String> actualIndices, List<WhereCondition> conditions, int fetchSize){
        this.table = table;
        this.actualIndices = actualIndices;
        this.conditions = conditions;
        this.fetchSize = fetchSize;
        this.client = client;
        this.dataCache = new ArrayBlockingQueue(fetchSize * 2);
    }

    /**
     * 初始化异步迭代对象
     * @param table
     * @param client
     * @param actualIndices 实际索引信息
     * @param conditions
     * @param fetchSize 每次从服务端拉取的数据量
     * @param cacheSize 数据本地缓存的容量
     */
    public AsyncScrollIterator(String table, RestClient client, List<String> actualIndices, List<WhereCondition> conditions, int fetchSize, int cacheSize){
        this.table = table;
        this.actualIndices = actualIndices;
        this.conditions = conditions;
        this.fetchSize = fetchSize;
        this.client = client;
        this.dataCache = new ArrayBlockingQueue(cacheSize);
    }

    /**
     * 设置监控
     * @param monitorGenerator
     */
//    public void setMonitorGenerator(MonitorGenerator monitorGenerator){
//        this.monitorGenerator = monitorGenerator;
//    }


    /**
     * 获取数据信息
     * @return
     */
    public JSONObject next(){
        if(first){
            //初始化线程池
            fetchDataPool.execute( new FetchData());
            first = false;
        }
        JSONObject result = null;
        while(true){
            result = dataCache.poll();
            if(result == null && !over){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    logger.warn("【游标异步方式查询】等待获取数据时被阻断退出",e);
                }
            }else{
                break;
            }
        }
        return result;
    }

    public long getTotal() {
        return total;
    }

    /**
     * 根据游标继续取数据
     */
    private JSONArray fetchMore()  throws ElasticsearchClientException {
//        MetricTimer.Context timer = monitorGenerator.monitorRT(table, "iterate");
//        //记录tps
//        monitorGenerator.monitorTPS(table, "iterate");
        JSONObject request = new JSONObject();
        request.put("scroll", "1m");
        request.put("scroll_id", scrollId);

        Response response = AssistantUtil.performRequest(client, "POST", "/_search/scroll", request);
//        if (timer != null) {
////            timer.stop();
////        }

        return this.getResults(response,false);
    }

    /**
     * 第一次查询一个索引
     */
    private JSONArray firstFetch(String index) throws ElasticsearchClientException{
//        MetricTimer.Context timer = monitorGenerator.monitorRT(table, "iterate");
//        //记录tps
//        monitorGenerator.monitorTPS(table, "iterate");
        long start = System.currentTimeMillis();
        // 生成查询请求
        SearchRequest request = GenerateSearchRequest.buildRequest(new String[]{index}, table, conditions);
        // 配置查询参数
        request.putQueryString("size", Integer.toString(fetchSize));
        // 配置scroll
        request.putQueryString("scroll", "1m");
        // 扫描
        String url = GenerateURL.buildUrl(request);
        String httpBody = AssistantUtil.toJSONString(request.getBody());
        Response response = AssistantUtil.performRequest(client, "POST", url, httpBody);
        logger.info("【游标异步方式查询】第一次查询索引{} 查询数量{} 耗时{}ms 查询地址{} 查询内容{}",index,fetchSize,(System.currentTimeMillis() - start), url, httpBody);
//        if (timer != null) {
//            timer.stop();
//        }

        return this.getResults(response,true);
    }

    private JSONArray getResults(Response response, boolean first) throws ElasticsearchClientException{
        if(response == null){
            throw new ElasticsearchClientException("ES查询时请求响应为空", ESExceptionType.HTTP_REQUEST_ERROR);
        }
        AssistantUtil.checkStatusCode(response);
        JSONObject responseJson = AssistantUtil.parseResponse(response);
        JSONObject hitsObject = responseJson.getJSONObject("hits");
        if(hitsObject != null){
            //第一次查询计算总量
            if(first){
                total += hitsObject.getLongValue("total");
            }
            JSONArray hits = hitsObject.getJSONArray("hits");
            if(!CollectionUtils.isEmpty(hits)){
                long took = responseJson.getLongValue("took");
                this.scrollId = responseJson.getString("_scroll_id");
                logger.info("【游标异步方式查询】得到结果条数：{} ES实际耗时：{}",hits.size(),took);
                //获取最新的游标
                return hits;
            }
        }
        return null;
    }

    private class FetchData implements Runnable{

        @Override
        public void run() {
            int offset = 0;
            String currentIndex = actualIndices.get(offset);
            while(true){
                JSONArray results = null;
                if(StringUtils.isEmpty(scrollId)){
                    //从新索引拉去数据
                    try {
                        results = firstFetch(currentIndex);
                    } catch (ElasticsearchClientException e) {
                        //监控异常
//                        monitorGenerator.countException(table, "iterate", e.getClass().getName());
                        logger.error("【游标异步方式查询】第一次查询索引"+currentIndex+"出错！",e);
                        break;
                    }
                }else{
                    long start = System.currentTimeMillis();
                    try {
                        results = fetchMore();
                        logger.info("【游标异步方式查询】通过游标获取索引{} 查询数量{} 耗时{}ms scroll_id{}", currentIndex, fetchSize,(System.currentTimeMillis() - start), scrollId);
                    } catch (ElasticsearchClientException e) {
                        //监控异常
//                        monitorGenerator.countException(table, "iterate", e.getClass().getName());
                        logger.error("【游标异步方式查询】根据游标获取下一组结果出错！错误信息："+ e.getMessage(),e);
                    }
                }

                if(!CollectionUtils.isEmpty(results)){
                    for (int i = 0; i < results.size(); i++) {
                        JSONObject hit = results.getJSONObject(i);
                        try {
                           dataCache.put(hit.getJSONObject("_source"));
                        } catch (InterruptedException e) {
                            logger.warn("【游标异步方式查询】查询数据等待进入队列被阻断退出！",e);
                        }
                    }
                    logger.info("【游标异步方式查询】得到结果条数："+ results.size());
                }
                if(CollectionUtils.isEmpty(results) || results.size() < fetchSize){
                    //获取下一个索引
                    offset++;
                    if(offset < actualIndices.size()){
                        currentIndex = actualIndices.get(offset);
                        //获取新索引时重新设置游标
                        scrollId = null;
                    }else{
                        break;
                    }
                }
            }
            over = true;
        }
    }
}