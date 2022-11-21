package com.yxy.nova.mwh.elasticsearch.policy;

import com.yxy.nova.mwh.elasticsearch.admin.ElasticSearchIndexAndAlias;
import com.yxy.nova.mwh.elasticsearch.basic.sort.SortMode;
import com.yxy.nova.mwh.elasticsearch.basic.where.WhereCondition;
import com.yxy.nova.mwh.elasticsearch.exception.ESExceptionType;
import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import com.yxy.nova.mwh.elasticsearch.policy.partition.PartitionPolicy;
import com.yxy.nova.mwh.elasticsearch.policy.route.RouteMap;
import com.yxy.nova.mwh.elasticsearch.policy.route.RouteShip;
import com.yxy.nova.mwh.elasticsearch.policy.vo.FieldMapCondition;
import com.yxy.nova.mwh.elasticsearch.policy.vo.PartitionAliases;
import com.yxy.nova.mwh.elasticsearch.policy.vo.PartitionIndex;
import com.yxy.nova.mwh.elasticsearch.policy.vo.Table;
import com.yxy.nova.mwh.elasticsearch.util.AnalysisConditionUtil;
import com.yxy.nova.mwh.elasticsearch.util.AnalysisConfigUtil;
import com.yxy.nova.mwh.elasticsearch.util.Constants;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 索引路由策略
 * @author quyuanwen
 * @version 2020-03-02
 */
public class IndexRoutePolicy extends AbstractPolicy {

    private static Logger logger = LoggerFactory.getLogger(IndexRoutePolicy.class);

    private PartitionIndex defaultPartitionIndex;

    private RouteShip<PartitionIndex> routeShip;

    private List<PartitionIndex> otherPartitionIndices;

    private static int OPTIMIZE_THRESHOLD = 20;
    /**
     * 多索引分区
     *{
     *      "defaultIndex": {
     *              "class":"com.yxy.nova.mwh.elasticsearch.policy.partition.DayPartitionPolicy",
     *              "partition": {
     *                      "indexReadPrefix": "forseti-r-",
     *                      "indexWritePrefix": "forseti-w-",
     *                      "indexPrefix": "forseti-",
     *                      "searchLimit": 7776000000,
     *                      "readFormat": "",
     *                      "writeFormat": "",
     *                      "defaultField": "",
     *                      "depend":[{
     *                              "table": "",
     *                              "field": ""
     *                      }]
     *              },
     *              "defaultTable": {
     *                      "name": "",
     *              },
     *              "tables": [{
     *                      "name": "",
     *                      "routeShip": "",
     *                      "routeMap": [{
     *                              "field": "",
     *                              "values": ["", ""]
     *                      }]
     *              }]
     *      },
     *      "indices": [{
     *              "class":"com.yxy.nova.mwh.elasticsearch.policy.partition.DayPartitionPolicy",
     *              "partition": {
     *                      "indexReadPrefix": "forseti-r-",
     *                      "indexWritePrefix": "forseti-w-",
     *                      "indexPrefix": "forseti-",
     *                      "searchLimit": 7776000000,
     *                      "readFormat": "",
     *                      "writeFormat": "",
     *                      "defaultField": "",
     *                      "depend":[{
     *                              "table": "",
     *                              "field": ""
     *                      }]
     *              },
     *              "routeShip": "",
     *              "routeMap": [{
     *                      "field": "",
     *                      "values": ["", ""]
     *              }],
     *              "defaultTable": {
     *                      "name": "",
     *              },
     *              "tables": [{
     *                      "name": "",
     *                      "routeShip": "",
     *                      "routeMap": [{
     *                              "field": "",
     *                              "values": ["", ""]
     *                      }]
     *              }]
     *      }]
     *}
     * @param policyConfig
     * @param clusterName
     * @throws ElasticsearchClientException
     */
    public IndexRoutePolicy(JSONObject policyConfig, String clusterName) throws ElasticsearchClientException {
        super(policyConfig, clusterName);
    }

