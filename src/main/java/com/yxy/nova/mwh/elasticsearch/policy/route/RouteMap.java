package com.yxy.nova.mwh.elasticsearch.policy.route;

import com.yxy.nova.mwh.elasticsearch.exception.ESExceptionType;
import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

public class RouteMap {

    private String[] splitField;

    private String field;

    private Set<String> valueSet = new HashSet<>();

    public RouteMap(String field, JSONArray values) throws ElasticsearchClientException {
        if(StringUtils.isNotBlank(field) && !CollectionUtils.isEmpty(values)){
            this.field = field;
            this.splitField = field.split("\\.");
            for(int i = 0; i < values.size(); i++) {
                valueSet.add(values.getString(i));
            }
        }else{
            throw new ElasticsearchClientException("路由映射信息配置错误", ESExceptionType.POLICY_ERROR);
        }
    }

    public String[] getSplitField() {
        return splitField;
    }

    public String getField() {
        return field;
    }

    public Set<String> getValueSet() {
        return valueSet;
    }
}