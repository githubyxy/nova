package com.yxy.nova.mwh.elasticsearch.basic.where;

import com.yxy.nova.mwh.elasticsearch.basic.select.WhereBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by caipeichao on 15/6/23.
 */
public class GroupWhere implements WhereCondition<List<WhereCondition>>, WhereBuilder<GroupWhere> {

    public static final int      OR         = 1;
    public static final int      AND        = 2;

    private int                  logic;
    private List<WhereCondition> conditions = new ArrayList<>();

    private GroupWhere(int logic){
        assert logic == OR || logic == AND;
        this.logic = logic;
    }

    public static GroupWhere newOr() {
        return new GroupWhere(OR);
    }

    public static GroupWhere newAnd() {
        return new GroupWhere(AND);
    }

    public boolean isLogicOr() {
        return logic == OR;
    }

    public boolean isLogicAnd() {
        return logic == AND;
    }

    public GroupWhere whereEquals(String key, Object value) {
        conditions.add(new WhereEquals(key, value));
        return this;
    }

    public GroupWhere whereNotEquals(String key, Object value) {
        conditions.add(new WhereNotEquals(key, value));
        return this;
    }

    public GroupWhere whereStartsWith(String key, Object value) {
        conditions.add(new WhereStartsWith(key, value));
        return this;
    }

    public GroupWhere whereGreater(String key, Object value) {
        conditions.add(new WhereGreater(key, value));
        return this;
    }

    public <T> GroupWhere whereIn(String key, Collection<T> values) {
        conditions.add(new WhereIn<>(key, values));
        return this;
    }

    public <T> GroupWhere whereNotIn(String key, Collection<T> values) {
        conditions.add(new WhereNotIn<>(key, values));
        return this;
    }

    public GroupWhere whereGreaterOrEqual(String key, Object value) {
        conditions.add(new WhereGreaterOrEqual(key, value));
        return this;
    }

    public GroupWhere whereLess(String key, Object value) {
        conditions.add(new WhereLess(key, value));
        return this;
    }

    public GroupWhere whereLessOrEqual(String key, Object value) {
        conditions.add(new WhereLessOrEqual(key, value));
        return this;
    }

    public GroupWhere whereContains(String key, Object value) {
        conditions.add(new WhereContains(key, value));
        return this;
    }

    public GroupWhere whereNull(String key) {
        conditions.add(new WhereIsNull(key));
        return this;
    }

    public GroupWhere whereNotNull(String key) {
        conditions.add(new WhereNotIsNull(key));
        return this;
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

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public List<WhereCondition> getValue() {
        return conditions;
    }
}
