package com.yxy.nova.mwh.elasticsearch.dto;

import java.util.Map;

public class FetchResult {

    private String              id;
    private String              type;
    private Map<String, Object> source;

    public FetchResult(String type, String id, Map<String, Object> source){
        this.type = type;
        this.id = id;
        this.source = source;
    }

    public String getType() {
        return this.type;
    }

    public String getId() {
        return id;
    }

    public Map<String, Object> getSource() {
        return source;
    }
}
