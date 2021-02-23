package com.yxy.nova.mwh.elasticsearch;

import com.yxy.nova.mwh.elasticsearch.dto.SearchRequest;
import com.yxy.nova.mwh.elasticsearch.exception.ESExceptionType;
import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import com.yxy.nova.mwh.elasticsearch.facade.org.elasticsearch.client.Response;
import com.yxy.nova.mwh.elasticsearch.facade.org.elasticsearch.client.RestClient;
import com.yxy.nova.mwh.elasticsearch.util.AssistantUtil;
import com.yxy.nova.mwh.elasticsearch.util.GenerateURL;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * 查询迭代器
 * @author quyuanwen
 */
public class SynScrollIterator implements Iterator<Map<String, Object>> {
    private Logger logger = LoggerFactory.getLogger(SynScrollIterator.class);

    protected String table;
    protected int fetchSize;
    protected Map<String, Object> next;
    protected RestClient client;
    protected String scrollId;
    protected LinkedList<JSONObject> hits = new LinkedList<>();
//    protected MonitorGenerator monitorGenerator;
    protected SearchRequest searchRequest;

    protected SynScrollIterator(String table, RestClient client, int fetchSize, SearchRequest searchRequest){
        this.table = table;
        this.fetchSize = fetchSize;
        this.client = client;
        this.searchRequest = searchRequest;
    }

    /**
     * 设置监控
     * @param monitorGenerator
     */
//    public void setMonitorGenerator(MonitorGenerator monitorGenerator){
//        this.monitorGenerator = monitorGenerator;
//    }

    @Override
    public boolean hasNext() {
        //如果查询的结果为空了执行下一次查询
        if (next == null) {
            if(CollectionUtils.isEmpty(hits)){
                if(scrollId != null){
                    //如果查询到数据则跳出
                    fetchMore();
                }else{
                    firstFetch();
                }
            }
            if(!CollectionUtils.isEmpty(hits)){
                next = hits.removeLast();
            }
        }
        return next != null;
    }
    /**
     * 根据游标继续取数据
     */
    protected boolean fetchMore() {
        StringBuilder message = new StringBuilder(100);
        message.append("【游标方式查询】fetchMore查询");
        try {
            //记录tps
//            this.monitorGenerator.monitorTPS(table, "iterate-fetchMore");
            long start = System.currentTimeMillis();
            JSONObject request = new JSONObject();
            request.put("scroll", "1m");
            request.put("scroll_id", scrollId);

            Response response = AssistantUtil.performRequest(client, "POST", "/_search/scroll", request);

            message.append(" URL:/_search/scroll ").append(" 查询数量:").append(fetchSize).append(" 客户耗时:").append((System.currentTimeMillis() - start)).append("ms 查询条件").append(scrollId);
            return this.getResults(response, false, message);
        }catch (Exception e){
            //监控异常
//            this.monitorGenerator.countException(table, "iterate-fetchMore", e.getClass().getName());
            message.append(" 查询异常！");
            logger.error(message.toString(),e);
        }
        return false;
    }

    /**
     * 第一次查询一个索引
     */
    protected boolean firstFetch(){
        StringBuilder message = new StringBuilder(100);
        message.append("【游标方式查询】firstFetch查询");
        try {
            //记录tps
//            this.monitorGenerator.monitorTPS(table, "iterate-firstFetch");
//            MetricTimer.Context timer = this.monitorGenerator.monitorRT(table, "iterate");
            long start = System.currentTimeMillis();
            // 配置查询参数
            searchRequest.putQueryString("size", Integer.toString(fetchSize));
            // 配置scroll
            searchRequest.putQueryString("scroll", "1m");
            // 扫描

            String url = GenerateURL.buildUrl(searchRequest);
            String httpBody = AssistantUtil.toJSONString(searchRequest.getBody());
            Response response = AssistantUtil.performRequest(client, "POST", url, httpBody);
//            if (timer != null) {
//                timer.stop();
//            }
            message.append(" URL:").append(url).append(" 查询数量:").append(fetchSize).append(" 客户耗时:").append((System.currentTimeMillis() - start)).append("ms 查询条件").append(httpBody);
            return this.getResults(response, true, message);
        }catch (Exception e){
            //监控异常
//            this.monitorGenerator.countException(table, "iterate-firstFetch", e.getClass().getName());
            message.append(" 查询异常！");
            logger.error(message.toString(),e);
        }
        return false;
    }

    /**
     * 获取迭代结果
     * @param response
     * @param first
     * @param message
     * @return
     * @throws ElasticsearchClientException
     */
    protected boolean getResults(Response response, boolean first, StringBuilder message) throws ElasticsearchClientException {
        if(response == null){
            throw new ElasticsearchClientException("ES查询时请求响应为空", ESExceptionType.HTTP_REQUEST_ERROR);
        }
        AssistantUtil.checkStatusCode(response);
        JSONObject responseJson = AssistantUtil.parseResponse(response);
        JSONObject hitsObject = responseJson.getJSONObject("hits");
        if(hitsObject != null){
            //第一次查询计算总量
            if(first){
                message.append(" 命中总数:").append(hitsObject.getIntValue("total"));
            }
            long took = responseJson.getLongValue("took");
            message.append(" 服务端耗时:").append(took);
            JSONArray hits = hitsObject.getJSONArray("hits");
            if(!CollectionUtils.isEmpty(hits)){
                message.append(" 实际条数:").append(hits.size());
                for (int i = 0; i < hits.size(); i++) {
                    JSONObject hit = hits.getJSONObject(i);
                    this.hits.add(hit.getJSONObject("_source"));
                }
                this.scrollId = responseJson.getString("_scroll_id");
                logger.info(message.toString());
                //获取最新的游标
                return true;
            }
            message.append(" 没有命中");
            logger.info(message.toString());
        }
        return false;
    }

    @Override
    public Map<String, Object> next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No next item");
        }
        Map<String, Object> result = next;
        next = null;
        return result;
    }

    @Override
    public void remove() {
    }
}