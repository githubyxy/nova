package com.yxy.nova.mwh.elasticsearch.policy.vo;

import com.yxy.nova.mwh.elasticsearch.exception.ESExceptionType;
import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import org.apache.commons.lang3.StringUtils;

public class Table {
    private String name;
    public Table(String name) throws ElasticsearchClientException {
        if(StringUtils.isNotBlank(name)){
            this.name = name;
        }else{
            throw new ElasticsearchClientException("表信息配置错误", ESExceptionType.POLICY_ERROR);
        }
    }

    public String getName() {
        return name;
    }
}
