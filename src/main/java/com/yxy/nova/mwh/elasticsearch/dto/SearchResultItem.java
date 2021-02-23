package com.yxy.nova.mwh.elasticsearch.dto;

import com.alibaba.fastjson.JSONObject;

public class SearchResultItem {
    private String id;
    private String table;
    private JSONObject source;

    public SearchResultItem(){

    }

    public SearchResultItem(String table, String id, JSONObject source) {
        this.id = id;
        this.table = table;
        this.source = source;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public void setSource(JSONObject source) {
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public String getTable() {
        return table;
    }

    public JSONObject getSource() {
        return source;
    }


}
