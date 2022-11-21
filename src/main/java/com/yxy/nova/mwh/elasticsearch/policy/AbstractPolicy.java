package com.yxy.nova.mwh.elasticsearch.policy;

import com.yxy.nova.mwh.elasticsearch.admin.Connection;
import com.yxy.nova.mwh.elasticsearch.admin.ElasticSearchIndexAndAlias;
import com.yxy.nova.mwh.elasticsearch.admin.IndicesCache;
import com.yxy.nova.mwh.elasticsearch.basic.sort.SortMode;
import com.yxy.nova.mwh.elasticsearch.exception.ESExceptionType;
import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import com.yxy.nova.mwh.elasticsearch.util.AscSort;
import com.yxy.nova.mwh.elasticsearch.util.DescSort;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.CollectionUtils;

import java.util.*;


public abstract class AbstractPolicy implements Policy {

    /**
     * 策略
     */
    protected JSONObject policyConfig ;

    /**
     * 集群名
     */
    protected final String clusterName;

    /**
     * 索引信息缓存
     */
    protected IndicesCache indicesCache;

    /**
     * 当前策略包含的索引别名或索引名
     */
    private List<String> indexPrefixes = new ArrayList<>();

    public AbstractPolicy(JSONObject policyConfig, String clusterName) throws ElasticsearchClientException {
        this.policyConfig = policyConfig;
        this.clusterName = clusterName;
    }

    @Override
    public void init(Connection connection) throws ElasticsearchClientException{
        Set<String> indexPrefixes = this.analysis(this.policyConfig);
        if(CollectionUtils.isEmpty(indexPrefixes)){
            throw new ElasticsearchClientException("获取索引信息信息错误",ESExceptionType.POLICY_ERROR);
        }
        this.indexPrefixes.addAll(indexPrefixes);
        //初始化缓存
        this.indicesCache = new IndicesCache(connection.getClient(), this.indexPrefixes);

    }

    @Override
    public ElasticSearchIndexAndAlias getAllAliases() {
        return indicesCache.getAllAliases();
    }

    @Override
    public List<String> getIndexPrefixes(){
        return this.indexPrefixes;
    }

    /**
     * 解析策略
     * @param policyConfig
     * @return 解析出的索引别名或索引名信息
     * @throws ElasticsearchClientException
     */
    protected abstract Set<String> analysis(JSONObject policyConfig) throws ElasticsearchClientException;

    /**
     * 去除不能存在和重复的别名
     * @param aliases
     * @return
     */
    protected List<String> removeNonExistAlias(Set<String> aliases) throws ElasticsearchClientException {
        ElasticSearchIndexAndAlias allAlias = this.getAllAliases();
        if(allAlias != null){
            Map<String, List<String>>  map = allAlias.getAliasToIndex();
            if(map != null && !map.isEmpty()){
                Set<String> set = map.keySet();
                List<String> result = new ArrayList<>();
                for (String alias : aliases) {
                    if (set.contains(alias)) {
                        result.add(alias);
                    }
                }
                return result;
            }
        }
        throw new ElasticsearchClientException("获取服务器的索引别名缓存信息为空", ESExceptionType.CACHE_ERROR);
    }

    /**
     * 别名排序
     *
     * @param indices
     * @param sortMode
     */
    protected void sort(List<String> indices, SortMode sortMode){
        if(sortMode == SortMode.ASC){
            Collections.sort(indices,new AscSort());
        }else {
            Collections.sort(indices,new DescSort());
        }
    }


}
