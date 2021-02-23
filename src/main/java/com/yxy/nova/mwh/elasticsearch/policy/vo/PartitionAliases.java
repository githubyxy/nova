package com.yxy.nova.mwh.elasticsearch.policy.vo;

import com.yxy.nova.mwh.elasticsearch.basic.sort.SortMode;
import com.yxy.nova.mwh.elasticsearch.policy.partition.PartitionPolicy;

import java.util.List;

/**
 * 分区索引信息
 *
 * @author quyuanwen
 */
public class PartitionAliases {


    private List<String> readAliases;

    private String writeAlias ;

    private SortMode sortMode;

    private PartitionPolicy partitionPolicy;

    public PartitionAliases(List<String> readAliases, PartitionPolicy partitionPolicy, SortMode sortMode) {
        this.readAliases = readAliases;
        this.partitionPolicy = partitionPolicy;
        this.sortMode = sortMode;
    }

    public PartitionAliases(String writeAlias, PartitionPolicy partitionPolicy, SortMode sortMode) {
        this.writeAlias = writeAlias;
        this.partitionPolicy = partitionPolicy;
        this.sortMode = sortMode;
    }

    public List<String> getReadAliases() {
        return readAliases;
    }

    public String getWriteAlias() {
        return writeAlias;
    }

    public SortMode getSortMode() {
        return sortMode;
    }

    public PartitionPolicy getPartitionPolicy() {
        return partitionPolicy;
    }
}
