package com.yxy.nova.mwh.elasticsearch.policy.partition;

import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import com.yxy.nova.mwh.elasticsearch.policy.vo.FieldMapCondition;

import java.util.Map;
import java.util.Set;

/**
 * 索引分区策略接口
 * @author quyuanwen
 */
public interface PartitionPolicy {

    /**
     * 根据表名和参数获取索引信息
     * @param tableName 可以为空
     * @param fieldMapCondition 条件key应该是拉平字段
     * @param ignoreLimit 是否忽视查询范围限制，适用迭代和根据id查询
     * @return
     */
    Set<String> getReadAliases(String tableName, FieldMapCondition fieldMapCondition, boolean ignoreLimit) throws ElasticsearchClientException;


    /**
     * 获得写索引
     * @param tableName
     * @param param
     * @return
     * @throws ElasticsearchClientException
     */
    String getWriteAlias(String tableName, Map<String, Object> param) throws ElasticsearchClientException;

    /**
     * 根据参数获取分区的别名，param的key只能是拉平的字段
     *
     * @param tableName
     * @param param
     * @return
     * @throws ElasticsearchClientException
     */
    String getReadAlias(String tableName, Map<String, Object> param) throws ElasticsearchClientException;

    /**
     * 读别名前缀
     * @return
     */
    String getIndexReadPrefix();

    /**
     * 写别名前缀
     * @return
     */
    String getIndexWritePrefix();

    /**
     * 索引前缀
     * @return
     */
    String getIndexPrefix();

    /**
     * 获取分片字段
     * @param tableName
     * @return
     */
    String getPartitionField(String tableName);

}
