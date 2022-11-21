package com.yxy.nova.mwh.elasticsearch.policy;

import com.yxy.nova.mwh.elasticsearch.admin.Connection;
import com.yxy.nova.mwh.elasticsearch.admin.ElasticSearchIndexAndAlias;
import com.yxy.nova.mwh.elasticsearch.basic.where.WhereCondition;
import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import com.yxy.nova.mwh.elasticsearch.policy.partition.PartitionPolicy;
import com.yxy.nova.mwh.elasticsearch.policy.vo.PartitionIndex;

import java.util.List;
import java.util.Map;

/**
 * 索引策略
 * @author quyuanwen
 */
public interface Policy {

    /**
     *策略初始化方法
     * @param connection
     * @throws ElasticsearchClientException
     */
    void init(Connection connection) throws ElasticsearchClientException;

    /**
     * 获取全部集群对应的别名信息
     *
     * @return
     */
    ElasticSearchIndexAndAlias getAllAliases();
    /**
     *
     * 获取读别名或索引集合
     * @param tableName
     * @param conditions
     * @param ignoreLimit 是否忽视查询范围限制，适用迭代和根据id查询
     * @return
     * @throws ElasticsearchClientException
     */
    List<String> getReadIndices(String tableName, List<WhereCondition> conditions, boolean ignoreLimit) throws ElasticsearchClientException;


    /**
     * 获取实际的索引信息
     * @param tableName
     * @param conditions
     * @param ignoreLimit
     * @return
     * @throws ElasticsearchClientException
     */
    List<String> getActualIndices(String tableName, List<WhereCondition> conditions, boolean ignoreLimit) throws ElasticsearchClientException;


    /**
     * 获取写别名或索引集合
     * @param tableName
     * @param param
     * @return
     * @throws ElasticsearchClientException
     */
    String getWriteIndex(String tableName, Map<String, Object> param) throws ElasticsearchClientException;

    /**
     * 根据路由值和分片值获取索引信息
     *
     * @param tableName
     * @param param 路由和分片参数，key为扁平化
     * @return
     * @throws ElasticsearchClientException
     */
    String getReadIndex(String tableName, Map<String, Object> param) throws ElasticsearchClientException;

    /**
     * 当前策略包含的索引别名或索引名
     * @return
     */
    List<String> getIndexPrefixes();

    /**
     * 根据表和条件信息获取分区字段
     * @param tableName
     * @param conditions
     * @return
     */
    String getPartitionField(String tableName, List<WhereCondition> conditions) throws ElasticsearchClientException;

    /**
     * 根据条件获取分区索引
     * @param conditions
     * @return
     * @throws ElasticsearchClientException
     */
    List<PartitionIndex> getPartitionIndex(List<WhereCondition> conditions) throws ElasticsearchClientException;

    /**
     * 根据索引别名前缀获取索引策略，别名以"-"结尾
     *
     * @param aliasPrefix
     * @return
     */
    PartitionPolicy getPartitionPolicy(String aliasPrefix);
}
