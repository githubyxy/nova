package com.yxy.nova.mwh.elasticsearch;

import com.yxy.nova.mwh.elasticsearch.basic.where.WhereCondition;
import com.yxy.nova.mwh.elasticsearch.dto.*;
import com.yxy.nova.mwh.elasticsearch.exception.ESExceptionType;
import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import com.yxy.nova.mwh.elasticsearch.facade.org.elasticsearch.client.Response;
import com.yxy.nova.mwh.elasticsearch.util.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class ElasticsearchClient extends AbstractSearchService implements SearchService {
    private Logger logger = LoggerFactory.getLogger(ElasticsearchClient.class);

    /**
     * 批量更新
     *
     * @return 更新结果
     */
    public BulkUpdateResult bulkUpdate(List<UpdateAction> actions) throws ElasticsearchClientException {

        if (CollectionUtils.isEmpty(actions)) return null;
        String type = actions.get(0).getTable();
//        //记录rt
//        MetricTimer.Context timer = this.monitorGenerator.monitorRT(type, "bulkUpdate");
//        //记录tps
//        this.monitorGenerator.monitorTPS(type, "bulkUpdate", actions.size());
        //记录批量
        //this.monitorGenerator.recordBatchSize(type, "bulkUpdate", actions.size());
        try {
            // 再写入ES
            StringBuilder bulkBody = new StringBuilder();
            for (UpdateAction action : actions) {
                String table = action.getTable();
                Map<String, Object> json = action.getJson();
                String id = action.getId();
                // 放入指令
                JSONObject indexCommand = new JSONObject();
                String index = policy.getWriteIndex(table, json);

                if (StringUtils.isBlank(index)) {
                    throw new ElasticsearchClientException("获取索引信息为空", ESExceptionType.INDEX_ERROR);
                }
                indexCommand.put("_index", index);
                indexCommand.put("_type", action.getTable());
                if (StringUtils.isNotBlank(id)) {
                    indexCommand.put("_id", id);
                } else {
                    throw new ElasticsearchClientException("更新操作id不能为空", ESExceptionType.PARAM_ERROR);
                }

                JSONObject command = new JSONObject();
                command.put("update", indexCommand);
                bulkBody.append(AssistantUtil.toJSONString(command));
                bulkBody.append('\n');

                JSONObject docCommand = new JSONObject();
                docCommand.put("doc", new JSONObject(action.getJson()));

                // 放入数据
                bulkBody.append(AssistantUtil.toJSONString(docCommand));
                bulkBody.append('\n');

            }
            // 发送请求
            Response response = AssistantUtil.performRequest(connection, "POST", "/_bulk", bulkBody.toString());
            AssistantUtil.checkStatusCode(response);
            JSONObject responseJson = AssistantUtil.parseResponse(response);
            BulkUpdateResult bulkUpdateResult = GenerateUpdateResponse.convertToBulkUpdateResponse(responseJson);
            //监控打点
//            if (timer != null) {
//                timer.stop();
//            }
            //监控插入失败
//            if(bulkUpdateResult.getFailNumber()  > 0){
//                this.monitorGenerator.countBatchFail(type, "bulkUpdate", bulkUpdateResult.getFailNumber());
//            }
            return bulkUpdateResult;
        } catch (Exception e) {
            //监控异常
//            this.monitorGenerator.countException(type, "bulkUpdate", e.getClass().getName());
            throw e;
        }
    }

    @Override
    public void insertInto(String table, String id, Map<String, Object> json) throws ElasticsearchClientException {

//        //记录rt
//        MetricTimer.Context timer = this.monitorGenerator.monitorRT(table, "insertInto");
//        //记录tps
//        this.monitorGenerator.monitorTPS(table, "insertInto");
        try {
            // 写入ES
            String index = policy.getWriteIndex(table, json);

            if (StringUtils.isBlank(index)) {
                throw new ElasticsearchClientException("获取索引信息为空", ESExceptionType.INDEX_ERROR);
            }
            String indexEncoded = AssistantUtil.urlEncode(index);
            String typeEncoded = AssistantUtil.urlEncode(table);
            Response response;
            if (StringUtils.isBlank(id)) {
                response = AssistantUtil.performRequest(connection, "POST", "/" + indexEncoded + "/" + typeEncoded, new JSONObject(json));
            } else {
                String idEncoded = AssistantUtil.urlEncode(id);
                response = AssistantUtil.performRequest(connection, "PUT", "/" + indexEncoded + "/" + typeEncoded + "/" + idEncoded, new JSONObject(json));
            }
            AssistantUtil.checkStatusCode(response);
            JSONObject responseJson = AssistantUtil.parseResponse(response);
            if (responseJson.getBoolean("created") != Boolean.TRUE) {
                logger.warn("ElasticSearch duplicated id: " + id);
            }
            //打点
//            if (timer != null) {
//                timer.stop();
//            }
        } catch (Exception e) {
            //监控异常
//            this.monitorGenerator.countException(table, "insertInto", e.getClass().getName());
            throw e;
        }
    }

    @Override
    public BulkResponse bulkInsert(List<InsertAction> actions) throws ElasticsearchClientException {

        if (CollectionUtils.isEmpty(actions)) return null;

        String type = actions.get(0).getTable();
//        //记录rt
//        MetricTimer.Context timer = this.monitorGenerator.monitorRT(type, "bulkInsert");
//        //记录tps
//        this.monitorGenerator.monitorTPS(type, "bulkInsert", actions.size());
        //记录批次数量
        //this.monitorGenerator.recordBatchSize(type, "bulkInsert", actions.size());
        try {
            // 写入ES
            StringBuilder bulkBody = new StringBuilder();
            for (InsertAction action : actions) {
                // 放入指令
                String table = action.getTable();
                String id = action.getId();
                Map<String, Object> json = action.getJson();
                JSONObject indexCommand = new JSONObject();
                String index = policy.getWriteIndex(table, json);

                if (StringUtils.isBlank(index)) {
                    throw new ElasticsearchClientException("获取索引信息为空", ESExceptionType.INDEX_ERROR);
                }
                indexCommand.put("_index", index);
                indexCommand.put("_type", table);

                if (StringUtils.isNotBlank(id)) {
                    indexCommand.put("_id", id);
                }
                if (action.getIfNotExists()) indexCommand.put("op_type", "create");
                JSONObject command = new JSONObject();
                command.put("index", indexCommand);
                bulkBody.append(AssistantUtil.toJSONString(command));
                bulkBody.append('\n');

                // 放入数据
                bulkBody.append(AssistantUtil.toJSONString(new JSONObject(json)));
                bulkBody.append('\n');
            }

            Response dataResponse = AssistantUtil.performRequest(connection, "POST", "/_bulk", bulkBody.toString());
            AssistantUtil.checkStatusCode(dataResponse);
            JSONObject dataResponseJson = AssistantUtil.parseResponse(dataResponse);
            BulkResponse bulkResponse = GenerateUpdateResponse.convertToBulkResponse(dataResponseJson);

            //打点
//            if (timer != null) {
//                timer.stop();
//            }
            //监控插入失败
            if (bulkResponse.hasFailures()) {
                int failNumber = 0;
                for (BulkItemResponse bulkItemResponse : bulkResponse.getItems()) {
                    if (bulkItemResponse.isFailed()) {
                        failNumber++;
                    }
                }
//                this.monitorGenerator.countBatchFail(type, "bulkInsert", failNumber);
            }

            return bulkResponse;
        } catch (Exception e) {
            //监控异常
//            this.monitorGenerator.countException(type, "bulkInsert", e.getClass().getName());
            throw e;
        }
    }


    @Override
    public List<Map<String, Object>> list(String table, List<WhereCondition> conditions) throws ElasticsearchClientException {
//        //记录rt
//        MetricTimer.Context timer = this.monitorGenerator.monitorRT(table, "list");
//        //记录tps
//        this.monitorGenerator.monitorTPS(table, "list");
        try {
            // 发送请求
            JSONObject response = responseJson(table, conditions);

            // 没有结果
            if (response == null) {
                logger.warn("Response is null on list");
                return new ArrayList<>();
            }

            // 将hits转换成列表
            JSONObject hits = response.getJSONObject("hits");
            JSONArray hits2 = hits.getJSONArray("hits");
            List<Map<String, Object>> result = new ArrayList<>();
            for (int i = 0; i < hits2.size(); i++) {
                JSONObject e = hits2.getJSONObject(i);
                JSONObject source = e.getJSONObject("_source");
                result.add(source);
            }
            //打点
//            if (timer != null) {
//                timer.stop();
//            }
            return result;
        } catch (Exception e) {
            //监控异常
//            this.monitorGenerator.countException(table, "list", e.getClass().getName());
            throw e;
        }
    }

    @Override
    public Iterator<Map<String, Object>> iterate(String table, List<WhereCondition> conditions, int fetchSize) throws ElasticsearchClientException {
        if (AnalysisConditionUtil.hasAggregation(conditions)) {
            throw new ElasticsearchClientException("迭代查询不可以使用聚合条件", ESExceptionType.CONDITION_ERROR);
        }
        try {
            //获取要查询的别名
            List<String> indices = policy.getActualIndices(table, conditions, true);
            logger.info("iterate方法，当前查询的索引信息是：" + indices);
            if (CollectionUtils.isEmpty(indices)) {
                throw new ElasticsearchClientException("获取索引信息为空", ESExceptionType.INDEX_ERROR);
            }
            SearchRequest searchRequest = GenerateSearchRequest.buildRequest(indices.toArray(new String[indices.size()]), table, conditions);

            SynScrollIterator synScrollIterator = new SynScrollIterator(table, connection.getClient(), fetchSize, searchRequest);
//            synScrollIterator.setMonitorGenerator( this.monitorGenerator);
            return synScrollIterator;
        } catch (Exception e) {
            //监控异常
//            this.monitorGenerator.countException(table, "iterate", e.getClass().getName());
            throw e;
        }
    }


    @Override
    public AsyncScrollIterator asyncIterate(String table, List<WhereCondition> conditions, int fetchSize, int cacheSize) throws ElasticsearchClientException {
        if (AnalysisConditionUtil.hasAggregation(conditions)) {
            throw new ElasticsearchClientException("【游标异步方式查询】不可以使用聚合条件", ESExceptionType.CONDITION_ERROR);
        }
        try {
            //获取要查询的别名
            List<String> actualIndices = policy.getActualIndices(table, conditions, true);
            logger.info("【游标异步方式查询】初始化索引信息,当前查询的索引信息是：" + actualIndices);
            if (CollectionUtils.isEmpty(actualIndices)) {
                throw new ElasticsearchClientException("【游标异步方式查询】获取索引信息为空", ESExceptionType.INDEX_ERROR);
            }

            AsyncScrollIterator asyncScrollIterator = null;
            if (cacheSize <= fetchSize) {
                asyncScrollIterator = new AsyncScrollIterator(table, connection.getClient(), actualIndices, conditions, fetchSize);
            } else {
                asyncScrollIterator = new AsyncScrollIterator(table, connection.getClient(), actualIndices, conditions, fetchSize, cacheSize);
            }

//            asyncScrollIterator.setMonitorGenerator(this.monitorGenerator);
            return asyncScrollIterator;
        } catch (Exception e) {
            //监控异常
//            this.monitorGenerator.countException(table, "iterate", e.getClass().getName());
            throw e;
        }
    }

    @Override
    public SearchResponse response(String table, List<WhereCondition> conditions) throws ElasticsearchClientException {
//        //记录rt
//        MetricTimer.Context timer = this.monitorGenerator.monitorRT(table, "response");
//        //记录tps
//        this.monitorGenerator.monitorTPS(table, "response");
        try {
            // 发送请求
            JSONObject result = responseJson(table, conditions);

            AggTypes aggTypes = AnalysisConditionUtil.getAggTypes(conditions);
            SearchResponse result2 = GenerateSearchResponse.convertToSearchResponse(result, aggTypes);
            //打点
//            if (timer != null) {
//                timer.stop();
//            }
            return result2;
        } catch (Exception e) {
            //监控异常
//            this.monitorGenerator.countException(table, "response", e.getClass().getName());
            throw e;
        }
    }

    @Override
    public SearchResult get(String table, List<WhereCondition> conditions) throws ElasticsearchClientException {
//        //记录rt
//        MetricTimer.Context timer = this.monitorGenerator.monitorRT(table, "get");
//        //记录tps
//        this.monitorGenerator.monitorTPS(table, "get");
        try {
            JSONObject result = responseJson(table, conditions);
            SearchResult searchResult = convertToSearchResult(result);
            //打点
//            if (timer != null) {
//                timer.stop();
//            }
            return searchResult;
        } catch (Exception e) {
            //监控异常
//            this.monitorGenerator.countException(table, "get", e.getClass().getName());
            throw e;
        }
    }

    @Override
    public void executeUpdate(String table, String id, Date eventOccurTime, Map<String, Object> sets) throws ElasticsearchClientException {
//        //记录rt
//        MetricTimer.Context timer = this.monitorGenerator.monitorRT(table, "executeUpdate");
//        //记录tps
//        this.monitorGenerator.monitorTPS(table, "executeUpdate");
        try {
            Map<String, Object> doc = JsonPath.parsePath(sets);
            // 写入ES
            JSONObject request = new JSONObject();
            request.put("doc", doc);
            String index = policy.getWriteIndex(table, doc);
            if (StringUtils.isBlank(index)) {
                throw new ElasticsearchClientException("获取索引信息为空", ESExceptionType.INDEX_ERROR);
            }

            String indexEncoded = AssistantUtil.urlEncode(index);
            String typeEncoded = AssistantUtil.urlEncode(table);

            if (StringUtils.isBlank(id)) {
                throw new ElasticsearchClientException("更新操作id不能为空", ESExceptionType.PARAM_ERROR);
            }
            String idEncoded = AssistantUtil.urlEncode(id);

            Response response = AssistantUtil.performRequest(connection.getClient(), "POST", "/" + indexEncoded + "/" + typeEncoded + "/"
                    + idEncoded + "/_update", request);
            AssistantUtil.checkStatusCode(response);
            //打点
//            if (timer != null) {
//                timer.stop();
//            }
        } catch (Exception e) {
            //监控异常
//            this.monitorGenerator.countException(table, "executeUpdate", e.getClass().getName());
            throw e;
        }
    }

    @Override
    public List<FetchResult> fetchById(List<FetchAction> fetchActions) throws ElasticsearchClientException {

        // 空白的请求，直接返回
        String requestId = GenerateURL.requestId();
        if (CollectionUtils.isEmpty(fetchActions)) {
            logger.info("Request {} fetch empty", requestId);
            return new ArrayList<>();
        }
        String type = fetchActions.get(0).getType();
//        //记录rt
//        MetricTimer.Context timer = this.monitorGenerator.monitorRT(type, "fetchById");
//        //记录tps
//        this.monitorGenerator.monitorTPS(type, "fetchById");
        try {
            // 生成ES查询请求
            long startTime = System.currentTimeMillis();
            logger.info("Request {} fetch {} items", requestId, fetchActions.size());
            JSONArray mgetDocs = new JSONArray();
            int requestCount = 0;
            for (FetchAction fetchAction : fetchActions) {
                String aliasOrIndex = policy.getReadIndex(fetchAction.getType(), fetchAction.getParam());
                if (StringUtils.isBlank(aliasOrIndex)) {
                    throw new ElasticsearchClientException("获取索引信息为空", ESExceptionType.INDEX_ERROR);
                }
                List<String> indices = policy.getAllAliases().getIndicesByAlias(aliasOrIndex);
                if (CollectionUtils.isEmpty(indices)) {
                    indices = Arrays.asList(aliasOrIndex);
                }
                for (String index : indices) {
                    JSONObject mgetDoc = new JSONObject();
                    mgetDoc.put("_index", index);
                    mgetDoc.put("_type", fetchAction.getType());
                    mgetDoc.put("_id", fetchAction.getId());
                    mgetDocs.add(mgetDoc);
                    requestCount += 1;
                }
            }
            logger.info("Request {} convert to {} mget request", requestId, requestCount);

            // 如果处理之后没有查询请求，那就直接返回结果
            if (requestCount == 0) {
                logger.info("Request {} return empty", requestId, requestCount);
                return new ArrayList<>();
            }
            // 解析ES返回的结果
            JSONObject request = new JSONObject();
            request.put("docs", mgetDocs);
            Response response = AssistantUtil.performRequest(connection.getClient(), "POST", "/_mget", request);
            AssistantUtil.checkStatusCode(response);
            JSONObject responseJson = AssistantUtil.parseResponse(response);
            List<FetchResult> result = parseMgetResponse(responseJson);

            // 返回结果
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Response {} fetch {} items in {} ms", requestId, fetchActions.size(), duration);
            //打点
//            if (timer != null) {
//                timer.stop();
//            }
            return result;
        } catch (Exception e) {
            //监控异常
//            this.monitorGenerator.countException(type, "fetchById", e.getClass().getName());
            throw e;
        }
    }

    @Override
    public List<DeleteResult> delete(List<DeleteAction> deleteActions) throws ElasticsearchClientException {

        if (CollectionUtils.isEmpty(deleteActions)) return new ArrayList<>();

        String type = deleteActions.get(0).getTable();
//        //记录rt
//        MetricTimer.Context timer = this.monitorGenerator.monitorRT(type, "delete");
//        //记录tps
//        this.monitorGenerator.monitorTPS(type, "delete");
        try {
            // 再写入ES
            StringBuilder bulkBody = new StringBuilder();
            for (DeleteAction action : deleteActions) {
                // 放入指令
                JSONObject indexCommand = new JSONObject();
                String index = policy.getReadIndex(action.getTable(), action.getParam());
                if (StringUtils.isBlank(index)) {
                    throw new ElasticsearchClientException("获取索引信息为空", ESExceptionType.INDEX_ERROR);
                }
                indexCommand.put("_index", index);
                indexCommand.put("_type", action.getTable());
                indexCommand.put("_id", action.getId());
                JSONObject command = new JSONObject();
                command.put("delete", indexCommand);
                bulkBody.append(AssistantUtil.toJSONString(command));
                bulkBody.append('\n');
            }
            // 发送请求
            Response response = AssistantUtil.performRequest(connection, "POST", "/_bulk", bulkBody.toString());
            AssistantUtil.checkStatusCode(response);
            JSONObject responseJson = AssistantUtil.parseResponse(response);
            JSONArray items = responseJson.getJSONArray("items");
            List<DeleteResult> resultList = new ArrayList<>();

            for (int i = 0; i < items.size(); i++) {
                JSONObject item = items.getJSONObject(i);
                JSONObject delete = item.getJSONObject("delete");
                String result = delete.getString("result");
                String id = delete.getString("_id");
                boolean error;
                if (result.equals("deleted") || result.equals("not_found")) {
                    error = false;
                } else {
                    logger.warn("Delete {} error, result is {}", id, result);
                    error = true;
                }
                resultList.add(new DeleteResult(delete.getString("_type"), id, error));
            }
            //打点
//            if (timer != null) {
//                timer.stop();
//            }
            return resultList;
        } catch (Exception e) {
            //监控异常
//            this.monitorGenerator.countException(type, "delete", e.getClass().getName());
            throw e;
        }

    }

    public JSONObject responseJson(String table, List<WhereCondition> conditions) throws ElasticsearchClientException {

        List<String> indices = policy.getReadIndices(table, conditions, false);

        if (CollectionUtils.isEmpty(indices)) {
            throw new ElasticsearchClientException("获取索引信息为空", ESExceptionType.INDEX_ERROR);
        }
        logger.info("responseJson方法，查询的索引信息为：" + indices);

        SearchRequest request = GenerateSearchRequest.buildRequest(indices.toArray(new String[indices.size()]), table, conditions);
        JSONObject result = search(request);
        return result;

    }

    /**
     * 执行查询请求
     *
     * @param request
     * @return
     * @throws ElasticsearchClientException
     */
    public JSONObject search(SearchRequest request) throws ElasticsearchClientException {

        String requestId = GenerateURL.requestId();

        // 发送请求
        String url = GenerateURL.buildUrl(request);

        Response response = AssistantUtil.performRequest(connection, "POST", url, request.getBody());

        AssistantUtil.checkStatusCode(response);

        JSONObject responseJson = AssistantUtil.parseResponse(response);
        Long took = responseJson.getLong("took");
        Object total = responseJson.getJSONObject("hits") != null ? responseJson.getJSONObject("hits").get("total") : "-";
        logger.info("Elasticsearch response {} size:{} duration {} ms", requestId, total, took);

        return responseJson;

    }

    protected SearchResult convertToSearchResult(JSONObject searchResult) {
        // 检查错误
        boolean error = checkError(searchResult);
        if (error) {
            return SearchResult.failure();
        }

        // 构建hits
        JSONObject hitsObject = searchResult.getJSONObject("hits");
        Long total = hitsObject.getLong("total");
        JSONArray hitsArray = hitsObject.getJSONArray("hits");
        List<SearchResultItem> items = new ArrayList<>();
        for (int i = 0; i < hitsArray.size(); i++) {
            JSONObject hit = hitsArray.getJSONObject(i);
            String id = hit.getString("_id");
            String type = hit.getString("_type");
            JSONObject source = hit.getJSONObject("_source");
            SearchResultItem item = new SearchResultItem(type, id, source);
            items.add(item);
        }

        return SearchResult.success(total, items);

    }


    protected boolean checkError(JSONObject searchResult) {


        Boolean timedOut = searchResult.getBoolean("timed_out");
        if (timedOut == null) {
            logger.error("timed_out field not found");
            return true;
        }
        if (timedOut) {
            logger.error("ES response timedout");
            return true;
        }
        JSONObject shards = searchResult.getJSONObject("_shards");
        if (shards == null) {
            logger.error("Shards is null");
            return true;
        }
        Integer failedCount = shards.getInteger("failed");
        if (failedCount == null) {
            logger.error("failed count is null");
            return true;
        }
        if (failedCount != 0) {
            logger.error("Shard contains failure: " + failedCount);
            return true;
        }

        return false;

    }


    protected List<FetchResult> parseMgetResponse(JSONObject responseJson) {

        JSONArray responseDocs = responseJson.getJSONArray("docs");
        List<FetchResult> result = new ArrayList<>();
        for (int i = 0; i < responseDocs.size(); i++) {
            JSONObject doc = responseDocs.getJSONObject(i);
            Boolean found = doc.getBoolean("found");
            if (found == null) continue;
            if (found.equals(Boolean.FALSE)) continue;
            FetchResult re = new FetchResult(doc.getString("_type"), doc.getString("_id"), doc.getJSONObject("_source"));
            result.add(re);
        }
        return result;

    }


}

