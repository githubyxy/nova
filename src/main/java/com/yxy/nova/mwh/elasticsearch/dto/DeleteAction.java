package com.yxy.nova.mwh.elasticsearch.dto;

import java.util.Map;

public class DeleteAction {
    private String table;
    private String id;
    private Map<String,Object> param;

    public DeleteAction(String table, String id) {
        this.table = table;
        this.id = id;
    }

    public String getTable() {
        return this.table;
    }

    public String getId() {
        return this.id;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }
}
