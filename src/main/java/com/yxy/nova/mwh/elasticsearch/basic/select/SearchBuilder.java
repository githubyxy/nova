package com.yxy.nova.mwh.elasticsearch.basic.select;

import com.yxy.nova.mwh.elasticsearch.SearchService;
import com.yxy.nova.mwh.elasticsearch.basic.sort.SortBy;
import com.yxy.nova.mwh.elasticsearch.basic.sort.SortMode;
import com.yxy.nova.mwh.elasticsearch.basic.where.*;
import com.yxy.nova.mwh.elasticsearch.dto.SearchResult;
import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import org.elasticsearch.action.search.SearchResponse;

import java.util.*;

/**
 * 查询语句
 */
public class SearchBuilder implements WhereBuilder<SearchBuilder> {

    protected List<WhereCondition> conditions = new LinkedList<>();
    protected SearchService service;
    protected String table;

    public SearchBuilder(SearchService service, String table) {
        this.service = service;
        this.table = table;
    }

    public SearchBuilder whereEquals(String key, Object value) {
        conditions.add(new WhereEquals(key, value));
        return this;
    }

    public SearchBuilder whereLike(String key, String value) {
        conditions.add(new WhereLike(key, value));
        return this;
    }

    public SearchBuilder wildcardWhereLike(String key, String value){
        conditions.add(new WildcardWhereLike(key, value));
        return this;
    }


    public SearchBuilder multiWhereLike(String query, String... fields){
        conditions.add(new MultiWhereLike(query, fields));
        return this;
    }

    public SearchBuilder whereNotEquals(String key, Object value) {
        conditions.add(new WhereNotEquals(key, value));
        return this;
    }

    public SearchBuilder whereStartsWith(String key, Object value) {
        conditions.add(new WhereStartsWith(key, value));
        return this;
    }

    public SearchBuilder whereGreater(String key, Object value) {
        conditions.add(new WhereGreater(key, value));
        return this;
    }

    public <T> SearchBuilder whereIn(String key, Collection<T> values) {
        if (values == null) {
            values = new ArrayList<>();
        }
        conditions.add(new WhereIn<>(key, values));
        return this;
    }

    public <T> SearchBuilder whereNotIn(String key, Collection<T> values) {
        if (values == null) {
            values = new ArrayList<>();
        }
        conditions.add(new WhereNotIn<>(key, values));
        return this;
    }

    public SearchBuilder whereGreaterOrEqual(String key, Object value) {
        conditions.add(new WhereGreaterOrEqual(key, value));
        return this;
    }

    public SearchBuilder whereLess(String key, Object value) {
        conditions.add(new WhereLess(key, value));
        return this;
    }

    public SearchBuilder whereLessOrEqual(String key, Object value) {
        conditions.add(new WhereLessOrEqual(key, value));
        return this;
    }

    public SearchBuilder whereContains(String key, Object value) {
        conditions.add(new WhereContains(key, value));
        return this;
    }

    public SearchBuilder whereNull(String key) {
        conditions.add(new WhereIsNull(key));
        return this;
    }

    public SearchBuilder whereNotNull(String key) {
        conditions.add(new WhereNotIsNull(key));
        return this;
    }

    public SearchBuilder limit(int from, int size) {
        conditions.add(new Limit(from, size));
        return this;
    }

    public SearchBuilder sortBy(String field, SortMode sortMode) {
        conditions.add(new SortBy(field, sortMode));
        return this;
    }

    /**
     * 默认升序
     */
    public SearchBuilder sortBy(String field) {
        return this.sortBy(field, SortMode.ASC);
    }

    public List<Map<String, Object>> list() throws ElasticsearchClientException {
        return service.list(table, conditions);
    }

    public SearchResult get() throws ElasticsearchClientException  {
        return service.get(table, conditions);
    }

    public Iterator<Map<String, Object>> iterate(int fetchSize) throws ElasticsearchClientException{
        return service.iterate(table, conditions, fetchSize);
    }

    public SearchResponse response() throws ElasticsearchClientException {
        return service.response(table, conditions);
    }

    public List<WhereCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<WhereCondition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public GroupWhere whereOr() {
        GroupWhere result = GroupWhere.newOr();
        conditions.add(result);
        return result;
    }

    @Override
    public GroupWhere whereAnd() {
        GroupWhere result = GroupWhere.newAnd();
        conditions.add(result);
        return result;
    }
}
