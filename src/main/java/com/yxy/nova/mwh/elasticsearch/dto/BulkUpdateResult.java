package com.yxy.nova.mwh.elasticsearch.dto;

import java.util.List;

public class BulkUpdateResult {

    private List<BulkUpdateResultItem> items;

    private long tookInMillis;

    private int failNumber;

    public BulkUpdateResult(List<BulkUpdateResultItem> items) {
        this.items = items;
    }
    public BulkUpdateResult(List<BulkUpdateResultItem> items, int failNumber, long tookInMillis) {
        this.items = items;
        this.failNumber = failNumber;
        this.tookInMillis = tookInMillis;
    }

    public List<BulkUpdateResultItem> getItems() {
        return items;
    }

    public void setItems(List<BulkUpdateResultItem> items) {
        this.items = items;
    }

    public int getFailNumber() {
        return failNumber;
    }

    public long getTookInMillis() {
        return tookInMillis;
    }

    public void setFailNumber(int failNumber) {
        this.failNumber = failNumber;
    }
}
