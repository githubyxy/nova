package com.yxy.nova.mwh.elasticsearch;

import com.yxy.nova.mwh.elasticsearch.basic.select.SearchBuilder;
import com.yxy.nova.mwh.elasticsearch.basic.where.WhereCondition;
import com.yxy.nova.mwh.elasticsearch.dto.*;
import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import com.yxy.nova.mwh.elasticsearch.policy.Policy;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchResponse;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 搜索服务。目前底层用ElasticSearch作为搜索引擎 <br>
 * 用法示例： searchService.selectFrom("activity").whereEquals("age", 10).whereEquals("name","Alice").list(); <br>
 *
 * @author caipeichao
 */
public interface SearchService {

    /**
     * 构建查询对象
     * @param table
     * @return
     */
    SearchBuilder selectFrom(String table);

    SearchBuilder selectFromAll();

    /**
     * 批量更新接口
     */
    BulkUpdateResult bulkUpdate(List<UpdateAction> actions) throws ElasticsearchClientException;

    void insertInto(String table, String id, Map<String, Object> json) throws ElasticsearchClientException;

    BulkResponse bulkInsert(List<InsertAction> actions) throws ElasticsearchClientException;

    List<Map<String, Object>> list(String table, List<WhereCondition> conditions) throws ElasticsearchClientException;

    Iterator<Map<String, Object>> iterate(String table, List<WhereCondition> conditions, int fetchSize) throws ElasticsearchClientException;

    /**
     * 游标异步查询获取
     * @param table
     * @param conditions
     * @param fetchSize
     * @param cacheSize
     * @return
     * @throws ElasticsearchClientException
     */
    AsyncScrollIterator asyncIterate(String table, List<WhereCondition> conditions, int fetchSize, int cacheSize) throws ElasticsearchClientException;

    SearchResponse response(String table, List<WhereCondition> conditions) throws ElasticsearchClientException;

    SearchResult get(String table, List<WhereCondition> conditions) throws ElasticsearchClientException;

    void executeUpdate(String table, String id, Date eventOccurTime, Map<String, Object> sets) throws ElasticsearchClientException;

    List<FetchResult> fetchById(List<FetchAction> fetchActions) throws ElasticsearchClientException;

    List<DeleteResult> delete(List<DeleteAction> deleteActions) throws ElasticsearchClientException;

    /**
     * 获取索引策略
     * @return
     */
    Policy getPolicy();
}
