package com.yxy.nova.mwh.elasticsearch.basic.agg.bucket;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 字段值分桶
 * @author quyuanwen
 * @version 2020-02-28
 */
public class Terms extends Bucket{

    private int size;

    private String missing;

    private List<Object> includes;

    private List<Object> excludes;

    public Terms(String name, String field) {
        super(name, field);
    }

    public Terms(String name, String field, int size) {
        super(name, field);
        this.size = size;
    }

    @Override
    public JSONObject assemble(String field) {
        JSONObject content = new JSONObject();
        JSONObject terms = new JSONObject();
        terms.put("field", field);
        if(this.size > 0){
            terms.put("size", this.size);
        }
        if(!StringUtils.isEmpty(this.missing)){
            terms.put("missing", this.missing);
        }
        if(!CollectionUtils.isEmpty(this.includes)){
            terms.put("include", new JSONArray(this.includes));
        }
        if(!CollectionUtils.isEmpty(this.excludes)){
            terms.put("exclude", new JSONArray(this.excludes));
        }
        content.put("terms", terms);
        return content;
    }

    public Terms size(int size) {
        this.size = size;
        return this;
    }

    public Terms missing(String missing) {
        this.missing = missing;
        return this;
    }


    public Terms include(Object value) {
        if(CollectionUtils.isEmpty(this.includes)){
            this.includes = new ArrayList<>();
        }
        includes.add(value);
        return this;
    }

    public Terms exclude(Object value) {
        if(CollectionUtils.isEmpty(this.excludes)){
            this.excludes = new ArrayList<>();
        }
        this.excludes.add(value);
        return this;
    }
}
