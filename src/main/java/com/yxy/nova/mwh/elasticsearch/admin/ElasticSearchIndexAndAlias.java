package com.yxy.nova.mwh.elasticsearch.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 索引别名信息
 * @author quyuanwen
 */
public class ElasticSearchIndexAndAlias {

    private List<String> aliasList = new ArrayList<>();
    private Map<String, List<String>> aliasMapIndices = new HashMap<>();

    public ElasticSearchIndexAndAlias( List<String> aliasList, Map<String, List<String>> aliasMapIndices) {
        this.aliasList = aliasList;
        this.aliasMapIndices = aliasMapIndices;
    }

    /**
     * 根据别名获取索引
     * @param alias
     * @return
     */
    public List<String> getIndicesByAlias(String alias) {
        List<String> x = aliasMapIndices.get(alias);
        if (x == null) return new ArrayList<>();
        return x;
    }

    /**
     * 获取别名对应的索引映射
     * @return
     */
    public Map<String, List<String>> getAliasToIndex() {
        return aliasMapIndices;
    }

    /**
     * 得到按降序排序的别名
     *
     * @return
     */
    public List<String> getSortAliases(){
        return aliasList;
    }
}
