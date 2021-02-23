package com.yxy.nova.mwh.utils;

/**
 * Created with IntelliJ IDEA.
 *
 * @description:
 * @author: renshui
 * @date: 2020-06-01 12:06 上午
 */
public class ShardingUtil {

    /**
     * 生成分片号
     * @param shardingkey
     * @param totalSharding
     * @return
     */
    public static int getShardingItem(String shardingkey, Integer totalSharding) {
        return (shardingkey.hashCode() & 0x7FFFFFFF) % totalSharding;
    }
}
