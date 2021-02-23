package com.yxy.nova.mwh.utils;

import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @description: UUID生成器
 * @author: shui.ren
 * @date: 2018-04-26 下午5:41
 */
public class UUIDGenerator {

    /**
     * 生成新的uuid
     * @return
     */
    public static String generate() {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        return uuid;
    }
}