    @Override
    public List<String> getReadIndices(String tableName, List<WhereCondition> conditions, boolean ignoreLimit) throws ElasticsearchClientException {
        PartitionAliases aliases = this.getPartitionAliases(tableName, conditions, ignoreLimit);
        if (!CollectionUtils.isEmpty(aliases.getReadAliases())) {
            //简化返回的索引数量
            return this.getSimplifyAliases(aliases.getPartitionPolicy().getIndexReadPrefix(), aliases.getReadAliases(), aliases.getSortMode());
        }
        return null;
    }

    @Override
    public List<String> getActualIndices(String tableName,List<WhereCondition> conditions, boolean ignoreLimit) throws ElasticsearchClientException{

        FieldMapCondition fieldMapCondition = AnalysisConditionUtil.getFieldMapCondition( conditions);

        List<PartitionIndex> partitionIndices = getPartitionIndex(fieldMapCondition.getRuteValueMap(), true);

        if(CollectionUtils.isEmpty(partitionIndices)){
            return new ArrayList<>();
        }
        Set<String> readAliases = new HashSet<>();
        for(PartitionIndex partitionIndex : partitionIndices){
            readAliases.addAll(partitionIndex.getPartitionPolicy()
                    .getReadAliases(tableName, fieldMapCondition, ignoreLimit));
        }

        //获取实际索引
        ElasticSearchIndexAndAlias elasticSearchIndexAndAlias = this.getAllAliases();
        Set<String> indexSet = new HashSet<>();
        for(String readAlias : readAliases){
            List<String> indices = elasticSearchIndexAndAlias.getIndicesByAlias(readAlias);
            indexSet.addAll(indices);
        }

        //获取排序规则，如果没有默认降序,多分区索引只区第一个字段排序
        SortMode sortMode = AnalysisConditionUtil.findFirstSort(conditions,
                partitionIndices.get(0).getPartitionPolicy().getPartitionField(tableName));
        if (sortMode == null) {
            sortMode = SortMode.DESC;
        }
        List<String> actualIndices = new ArrayList<>(indexSet);

        super.sort(actualIndices, sortMode);

        return actualIndices;
    }

    @Override
    public String getPartitionField(String tableName, List<WhereCondition> conditions) throws ElasticsearchClientException{
        //获取路由字段
        FieldMapCondition fieldMapCondition = AnalysisConditionUtil.getFieldMapCondition(conditions);

        List<PartitionIndex> partitionIndices = getPartitionIndex(fieldMapCondition.getRuteValueMap(), true);

        if(CollectionUtils.isEmpty(partitionIndices)){
           throw new ElasticsearchClientException("获取对应的索引信息错误！", ESExceptionType.POLICY_ERROR);
        }
        for(PartitionIndex partitionIndex : partitionIndices){
            String patitionField = partitionIndex.getPartitionPolicy().getPartitionField(tableName);
            //获取对应的分区字段
            if(StringUtils.isNotBlank(patitionField)){
                //判断是否存在
                if(fieldMapCondition.getGreater(patitionField) != null){
                    return patitionField;
                }else if(fieldMapCondition.getGreaterOrEqualMap(patitionField) != null){
                    return patitionField;
                }else if(fieldMapCondition.getLessMap(patitionField) != null){
                    return patitionField;
                }else if(fieldMapCondition.getLessOrEqualMap(patitionField) != null){
                    return patitionField;
                }else if(fieldMapCondition.getEqualsMap(patitionField) != null){
                    return patitionField;
                }
            }
        }
        throw new ElasticsearchClientException("查询分区索引的条件中没有包含分区字段！", ESExceptionType.POLICY_ERROR);
    }
    /**
     * 获取分区索引信息
     *
     * @param tableName
     * @param conditions
     * @return
     * @throws ElasticsearchClientException
     */
    protected PartitionAliases getPartitionAliases(String tableName, List<WhereCondition> conditions, boolean ignoreLimit) throws ElasticsearchClientException {

        FieldMapCondition fieldMapCondition = AnalysisConditionUtil.getFieldMapCondition(conditions);

        Map<String, Object> routeValue = fieldMapCondition.getRuteValueMap();

        List<PartitionIndex> partitionIndices = getPartitionIndex(routeValue, true);

        //多索引默认取第一个
        if(CollectionUtils.isEmpty(partitionIndices)){
            return new PartitionAliases(new ArrayList<String>(), null, null);
        }
        PartitionIndex partitionIndex = partitionIndices.get(0);

        //id不需要确定分区
        Constants.IDINCONDITION idincondition = AnalysisConditionUtil.containId(conditions);
        if (idincondition == Constants.IDINCONDITION.ONLYID) {
            return new PartitionAliases(Arrays.asList(partitionIndex.getPartitionPolicy().getIndexReadPrefix() + "*")
                    , partitionIndex.getPartitionPolicy(), SortMode.DESC);
        }

        //路由到表
        if (StringUtils.isBlank(tableName)) {
            tableName = getTableName(partitionIndex, routeValue, true);

        }

        Set<String> readAliases = partitionIndex.getPartitionPolicy()
                .getReadAliases(tableName, fieldMapCondition, ignoreLimit);

        //获取排序规则，如果没有默认降序
        SortMode sortMode = AnalysisConditionUtil.findFirstSort(conditions,
                partitionIndex.getPartitionPolicy().getPartitionField(tableName));
        if (sortMode == null) {
            sortMode = SortMode.DESC;
        }
        List<String> result = null;
        if (CollectionUtils.isEmpty(readAliases)) {
            return new PartitionAliases(new ArrayList<String>(), partitionIndex.getPartitionPolicy(), null);
        } else if (readAliases.size() == 1) {
            //判断是否查全部索引
            String aliase = readAliases.iterator().next();
            if (aliase.endsWith("*")) {//星号是查全部索引
                result = Arrays.asList(aliase);
                return new PartitionAliases(result, partitionIndex.getPartitionPolicy(), sortMode);
            }
        }

        //去除不存在的别名
        result = super.removeNonExistAlias(readAliases);
        if (result == null) {
            logger.warn("result is null,readAliases:{}", readAliases);
        }

        super.sort(result, sortMode);

        return new PartitionAliases(result, partitionIndex.getPartitionPolicy(), sortMode);
    }

