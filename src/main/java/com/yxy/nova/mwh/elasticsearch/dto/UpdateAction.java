package com.yxy.nova.mwh.elasticsearch.dto;

import java.util.Map;

public class UpdateAction {

    private String table;
    private String id;
    private Map<String, Object> json;
    private boolean ifNotExists;

    public UpdateAction(String table, String id, Map<String, Object> json) {
        this.table = table;
        this.id = id;
        this.json = json;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getJson() {
        return json;
    }

    public void setJson(Map<String, Object> json) {
        this.json = json;
    }

    public boolean isIfNotExists() {
        return ifNotExists;
    }

    public void setIfNotExists(boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }
}
