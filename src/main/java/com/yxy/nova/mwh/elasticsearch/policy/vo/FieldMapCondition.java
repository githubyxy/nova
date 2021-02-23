package com.yxy.nova.mwh.elasticsearch.policy.vo;

import com.yxy.nova.mwh.elasticsearch.basic.where.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 字段对应的条件信息
 * @author quyuanwen
 */
public class FieldMapCondition {

    Map<String,WhereGreater> greaterMap ;
    Map<String,WhereGreaterOrEqual> greaterOrEqualMap ;
    Map<String,WhereLess> lessMap ;
    Map<String,WhereLessOrEqual> lessOrEqualMap ;
    Map<String,WhereEquals> equalsMap ;
    Map<String,WhereIn> inMap;

    Map<String,Object> ruteValueMap;

    public WhereGreater getGreater(String field) {
        if(greaterMap != null){
            return greaterMap.get(field);
        }
        return null;
    }

    public void putGreaterMap(String field, WhereGreater greater) {
        if(this.greaterMap == null){
            this.greaterMap = new HashMap<>();
        }
        this.greaterMap.put(field,greater);
    }

    public WhereGreaterOrEqual getGreaterOrEqualMap(String field) {
        if (greaterOrEqualMap != null) {
            return greaterOrEqualMap.get(field);
        }
        return null;
    }

    public void putGreaterOrEqualMap(String field, WhereGreaterOrEqual greaterOrEqual) {
        if(this.greaterOrEqualMap == null){
            this.greaterOrEqualMap = new HashMap<>();
        }
        this.greaterOrEqualMap.put(field,greaterOrEqual);
    }

    public WhereLess getLessMap(String field) {
        if (lessMap != null) {
            return lessMap.get(field);
        }
        return null;
    }

    public void putLessMap(String field, WhereLess less) {
        if(this.lessMap == null){
            this.lessMap = new HashMap<>();
        }
        this.lessMap.put(field,less);
    }

    public WhereLessOrEqual getLessOrEqualMap(String field) {
        if (lessOrEqualMap != null) {
            return lessOrEqualMap.get(field);
        }
        return null;
    }

    public void putLessOrEqualMap(String field, WhereLessOrEqual lessOrEqual) {
        if(this.lessOrEqualMap == null){
            this.lessOrEqualMap = new HashMap<>();
        }
        this.lessOrEqualMap.put(field,lessOrEqual);
    }

    public WhereEquals getEqualsMap(String field) {
        if (equalsMap != null) {
            return equalsMap.get(field);
        }
        return null;
    }

    public void putEqualsMap(String field, WhereEquals equals) {
        if(this.equalsMap == null){
            this.equalsMap = new HashMap<>();
            this.ruteValueMap = new HashMap<>();
        }
        this.equalsMap.put(field, equals);

        this.ruteValueMap.put(field, equals.getValue());
    }

    public WhereIn getInMap(String field) {
        if (inMap != null) {
            return inMap.get(field);
        }
        return null;
    }

    public void putInMap(String field, WhereIn in) {
        if(this.inMap == null){
            this.inMap = new HashMap<>();
        }
        this.inMap.put(field,in);
    }

    public Map<String, Object> getRuteValueMap() {
        return ruteValueMap;
    }
}
