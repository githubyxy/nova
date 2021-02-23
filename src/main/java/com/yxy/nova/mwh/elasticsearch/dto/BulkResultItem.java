package com.yxy.nova.mwh.elasticsearch.dto;

public class BulkResultItem {
    private final String table;
    private final String id;
    private final boolean success;
    private final boolean shouldRetry;
    private final String failureMessage;
    private final boolean versionConflict;

    public BulkResultItem(String table, String id, boolean success, boolean shouldRetry, String failureMessage, boolean versionConflict) {
        this.table = table;
        this.id = id;
        this.success = success;
        this.shouldRetry = shouldRetry;
        this.failureMessage = failureMessage;
        this.versionConflict = versionConflict;
    }

    public boolean isShouldRetry() {
        return shouldRetry;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getId() {
        return id;
    }

    public String getTable() {
        return table;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public boolean isVersionConflict() {
        return versionConflict;
    }
}
