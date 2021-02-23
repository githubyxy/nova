package com.yxy.nova.mwh.elasticsearch.util;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 什么是JsonPath？ 比如一个json串： <code>
 *     {
 *         "a": {
 *             "b": {
 *                 "c": "123"
 *             }
 *         }
 *     }
 * </code> 那么"a.b.c"就能表示"123"。 Created by caipeichao on 2015/1/7.
 */
public class JsonPath {

    public static Object getJsonValue(Object json, String key) {
        String[] keys = key.split("\\.");
        for (int j = 0; j < keys.length; j++) {
            if (json == null) return null;
            String k = keys[j];
            if (json instanceof Map) {
                json = ((Map) json).get(k);
            } else {
                return null;
            }
        }
        return json;
    }

    /**
     * 根据key信息从json中获取值
     * @param json
     * @param keys
     * @return
     */
    public static Object getJsonValue(Map<String, Object> json, String[] keys) {
        for (int i = 0; i < keys.length;) {
            Object value = json.get(keys[i]);
            if(++i == keys.length){
                return value;
            }else{
                if (value instanceof Map){
                    json = (Map) value;
                }else{
                    break;
                }
            }
        }
        return null;
    }

    public static void setJsonValue(Map<String, Object> json, String key, Object value) {
        String[] keys = key.split("\\.");
        for (int i = 0; i < keys.length - 1; i++) {
            String k = keys[i];
            if (!json.containsKey(k)) json.put(k, new HashMap());
            json = (Map) json.get(k);
        }
        String lastKey = keys[keys.length - 1];
        if (value == null) {
            json.remove(lastKey);
        } else {
            json.put(lastKey, value);
        }
    }

    /**
     * 消除key中的点号，比如： <code>
     * {
     *     "a.b.c": "123",
     *     "a.b.d": "222"
     * }
     * </code> 转换之后就变成： <code>
     * {
     *     "a": {
     *         "b": {
     *             "c": "123",
     *             "d": "222"
     *         }
     *     }
     * }
     * </code>
     */
    public static JSONObject parsePath(Map<String, Object> list) {
        JSONObject result = new JSONObject(true);
        for (String key : list.keySet()) {
            Object o = list.get(key);
            if (o instanceof Map) {
                o = parsePath((Map) o); // key必须为String类型
            }
            setJsonValue(result, key, o);
        }
        return result;
    }


}

