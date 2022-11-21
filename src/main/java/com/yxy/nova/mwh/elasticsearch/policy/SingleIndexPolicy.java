package com.yxy.nova.mwh.elasticsearch.policy;

import com.yxy.nova.mwh.elasticsearch.basic.where.WhereCondition;
import com.yxy.nova.mwh.elasticsearch.exception.ESExceptionType;
import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import com.yxy.nova.mwh.elasticsearch.policy.partition.PartitionPolicy;
import com.yxy.nova.mwh.elasticsearch.policy.vo.Index;
import com.yxy.nova.mwh.elasticsearch.policy.vo.PartitionIndex;
import com.yxy.nova.mwh.elasticsearch.util.AnalysisConfigUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.*;

/**
 * 单索引策略
 * @author quyuanwen
 */
public class SingleIndexPolicy extends AbstractPolicy{

    private Index index = new Index();
    /**
     * 配置内容格式：
     * {
     *      "class": "com.yxy.nova.mwh.elasticsearch.policy.SingleIndexPolicy",
     *      "index":{
     *              "read": "",
     *              "write": "",
     *              "defaultTable": {
     *                      "name": "",
     *                      "idField": ""
     *              },
     *              "tables": [{
     *                      "name": "",
     *                      "idField": "",
     *                      "routeShip": "",
     *                      "routeMap": [{
     *                              "field": "",
     *                              "values": ["", ""]
     *                      }]
     *              }]
     *      }
     * }
     * @param policyConfig
     * @param clusterName
     * @throws ElasticsearchClientException
     */
    public SingleIndexPolicy(JSONObject policyConfig, String clusterName) throws ElasticsearchClientException {
        super(policyConfig,clusterName);
    }

    @Override
    protected Set<String> analysis(JSONObject policyConfig) throws ElasticsearchClientException{
        JSONObject indexConfig = policyConfig.getJSONObject("index");
        if(indexConfig == null){
            throw new ElasticsearchClientException("没有定义索引策略规则", ESExceptionType.POLICY_ERROR);
        }
        Set<String> result = new HashSet<>();
        AnalysisConfigUtil.analysisCommenIndex(indexConfig, this.index);
        if(this.index.getReadIndex().equals(this.index.getWriteIndex())){
            result.add(this.index.getReadIndex());
            return result;
        }else{
            result.add(this.index.getReadIndex());
            result.add(this.index.getWriteIndex());
            return result;
        }
    }

    @Override
    public List<String> getReadIndices(String tableName,List<WhereCondition> conditions, boolean ignoreLimit) throws ElasticsearchClientException {
        return Arrays.asList(index.getReadIndex());
    }

    @Override
    public List<String> getActualIndices(String tableName,List<WhereCondition> conditions, boolean ignoreLimitm) throws ElasticsearchClientException{
        return Arrays.asList(index.getReadIndex());
    }

    @Override
    public String getWriteIndex(String tableName, Map<String, Object> param) throws ElasticsearchClientException {
        //单索引直接返回
        return this.index.getWriteIndex();
    }

    @Override
    public String getReadIndex(String tableName, Map<String, Object> param) throws ElasticsearchClientException {
        return index.getReadIndex();
    }

    @Override
    public String getPartitionField(String tableName, List<WhereCondition> conditions) throws ElasticsearchClientException {
        return null;
    }

    @Override
    public List<PartitionIndex> getPartitionIndex(List<WhereCondition> conditions) throws ElasticsearchClientException {
        return null;
    }

    @Override
    public PartitionPolicy getPartitionPolicy(String aliasPrefix) {
        return null;
    }

}
