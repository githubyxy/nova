package com.yxy.nova.mwh.elasticsearch;

import com.yxy.nova.mwh.elasticsearch.admin.ElasticSearchIndexAndAlias;
import com.yxy.nova.mwh.elasticsearch.basic.where.WhereCondition;
import com.yxy.nova.mwh.elasticsearch.dto.SearchRequest;
import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import com.yxy.nova.mwh.elasticsearch.facade.org.elasticsearch.client.Response;
import com.yxy.nova.mwh.elasticsearch.facade.org.elasticsearch.client.RestClient;
import com.yxy.nova.mwh.elasticsearch.util.AssistantUtil;
import com.yxy.nova.mwh.elasticsearch.util.GenerateSearchRequest;
import com.yxy.nova.mwh.elasticsearch.util.GenerateURL;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 查询迭代器
 * @author quyuanwen
 */
public class ScrollIterator implements Iterator<Map<String, Object>> {
    private Logger logger = LoggerFactory.getLogger(ScrollIterator.class);

    protected String table;
    protected List<String> indices;
    protected List<WhereCondition> conditions;
    protected int fetchSize;
    protected Map<String, Object> next;
    protected Set<String> used;
    protected int offset = 0;
    protected RestClient client;
    protected String scrollId;
    protected LinkedList<JSONObject> hits = new LinkedList<>();
    protected ElasticSearchIndexAndAlias elasticSearchIndexAndAlias;
//    protected MonitorGenerator monitorGenerator;

    protected ScrollIterator(String table, RestClient client, List<String> indices, List<WhereCondition> conditions, int fetchSize, ElasticSearchIndexAndAlias elasticSearchIndexAndAlias){
        this.table = table;
        this.indices = indices;
        this.conditions = conditions;
        this.fetchSize = fetchSize;
        this.elasticSearchIndexAndAlias = elasticSearchIndexAndAlias;
        this.used=new HashSet<>();
        this.client = client;
    }

//    /**
//     * 设置监控
//     * @param monitorGenerator
//     */
//    public void setMonitorGenerator(MonitorGenerator monitorGenerator){
//        this.monitorGenerator = monitorGenerator;
//    }

    @Override
    public boolean hasNext() {
        //如果查询的结果为空了执行下一次查询

        if (next == null) {
            if(CollectionUtils.isEmpty(hits)){
                while(true){
                    if(scrollId != null){
                        //如果查询到数据则跳出
                        if(fetchMore()){
                            break;
                        }
                    }
                    //没有查询到数据，则判断还有没有可以查询的索引
                    if(offset < indices.size()) {
                        if(firstFetch()){
                            //查询到数据直接退出循环
                            break;
                        }
                    }else{
                        //查询完所有的索引
                        break;
                    }
                }
            }
            if(!CollectionUtils.isEmpty(hits)){
                next = hits.removeLast();
            }else{
                //如果没取到，切没取完接着取
                if(offset < indices.size()){
                    return this.hasNext();
                }
            }
        }
        return next != null;
    }

    /**
     * 根据游标继续取数据
     */
    private boolean fetchMore()  {
        JSONObject request = new JSONObject();
        request.put("scroll", "1m");
        request.put("scroll_id", scrollId);
        try {
            Response response = AssistantUtil.performRequest(client, "POST", "/_search/scroll", request);
            return getResult(response);
        }catch (ElasticsearchClientException e){
            logger.error("根据游标获取下一组结果出错！错误信息："+ e.getMessage(),e);
            return false;
        }
    }

    /**
     * 第一次查询一个索引
     */
    private boolean firstFetch() {
        //保证游标初始为空
        scrollId = null;

        //避免迭代时索引重复查询
        String index = indices.get(offset);
        //根据别名获取所有索引
        List<String> aliasIndices = elasticSearchIndexAndAlias.getIndicesByAlias(index);
        logger.info("firstFetch方法，获取的索引别名信息："+aliasIndices);
        if(!CollectionUtils.isEmpty(aliasIndices)){
            //重新选择需要查询的索引
            index = null;
            int i = 0;
            for(; i< aliasIndices.size(); i++){
                //判断有没有使用过
                if(!used.contains(aliasIndices.get(i))){
                    index = aliasIndices.get(i);
                    used.add(index);
                    i++;
                    break;
                }
            }
            //所有的别名对应的索引都比较过，则准备下一个索引位置
            if (i == aliasIndices.size()){
                offset++;
            }
            //没有可查询的索引
            if(index == null){
                return false;
            }
        }else{
            offset++;
            //判断有没有使用过
            if(!used.contains(index)){
                used.add(index);
            }else{
                return false;
            }
        }
//        MetricTimer.Context timer = this.monitorGenerator.monitorRT(table, "iterate");
//        //记录tps
//        this.monitorGenerator.monitorTPS(table, "iterate");
        try {
            logger.info("firstFetch方法，查询的索引信息："+index);

            // 生成查询请求
            SearchRequest request = GenerateSearchRequest.buildRequest(new String[]{index}, table, conditions);

            // 配置查询参数
            request.putQueryString("size", Integer.toString(fetchSize));
            // 配置scroll
            request.putQueryString("scroll", "1m");
            // 扫描
            String url = GenerateURL.buildUrl(request);

            Response response = AssistantUtil.performRequest(client, "POST", url, request.getBody());

//            if (timer != null) {
//                timer.stop();
//            }
            return getResult(response);
        }catch (ElasticsearchClientException e){
            //监控异常
//            this.monitorGenerator.countException(table, "iterate", e.getClass().getName());
            logger.error("迭代时初始查询时出错！错误信息："+ e.getMessage(),e);
            return false;
        }
    }

    protected boolean getResult(Response response) throws ElasticsearchClientException{
        AssistantUtil.checkStatusCode(response);
        JSONObject responseJson = AssistantUtil.parseResponse(response);
        JSONObject hitsObject = responseJson.getJSONObject("hits");
        if(hitsObject != null){
            JSONArray hits = responseJson.getJSONObject("hits").getJSONArray("hits");
            if(!CollectionUtils.isEmpty(hits)){
                for (int i = 0; i < hits.size(); i++) {
                    JSONObject hit = hits.getJSONObject(i);
                    this.hits.add(hit.getJSONObject("_source"));
                }
                logger.info("迭代器的getResult方法，得到结果条数："+hits.size());
                this.scrollId = responseJson.getString("_scroll_id");
                return true;
            }
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