    @Override
    public String getWriteIndex(String tableName, Map<String, Object> param) throws ElasticsearchClientException {
        List<PartitionIndex> partitionIndices =  getPartitionIndex(param, false);
        //多索引默认取第一个
        if(CollectionUtils.isEmpty(partitionIndices)){
            return null;
        }
        PartitionIndex partitionIndex = partitionIndices.get(0);
        if (tableName == null) {
            tableName = getTableName(partitionIndex, param, false);
        }
        //根据表和分区字段获取索引分区
        return partitionIndex.getPartitionPolicy().getWriteAlias(tableName, param);
    }


    @Override
    public String getReadIndex(String tableName, Map<String, Object> param) throws ElasticsearchClientException {
        List<PartitionIndex> partitionIndices =  getPartitionIndex(param, true);
        //多索引默认取第一个
        if(CollectionUtils.isEmpty(partitionIndices)){
            return null;
        }
        PartitionIndex partitionIndex = partitionIndices.get(0);
        if (tableName == null) {
            tableName = getTableName(partitionIndex, param, true);
        }
        return partitionIndex.getPartitionPolicy().getReadAlias(tableName, param);
    }

    /**
     * 根据条件获取分区索引
     * @param conditions
     * @return
     * @throws ElasticsearchClientException
     */
    @Override
    public List<PartitionIndex> getPartitionIndex(List<WhereCondition> conditions) throws ElasticsearchClientException {
        FieldMapCondition fieldMapCondition = AnalysisConditionUtil.getFieldMapCondition(conditions);
        return getPartitionIndex(fieldMapCondition.getRuteValueMap(), true);
    }

    @Override
    public PartitionPolicy getPartitionPolicy(String aliasPrefix) {
        if(!CollectionUtils.isEmpty(otherPartitionIndices)) {
            for (PartitionIndex partitionIndex : otherPartitionIndices) {
                if (partitionIndex.getPartitionPolicy().getIndexReadPrefix().equals(aliasPrefix)) {
                    return partitionIndex.getPartitionPolicy();
                }
                if (partitionIndex.getPartitionPolicy().getIndexWritePrefix().equals(aliasPrefix)) {
                    return partitionIndex.getPartitionPolicy();
                }
            }
        }
        return defaultPartitionIndex.getPartitionPolicy();
    }

