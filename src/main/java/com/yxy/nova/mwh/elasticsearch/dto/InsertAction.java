package com.yxy.nova.mwh.elasticsearch.dto;

import java.util.Map;

public class InsertAction {

    private String table;
    private String id;
    private Map<String, Object> json;
    private boolean ifNotExists;

    public InsertAction() {
    }

    public InsertAction(String table, String id, Map<String, Object> json) {
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

    public void setIfNotExists(boolean value) {
        this.ifNotExists = value;
    }

    public boolean getIfNotExists() {
        return ifNotExists;
    }
}
