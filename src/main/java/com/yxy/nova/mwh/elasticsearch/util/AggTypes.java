package com.yxy.nova.mwh.elasticsearch.util;

import com.yxy.nova.mwh.elasticsearch.util.enumerate.AggType;

import java.util.HashMap;
import java.util.Map;

public  class AggTypes {

    private Map<String, AggType> types = new HashMap<>();
    private Map<String, AggTypes> subs = new HashMap<>();

    public void put(String name, AggType type, AggTypes subAggTypes) {
        types.put(name, type);
        subs.put(name, subAggTypes);
    }

    public AggType getAggType(String name) {
        return this.types.get(name);
    }

    public AggTypes getSubAggTypes(String name) {
        return this.subs.get(name);
    }

}