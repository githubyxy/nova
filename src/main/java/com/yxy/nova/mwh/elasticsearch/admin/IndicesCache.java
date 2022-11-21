package com.yxy.nova.mwh.elasticsearch.admin;

import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import com.yxy.nova.mwh.elasticsearch.facade.org.elasticsearch.client.Response;
import com.yxy.nova.mwh.elasticsearch.facade.org.elasticsearch.client.RestClient;
import com.yxy.nova.mwh.elasticsearch.util.AssistantUtil;
import com.yxy.nova.mwh.elasticsearch.util.DescSort;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 索引信息缓存
 * @author quyuanwen
 */
public class IndicesCache {

    private LoadingCache<String, ElasticSearchIndexAndAlias> aliasCache;

    private RestClient client;
    //索引别名
    private List<String> indexPrefixes;

    private final static String aliasesPath = "/_cat/aliases/%s?format=json";
    private final static String indicesPath = "/_cat/indices/%s?format=json";

    public IndicesCache(RestClient client, List<String> indexPrefixes){
        this.client = client;
        this.indexPrefixes = indexPrefixes;
        init();
    }

    public void init() {
        CacheLoader<String, ElasticSearchIndexAndAlias> loader = new AliasLoader();
        loader = CacheLoader.asyncReloading(loader,
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
        this.aliasCache = CacheBuilder.newBuilder().refreshAfterWrite(1, TimeUnit.HOURS).build(loader);
        getAllAliases();
    }

    public Date parseDate(String s) {
        try {
            return new SimpleDateFormat("yyyyMMdd").parse(s);
        } catch (ParseException e) {
            return null;
        }
    }

    public ElasticSearchIndexAndAlias getAllAliases() {
        if (!isConnected()) return empty();
        try {
            return this.aliasCache.get("default");
        } catch (ExecutionException e) {
            return null;
        }
    }

    public void clearCache() {
        this.aliasCache.invalidateAll();
    }

    private boolean isConnected() {
        if (this.client == null) return false;
        return true;
    }

    private ElasticSearchIndexAndAlias empty() {
        return new ElasticSearchIndexAndAlias(new ArrayList<String>(), new HashMap<String, List<String>>());
    }

    /**
     * 获取全部别名和索引
     */
    private ElasticSearchIndexAndAlias getAllAliasesNoCache() throws ElasticsearchClientException {

        List<String> aliasList = new ArrayList<>();
        Map<String, List<String>> aliasMapIndices = new HashMap<>();

        // 如果没有连接，就返回空的吧
        if (this.client == null) {
            return new ElasticSearchIndexAndAlias(aliasList, aliasMapIndices);
        }

        // 获取索引列表和别名列表
        this.analysisAliases(aliasList, aliasMapIndices);

        return new ElasticSearchIndexAndAlias(aliasList, aliasMapIndices);
    }

    /**
     * 分析当前集群情况
     *
     * @param aliasList
     * @param aliasMapIndices
     * @return
     * @throws ElasticsearchClientException
     */
    private boolean analysisAliases(List<String> aliasList, Map<String, List<String>> aliasMapIndices) throws ElasticsearchClientException {
        boolean found = false;
        //获取别名对应索引信息
        for(String indexPrefix : indexPrefixes){
            String path = String.format(aliasesPath, indexPrefix + "*");
            Response response = AssistantUtil.performRequest(this.client, "GET", path);
            AssistantUtil.checkStatusCode(response);
            JSONArray jsonArray = AssistantUtil.parseResponse2Array(response);
            if(!CollectionUtils.isEmpty(jsonArray)){
                found = true;
                for(int i = 0; i < jsonArray.size(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String alias = jsonObject.getString("alias");
                    String index = jsonObject.getString("index");
                    List<String> indices = aliasMapIndices.get(alias);
                    if(indices == null){
                        indices = new ArrayList<>();
                        aliasMapIndices.put(alias, indices);
                        indices.add(index);
                    }else{
                        if(!indices.contains(index)){
                            indices.add(index);
                        }
                    }
                    aliasList.add(alias);
                }
            }
        }
        //如果别名信息获取不到，则获取索引信息
        if(CollectionUtils.isEmpty(aliasList)){
            for(String indexPrefix : indexPrefixes){
                String path = String.format(indicesPath, indexPrefix + "*");
                Response response = AssistantUtil.performRequest(this.client, "GET", path);
                AssistantUtil.checkStatusCode(response);
                JSONArray jsonArray = AssistantUtil.parseResponse2Array(response);
                if(!CollectionUtils.isEmpty(jsonArray)){
                    found = true;
                    for(int i = 0; i < jsonArray.size(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String index = jsonObject.getString("index");
                        String status = jsonObject.getString("status");
                        if(!StringUtils.isEmpty(status) && "open".equals(status)){
                            aliasMapIndices.put(index, Arrays.asList(index));
                            aliasList.add(index);
                        }
                    }
                }
            }
        }
        //降序排列
        Collections.sort(aliasList,new DescSort());

        return found;
    }

    private class AliasLoader extends CacheLoader<String, ElasticSearchIndexAndAlias> {

        @Override
        public ElasticSearchIndexAndAlias load(String key) throws Exception {
            // 载入
            return getAllAliasesNoCache();
        }
    }
}
