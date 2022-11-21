package com.yxy.nova.mwh.utils;

/**
 * Created with IntelliJ IDEA.
 *
 * @description:
 * @author: renshui
 * @date: 2020-06-02 9:13 下午
 */
public class HashUtil {

    /**
     * 返回long类型的哈希值
     * @param string
     * @return
     */
    public static long longHash(String string) {
        long h = 1125899906842597L;
        int len = string.length();

        for (int i = 0; i < len; i++) {
            h = 31 * h + string.charAt(i);
        }
        return h;
    }

    /**
     * 返回long类型的 正数哈希值
     * @param string
     * @return
     */
    public static long positiveLongHash(String string) {
        return longHash(string) & Long.MAX_VALUE;
    }

}
