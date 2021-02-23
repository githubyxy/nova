package com.yxy.nova.mwh.elasticsearch.dto;

public class DeleteResult {
    private String table;
    private String id;
    private boolean error;

    public DeleteResult(String table, String id, boolean error) {
        this.table = table;
        this.id = id;
        this.error = error;
    }

    public String getTable() {
        return this.table;
    }

    public String getId() {
        return this.id;
    }

    public boolean getError() {
        return error;
    }
}
