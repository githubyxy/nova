package com.yxy.nova.mwh.elasticsearch.policy.partition;

import com.yxy.nova.mwh.elasticsearch.basic.where.*;
import com.yxy.nova.mwh.elasticsearch.exception.ESExceptionType;
import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import com.yxy.nova.mwh.elasticsearch.policy.vo.FieldMapCondition;
import com.yxy.nova.mwh.elasticsearch.policy.vo.PartitionFieldValue;
import com.yxy.nova.mwh.elasticsearch.util.JsonPath;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 分区策略模板
 * @author quyuanwen
 */
public abstract class AbstractPartitionPolicy implements PartitionPolicy{

    /**
     * 拉平字段
     */
    private String defaultField;

    /**
     * 层次字段
     */
    private String[] defaultSliceField;

    private String indexReadPrefix;

    private String indexWritePrefix;

    private String indexPrefix;

    private long searchLimit;
    /**
     * 表与拉平字段映射
     */
    private Map<String,String> tableFieldMap = new HashMap<>();


    /**
     * 表与层次字段映射
     */
    private Map<String,String[]> tableSliceFieldMap = new HashMap<>();

    /**
     * 初始化分区策略
     * @param indexConfig
     * @throws ElasticsearchClientException
     */
    public AbstractPartitionPolicy(JSONObject indexConfig) throws ElasticsearchClientException {

        JSONObject partitionConfig = indexConfig.getJSONObject("partition");
        if(partitionConfig == null){
            throw new ElasticsearchClientException("没有定义索引策略规则", ESExceptionType.POLICY_ERROR);
        }
        this.indexReadPrefix = partitionConfig.getString("indexReadPrefix");
        if(StringUtils.isBlank(this.indexReadPrefix) || StringUtils.isBlank(this.indexReadPrefix)){
            throw new ElasticsearchClientException("没有读索引前缀定义", ESExceptionType.POLICY_ERROR);
        }
        this.indexWritePrefix = partitionConfig.getString("indexWritePrefix");
        if(StringUtils.isBlank(this.indexWritePrefix) || StringUtils.isBlank(this.indexWritePrefix)){
            throw new ElasticsearchClientException("没有写索引前缀定义", ESExceptionType.POLICY_ERROR);
        }
        this.indexPrefix = partitionConfig.getString("indexPrefix");
        this.searchLimit = partitionConfig.getLongValue("searchLimit");
        this.defaultField= partitionConfig.getString("defaultField");
        if(StringUtils.isBlank(defaultField)){
            throw new ElasticsearchClientException("必须设置默认分区字段", ESExceptionType.POLICY_ERROR);
        }

        defaultSliceField = defaultField.split("\\.");

        //解析分区信息
        JSONArray dependArray = partitionConfig.getJSONArray("depend");
        if(!CollectionUtils.isEmpty(dependArray)){
            for(int i = 0; i < dependArray.size(); i++){
                JSONObject dependbject = dependArray.getJSONObject(i);
                String table = dependbject.getString("table");
                String field = dependbject.getString("field");
                if(StringUtils.isBlank(table) || StringUtils.isBlank(field)){
                    throw new ElasticsearchClientException("缺少分区字段定义", ESExceptionType.POLICY_ERROR);
                }
                tableFieldMap.put(table,field);
                tableSliceFieldMap.put(table,field.split("\\."));
            }
        }
        //扩展内容解析
        analysis(partitionConfig);
    }

    @Override
    public Set<String> getReadAliases(String tableName, FieldMapCondition fieldMapCondition, boolean ignoreLimit) throws ElasticsearchClientException {
        //获取表的分区字段
        if(fieldMapCondition != null){
            PartitionFieldValue partitionFieldValue = new PartitionFieldValue();
            //获取分片字段
            String field = this.getPartitionField(tableName);
            boolean noPartitionValue = true;
            WhereGreater greater = fieldMapCondition.getGreater(field);
            if(greater != null){
                noPartitionValue = false;
                partitionFieldValue.setGreater(greater);
            }
            WhereGreaterOrEqual greaterOrEqual = fieldMapCondition.getGreaterOrEqualMap(field);
            if(greaterOrEqual != null){
                noPartitionValue = false;
                partitionFieldValue.setGreaterOrEqual(greaterOrEqual);
            }
            WhereLess less = fieldMapCondition.getLessMap(field);
            if(less != null){
                noPartitionValue = false;
                partitionFieldValue.setLess(less);
            }
            WhereLessOrEqual lessOrEqual = fieldMapCondition.getLessOrEqualMap(field);
            if(lessOrEqual != null){
                noPartitionValue = false;
                partitionFieldValue.setLessOrEqual(lessOrEqual);
            }
            WhereEquals equals = fieldMapCondition.getEqualsMap(field);
            if(equals != null){
                noPartitionValue = false;
                partitionFieldValue.setEquals(equals);
            }
            WhereIn in = fieldMapCondition.getInMap(field);
            if(in != null){
                noPartitionValue = false;
                partitionFieldValue.setIn(in);
            }
            if(!noPartitionValue){
                return generateReadAliases(partitionFieldValue,indexReadPrefix, searchLimit, ignoreLimit);
            }
        }
        return generateDefaultReadAliases(indexReadPrefix, ignoreLimit);
    }