    protected Set<String> analysis(JSONObject policyConfig) throws ElasticsearchClientException {
        JSONObject indexConfig = policyConfig.getJSONObject("defaultIndex");
        if(indexConfig == null){
            indexConfig = policyConfig.getJSONObject("index");
        }
        if(indexConfig == null){
            throw new ElasticsearchClientException("索引路由策略定义错误", ESExceptionType.POLICY_ERROR);
        }


        Set<String> indexPrefixes = new HashSet<>();
        Table defaultTable = AnalysisConfigUtil.analysisDefaultTable(indexConfig);
        this.defaultPartitionIndex = new PartitionIndex(PolicyFactory.getPartitionPolicy(indexConfig),
                defaultTable, AnalysisConfigUtil.analysisTableRouteShip(indexConfig, defaultTable));

        //记录索引前缀信息
        this.addIndexPrefixes(indexPrefixes, this.defaultPartitionIndex);

        JSONArray indexConfigs = policyConfig.getJSONArray("indices");
        if(!CollectionUtils.isEmpty(indexConfigs)){
            RouteShip<PartitionIndex> next = null;
            otherPartitionIndices = new ArrayList<>();
            for(int i = 0; i < indexConfigs.size(); i++){
                JSONObject jsonObject = indexConfigs.getJSONObject(i);

                //生成分区索引信息
                Table table = AnalysisConfigUtil.analysisDefaultTable(jsonObject);
                PartitionIndex partitionIndex = new PartitionIndex(PolicyFactory.getPartitionPolicy(jsonObject),
                        table, AnalysisConfigUtil.analysisTableRouteShip(indexConfig, table));

                otherPartitionIndices.add(partitionIndex);

                //记录索引前缀信息
                this.addIndexPrefixes(indexPrefixes, partitionIndex);

                //生成路由信息
                JSONArray routeMapArray = jsonObject.getJSONArray("routeMap");
                if(CollectionUtils.isEmpty(routeMapArray)){
                    throw new ElasticsearchClientException("多索引路由配置不正确", ESExceptionType.POLICY_ERROR);
                }
                RouteMap[] routeMaps = new RouteMap[routeMapArray.size()];
                for(int n = 0; n < routeMapArray.size(); n++){
                    JSONObject routeMapObject = routeMapArray.getJSONObject(n);
                    routeMaps[n] = new RouteMap(routeMapObject.getString("field"),routeMapObject.getJSONArray("values"));
                }

                if(next == null){
                    this.routeShip = new RouteShip(jsonObject.getString("routeShip"), partitionIndex, routeMaps);
                    next = this.routeShip ;
                }else{
                    next = next.append(jsonObject.getString("routeShip"), partitionIndex, routeMaps);
                }
            }
            next.appendDefault(defaultPartitionIndex);
        }
        return indexPrefixes;
    }

    /**
     * 获取路由到的分区索引
     *
     * @param routeValue
     * @param flatten 字段是否为拉平
     * @return
     * @throws ElasticsearchClientException
     */
    private List<PartitionIndex> getPartitionIndex(Map<String, Object> routeValue, boolean flatten) throws ElasticsearchClientException{
        List<PartitionIndex> partitionIndices = new ArrayList<>();
        if(routeValue != null && routeShip != null){
            PartitionIndex partitionIndex = routeShip.getNode(routeValue, flatten);
            if(partitionIndex != null){
                partitionIndices.add(partitionIndex);
            }
        }
        //没有路由到索引返回全部
        if(CollectionUtils.isEmpty(partitionIndices)){
            partitionIndices.add(defaultPartitionIndex);
            if(!CollectionUtils.isEmpty(otherPartitionIndices)){
                partitionIndices.addAll(otherPartitionIndices);
            }
        }
        return partitionIndices;
    }


    /**
     * 添加索引前缀信息
     * @param indexPrefixes
     * @param partitionIndex
     */
    private void addIndexPrefixes(Set<String> indexPrefixes, PartitionIndex partitionIndex){
        //记录索引信息
        indexPrefixes.add(partitionIndex.getPartitionPolicy().getIndexReadPrefix());
        indexPrefixes.add(partitionIndex.getPartitionPolicy().getIndexWritePrefix());
    }


