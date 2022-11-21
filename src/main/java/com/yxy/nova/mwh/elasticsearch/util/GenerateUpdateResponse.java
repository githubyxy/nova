package com.yxy.nova.mwh.elasticsearch.util;

import com.yxy.nova.mwh.elasticsearch.dto.BulkUpdateResult;
import com.yxy.nova.mwh.elasticsearch.dto.BulkUpdateResultItem;
import com.yxy.nova.mwh.elasticsearch.exception.ESExceptionType;
import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.rest.RestStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * 返回结果处理
 * @author quyuanwen
 */
public class GenerateUpdateResponse {

    public static BulkUpdateResult convertToBulkUpdateResponse(JSONObject responseJson){

        Long took = responseJson.getLong("took");
        if (took==null){
            took=0L;
        }

        JSONArray items = responseJson.getJSONArray("items");

        List<BulkUpdateResultItem> itemList=new ArrayList<>();
        int failNumber = 0;
        for (Object itemObject : items){
            JSONObject item = (JSONObject)itemObject;
            BulkUpdateResultItem bulkUpdateResultItem = convertToBulkUpdateResultItem(item);
            if (bulkUpdateResultItem!=null){
                if(!bulkUpdateResultItem.isSuccess()){
                    failNumber++;
                }
                itemList.add(bulkUpdateResultItem);
            }
        }
        return new BulkUpdateResult(itemList, failNumber, took);
    }

    public static BulkUpdateResultItem convertToBulkUpdateResultItem(JSONObject jsonObject){
        if (jsonObject==null){
            return null;
        }

        if (!jsonObject.containsKey("update")){
            return null;
        }

        BulkUpdateResultItem bulkUpdateResultItem = new BulkUpdateResultItem();
        JSONObject updateJsonObj = jsonObject.getJSONObject("update");
        int status = updateJsonObj.getIntValue("status");
        String index = updateJsonObj.getString("_index");
        String type = updateJsonObj.getString("_type");
        String id = updateJsonObj.getString("_id");

        bulkUpdateResultItem.setIndex(index);
        bulkUpdateResultItem.setType(type);
        bulkUpdateResultItem.setId(id);

        if (status != 200){
            bulkUpdateResultItem.setSuccess(false);
        }else{
            bulkUpdateResultItem.setSuccess(true);
        }
        return bulkUpdateResultItem;
    }


    public static BulkResponse convertToBulkResponse(JSONObject response) throws ElasticsearchClientException{
        // 耗时
        JSONObject result = response;
        Long took = result.getLong("took");
        if (took == null) {
            took = 0L;
        }

        // 每个请求的结果
        JSONArray items = result.getJSONArray("items");
        int i = -1;
        List<BulkItemResponse> itemList = new ArrayList<>();
        for (Object itemObject : items) {
            i++;
            JSONObject item = (JSONObject) itemObject;
            itemList.add(convertBulkItemResponse(item));
        }

        // 组装最终结果
        return new BulkResponse(itemList.toArray(new BulkItemResponse[0]), took);
    }



    private static BulkItemResponse convertBulkItemResponse(JSONObject item) throws ElasticsearchClientException{
        if (item.containsKey("create")) {
            return convertBulkItemResponseCreate(item.getJSONObject("create"));
        } else if (item.containsKey("index")) {
            return convertBulkItemResponseCreate(item.getJSONObject("index"));
        } else if (item.containsKey("delete")) {
            // 输出错误
            throw new ElasticsearchClientException("不支持删除", ESExceptionType.REQUEST_ERROR);
        } else if (item.containsKey("update")) {
            // 输出错误
            throw new ElasticsearchClientException("不支持更新", ESExceptionType.REQUEST_ERROR);
        } else {
            // 输出错误
            throw new ElasticsearchClientException("不能识别请求的操作", ESExceptionType.REQUEST_ERROR);
        }
    }

    private static BulkItemResponse convertBulkItemResponseCreate(JSONObject item) {
        // 写入失败的情况
        String index = item.getString("_index");
        String type = item.getString("_type");
        String id = item.getString("_id");
        String error = item.getString("error");
        if (error != null) {
            Integer status = item.getInteger("status");
            RestStatus restStatus = getRestStatus(status);
            BulkItemResponse.Failure failure = new BulkItemResponse.Failure(index, type, id, error, restStatus);
            return new BulkItemResponse(0, "index", failure);
        }

        // 写入成功的情况
        Long version = item.getLong("_version");
        if (version == null) version = 0L;
        Boolean created = item.getBoolean("created");
        IndexResponse indexResponse = new IndexResponse(index, type, id, version, created);
        return new BulkItemResponse(0, "index", indexResponse);
    }

    private static RestStatus getRestStatus(Integer status) {
        if (status == null) {
            return null;
        }
        for (RestStatus e : RestStatus.values()) {
            if (e.getStatus() == status) {
                return e;
            }
        }
        return null;
    }


    /**
     * 返回空的数据
     * @return
     */
    public static JSONObject emptyResponseJson() {
        // 定义结果
        JSONObject result = new JSONObject();

        // 放入基本的数据
        result.put("took", -1);
        result.put("timed_out", false);
        JSONObject shards = new JSONObject();
        shards.put("total", 1);
        shards.put("successful", 1);
        shards.put("failed", 0);
        result.put("_shards", shards);

        // 生成hits
        JSONObject hits = new JSONObject();
        hits.put("hits", new JSONArray());
        hits.put("max_score", 1.0d);
        hits.put("total", 0);
        result.put("hits", hits);

        // 组装结果
        return result;
    }
}