    @Override
    public String getWriteAlias(String tableName, Map<String, Object> param) throws ElasticsearchClientException{
        //获取表的分区字段
        PartitionFieldValue partitionFieldValue = new PartitionFieldValue();

        partitionFieldValue.setEquals(new WhereEquals(null,JsonPath.getJsonValue(param,getPartitionSliceField(tableName))));

        return generateWriteAlias(partitionFieldValue,indexWritePrefix);
    }

    @Override
    public String getReadAlias(String tableName, Map<String, Object> param) throws ElasticsearchClientException{

        //获取表的分区字段
        PartitionFieldValue partitionFieldValue = new PartitionFieldValue();
        if(param == null || param.isEmpty()){
            throw new ElasticsearchClientException("查询分区索引必须传分区字段信息",ESExceptionType.PARTITION_ERROR);
        }else if(param.get("defaultPartition") != null){
            partitionFieldValue.setEquals(new WhereEquals(null,param.get("defaultPartition")));
        }else{
            partitionFieldValue.setEquals(new WhereEquals(null,JsonPath.getJsonValue(param,getPartitionField(tableName))));
        }

        Set<String> aliases = generateReadAliases(partitionFieldValue,indexReadPrefix, searchLimit,false);
        if(!CollectionUtils.isEmpty(aliases)){
            return aliases.iterator().next();
        }
        return null;
    }
    /**
     * 解析阔在分片策略配置
     *
     * @param partitionConfig
     */
    public abstract void analysis(JSONObject partitionConfig);

    /**
     * 根据分区字段值获得分区索引信息
     * @param partitionFieldValue 分区字段值
     * @param indexReadPrefix 读别名前缀
     * @param searchLimit
     * @param ignoreLimit 是否忽视查询范围限制，适用迭代和根据id查询
     * @return
     * @throws ElasticsearchClientException
     */
    protected abstract Set<String> generateReadAliases(PartitionFieldValue partitionFieldValue, String indexReadPrefix, long searchLimit, boolean ignoreLimit) throws ElasticsearchClientException ;

    /**
     * 如果没有任何条件或分区信息，获取默认的分区索引
     * @param indexReadPrefix
     * @param ignoreLimit 是否忽视查询范围限制，适用迭代和根据id查询
     * @return
     * @throws ElasticsearchClientException
     */
    protected abstract Set<String> generateDefaultReadAliases(String indexReadPrefix, boolean ignoreLimit) throws ElasticsearchClientException ;

    /**
     * 写索引
     * @param partitionFieldValue
     * @param indexWritePrefix
     * @return
     * @throws ElasticsearchClientException
     */
    protected abstract String generateWriteAlias(PartitionFieldValue partitionFieldValue, String indexWritePrefix) throws ElasticsearchClientException ;

    @Override
    public String getIndexReadPrefix() {
        return indexReadPrefix;
    }

    @Override
    public String getIndexWritePrefix() {
        return indexWritePrefix;
    }

    @Override
    public String getIndexPrefix() {
        return indexPrefix;
    }

    @Override
    public String getPartitionField(String tableName){
        String field = null;
        if(StringUtils.isNotBlank(tableName)){
            field = tableFieldMap.get(tableName);
        }
        if(field == null){
            field = defaultField;
        }
        return field;
    }

    public String[] getPartitionSliceField(String tableName){
        String[] fields = null;
        if(StringUtils.isNotBlank(tableName)){
            fields = tableSliceFieldMap.get(tableName);
        }
        if(fields == null){
            fields = defaultSliceField;
        }
        return fields;
    }
}