    /**
     * 获取表名
     *
     * @param partitionIndex
     * @param param
     * @param flatten
     * @return
     * @throws ElasticsearchClientException
     */
    private String getTableName(PartitionIndex partitionIndex, Map<String, Object> param, boolean flatten) throws ElasticsearchClientException {
        //根据参数获取路由表
        Table table = null;
        if (partitionIndex.getRouteShip() != null) {
            table = partitionIndex.getRouteShip().getNode(param, flatten);
        } else {
            table = partitionIndex.getDefaultTable();
        }
        return table.getName();
    }

    /**
     * 精简别名
     *
     * @param prefix
     * @param aliases
     * @param sortMode
     * @return
     * @throws ElasticsearchClientException
     */
    private List<String> getSimplifyAliases(String prefix, List<String> aliases,  SortMode sortMode) throws ElasticsearchClientException {

        // 数量较小,不需要优化
        if (aliases.size() <= OPTIMIZE_THRESHOLD) return aliases;

        ElasticSearchIndexAndAlias elasticSearchIndexAndAlias = this.getAllAliases();
        if (elasticSearchIndexAndAlias != null) {
            List<String> allAliases = elasticSearchIndexAndAlias.getSortAliases();
            if (allAliases != null) {
                int afterEnd = -1;
                int beforBegin = -1;
                String beforBeginAlias = null;
                String afterEndAlias = null;
                String first = aliases.get(0);
                String last = aliases.get(aliases.size() - 1);
                if (sortMode == SortMode.DESC) {
                    afterEnd = allAliases.indexOf(first) - 1;
                    beforBegin = allAliases.indexOf(last) + 1;
                } else {
                    afterEnd = allAliases.indexOf(last) - 1;
                    beforBegin = allAliases.indexOf(first) + 1;
                }
                if (beforBegin < allAliases.size()) {
                    beforBeginAlias = allAliases.get(beforBegin);
                }
                if (afterEnd > -1) {
                    afterEndAlias = allAliases.get(afterEnd);
                }

                return this.getSimplifyAliases(prefix, aliases, beforBeginAlias, afterEndAlias);
            }
        }
        throw new ElasticsearchClientException("获取服务器的索引别名缓存信息为空", ESExceptionType.CACHE_ERROR);
    }

    /**
     * 精简别名
     *
     * @param prefix
     * @param aliases
     * @param beforBeginAlias
     * @param afterEndAlias
     * @return
     */
    private List<String> getSimplifyAliases(String prefix, List<String> aliases, String beforBeginAlias, String afterEndAlias) {
        String newPrefix = prefix;
        List<String> simplifyAliases = new ArrayList<>();

        for (int i = 0; i < aliases.size(); ) {
            String alias = aliases.get(i);
            if (newPrefix.equals(alias)) {
                simplifyAliases.add(newPrefix);
                i++;
                continue;
            }
            if (checkPrefix(beforBeginAlias, afterEndAlias, newPrefix)) {
                if (alias.startsWith(newPrefix)) {
                    i++;
                    if (i == aliases.size()) {//达到上限跳出循环
                        simplifyAliases.add(newPrefix + "*");
                    }
                } else {
                    //出现了不一样的情况，先记录之前生成别名信息
                    simplifyAliases.add(newPrefix + "*");
                    int length = newPrefix.length();
                    //从后往前推,找出遇上一个newPrefix，不一样的别名
                    while (true) {
                        String temp = alias.substring(0, length);
                        //直到新截取的字符串是以前别名的开头，新生成别名的值为上一次截取值
                        if (newPrefix.startsWith(temp)) {
                            newPrefix = alias;
                            break;
                        }
                        alias = temp;
                        length--;
                    }
                }
            } else {
                newPrefix = alias.substring(0, newPrefix.length() + 1);
            }
        }
        return simplifyAliases;
    }

    /**
     * 校验别名是否存在非查询中中
     *
     * @param beforBeginAlias
     * @param afterEndAlias
     * @param newPrefix
     * @return
     */
    private static boolean checkPrefix(String beforBeginAlias, String afterEndAlias, String newPrefix) {
        if (beforBeginAlias != null && beforBeginAlias.startsWith(newPrefix)) {
            return false;
        }
        if (afterEndAlias != null && afterEndAlias.startsWith(newPrefix)) {
            return false;
        }
        return true;
    }
}
