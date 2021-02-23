package com.yxy.nova.mwh.elasticsearch.dto;

import java.util.List;

public class SearchResult {
    private boolean success;
    private long total;
    private List<SearchResultItem> items;

    private SearchResult() {
    }

    public static SearchResult failure() {
        SearchResult result = new SearchResult();
        result.success = false;
        return result;
    }

    public static SearchResult success(long total, List<SearchResultItem> items) {
        SearchResult result = new SearchResult();
        result.success = true;
        result.items = items;
        result.total = total;
        return result;
    }

    public boolean getSuccess() {
        return success;
    }

    public long getTotal() {
        return total;
    }

    public List<SearchResultItem> getItems() {
        return items;
    }
}
