package com.yxy.nova.mwh.elasticsearch.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FetchAction {
    private String id;
    private String type;
    private Map<String,Object> param;

    public FetchAction(String type, String id, Date time) {
        this.type = type;
        this.id = id;
        this.param = new HashMap<>();
        this.param.put("defaultPartition",time);
    }

    /**
     * param 为路由和分片信息，key为扁平化的
     * @param id
     * @param type
     * @param param
     */
    public FetchAction(String id, String type, Map<String,Object> param) {
        this.id = id;
        this.type = type;
        this.param = param;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Map<String, Object> getParam() {
        return param;
    }
}
