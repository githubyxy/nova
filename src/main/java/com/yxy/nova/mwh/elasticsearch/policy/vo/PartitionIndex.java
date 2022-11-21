package com.yxy.nova.mwh.elasticsearch.policy.vo;

import com.yxy.nova.mwh.elasticsearch.policy.partition.PartitionPolicy;
import com.yxy.nova.mwh.elasticsearch.policy.route.RouteShip;

/**
 * 分区索引信息
 */
public class PartitionIndex {

    /**
     * 分区策略
     */
    private PartitionPolicy partitionPolicy;

    private Table defaultTable;

    private RouteShip<Table> routeShip;

    public PartitionIndex(PartitionPolicy partitionPolicy, Table defaultTable, RouteShip<Table> routeShip) {
        this.partitionPolicy = partitionPolicy;
        this.defaultTable = defaultTable;
        this.routeShip = routeShip;
    }

    public PartitionPolicy getPartitionPolicy() {
        return partitionPolicy;
    }

    public Table getDefaultTable() {
        return defaultTable;
    }

    public RouteShip<Table> getRouteShip() {
        return routeShip;
    }
}