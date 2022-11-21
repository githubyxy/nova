package com.yxy.nova.mwh.elasticsearch.dto;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SearchRequest {

    private Long                from;
    private Long                size;
    private String              index;
    private String              type;
    private Map<String, String> queryString = new HashMap<>();
    private JSONObject body        = new JSONObject();

    public SearchRequest(){

    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getQueryString() {
        return queryString;
    }

    public void putQueryString(String key, String value) {
        queryString.put(key, value);
    }

    public void putBody(String key, Object value) {
        body.put(key, value);
    }

    public void putBody(Map<String, Object> bodyPart) {
        body.putAll(bodyPart);
    }

    public JSONObject getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "SearchRequest{" + "from=" + from + ", size=" + size + ", index='" + index + '\'' + ", type='" + type + '\'' + ", body=" + body + '}';
    }
}
