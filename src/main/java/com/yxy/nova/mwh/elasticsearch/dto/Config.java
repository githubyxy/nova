package com.yxy.nova.mwh.elasticsearch.dto;

import com.alibaba.fastjson.JSONObject;

/**
 * 集群配置信息
 */
public class Config {
    private String clusterName;
    private String hostList;
    private String username;
    private String password;
    private long searchLimit;
    private int tpsLimit;
    private JSONObject policy;

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getHostList() {
        return hostList;
    }

    public void setHostList(String hostList) {
        this.hostList = hostList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getSearchLimit() {
        return searchLimit;
    }

    public void setSearchLimit(long searchLimit) {
        this.searchLimit = searchLimit;
    }

    public int getTpsLimit() {
        return tpsLimit;
    }

    public void setTpsLimit(int tpsLimit) {
        this.tpsLimit = tpsLimit;
    }

    public JSONObject getPolicy() {
        return policy;
    }

    public void setPolicy(JSONObject policy) {
        this.policy = policy;
    }

    @Override
    public String toString() {
        return "Config{" + "clusterName='" + clusterName + '\'' + ", hostList='" + hostList + '\'' + ", username='" + username + '\'' + ", password='" + password + '\'' + ", searchLimit=" + searchLimit + ", tpsLimit=" + tpsLimit + ", policy=" + policy + '}';
    }
}
