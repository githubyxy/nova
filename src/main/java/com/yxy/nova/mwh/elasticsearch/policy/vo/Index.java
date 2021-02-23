package com.yxy.nova.mwh.elasticsearch.policy.vo;

import com.yxy.nova.mwh.elasticsearch.policy.route.RouteShip;

public class Index {

    private String readIndex ;

    private String writeIndex ;

    private Table defaultTbale;

    private RouteShip<Table> routeShip;

    public String getReadIndex() {
        return readIndex;
    }

    public void setReadIndex(String readIndex) {
        this.readIndex = readIndex;
    }

    public String getWriteIndex() {
        return writeIndex;
    }

    public void setWriteIndex(String writeIndex) {
        this.writeIndex = writeIndex;
    }

    public Table getDefaultTbale() {
        return defaultTbale;
    }

    public void setDefaultTbale(Table defaultTbale) {
        this.defaultTbale = defaultTbale;
    }

    public RouteShip<Table>  getRouteShip() {
        return routeShip;
    }

    public void setRouteShip(RouteShip<Table> routeShip) {
        this.routeShip = routeShip;
    }
}
