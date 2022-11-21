package com.yxy.nova.mwh.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhaoyao on 16/10/27.
 */
public class JSONUtils {

    /**
     * 将json拍平,可以传入JSONObject\JSONArray\基本类型
     * 
     * @param val
     * @return
     */
    public static String jsonFlattener(Object val) {
        if (null == val) {
            return null;
        }
        String flatStr = flattener(null, val);
        return "{" + flatStr + "}";
    }

    /**
     * 将json拍平,可以传入JSONObject\JSONArray\基本类型
     * 
     * @param parent
     * @param val
     * @return
     */
    public static String jsonFlattener(String parent, Object val) {
        if (null == val) {
            return null;
        }
        String flatStr = flattener(parent, val);
        return "{" + flatStr + "}";
    }

    /**
     * 将json拍平
     *
     * @param parent
     * @param val
     * @return
     */
    private static String flattener(String parent, Object val) {
        StringBuilder sb = new StringBuilder();
        if (val instanceof JSONObject) {
            JSONObject jo = (JSONObject) val;
            Set<String> keySet = jo.keySet();
            for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
                String key = i.next();
                String hkey = (parent == null) ? key : parent + "." + key;
                Object jval = filterNullString(jo.get(key));
                String json = flattener(hkey, jval);
                sb.append(json);
                if (i.hasNext()) {
                    sb.append(",");
                }
            }
        } else if (val instanceof JSONArray) {
            JSONArray ja = (JSONArray) val;
            for (int i = 0; i < ja.size(); i++) {
                String hkey = (parent == null) ? "" + i : parent + "[" + i + "]";
                Object aval = filterNullString(ja.get(i));
                String json = flattener(hkey, aval);
                sb.append(json);
                if (i < ja.size() - 1) {
                    sb.append(",");
                }
            }
        } else if (val instanceof String) {
            sb.append("\"").append(parent).append("\"").append(":");
            String s = (String) val;
            sb.append("\"");
            sb.append(s);
            sb.append("\"");
        } else if (val instanceof Integer) {
            sb.append("\"").append(parent).append("\"").append(":");
            Integer integer = (Integer) val;
            sb.append(integer);
        } else if (val instanceof Double) {
            sb.append("\"").append(parent).append("\"").append(":");
            Double d = (Double) val;
            sb.append(d);
        } else if (val instanceof Long) {
            sb.append("\"").append(parent).append("\"").append(":");
            Long l = (Long) val;
            sb.append(l);
        } else if (val instanceof Boolean) {
            sb.append("\"").append(parent).append("\"").append(":");
            Boolean b = (Boolean) val;
            sb.append(b);
        } else if (val == null) {
            sb.append("\"").append(parent).append("\"").append(":");
            sb.append(val);
        }
        return sb.toString();
    }

    public static Map<String, Object> flattenerAsMap(Object val) {
        return flattenerAsMap(null, val);
    }

    /**
     * 将json拍平,输出map结构,key为拍平的路径,val为值.val为 将值为null或null字符串过滤
     * 
     * @param parent
     * @param val
     * @return
     */
    public static Map<String, Object> flattenerAsMap(String parent, Object val) {
        if (null == val) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        if (val instanceof JSONObject) {
            JSONObject jo = (JSONObject) val;
            Set<String> keySet = jo.keySet();
            for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
                String key = i.next();
                String hkey = (parent == null) ? key : parent + "." + key;
                Object jval = filterNullString(jo.get(key));
                Map<String, Object> jMap = flattenerAsMap(hkey, jval);
                if (null != jMap) {
                    map.putAll(jMap);
                }
            }
        } else if (val instanceof JSONArray) {
            JSONArray ja = (JSONArray) val;
            int j = 0;
            for (int i = 0; i < ja.size(); i++) {
                Object aval = filterNullString(ja.get(i));
                // map中存的key,如果遇到null或null字符串的情况,当其为空处理,后面正常的字符的下标不应受到当空处理的字段的影响
                String hkey = (parent == null) ? "" + j : parent + "[" + j + "]";
                Map<String, Object> aMap = flattenerAsMap(hkey, aval);
                if (null != aMap) {
                    map.putAll(aMap);
                    j++;
                }
            }
        } else if (val instanceof String) {
            String s = (String) val;
            map.put(parent, s);
        } else if (val instanceof Integer) {
            Integer integer = (Integer) val;
            map.put(parent, integer);
        } else if (val instanceof Double) {
            Double d = (Double) val;
            map.put(parent, d);
        } else if (val instanceof Long) {
            Long l = (Long) val;
            map.put(parent, l);
        } else if (val instanceof Boolean) {
            Boolean b = (Boolean) val;
            map.put(parent, b);
        } else if (null != val) {
            map.put(parent, val);
        }
        return map;
    }

    /**
     * 过滤null字符串
     *
     * @param val
     * @return
     */
    private static Object filterNullString(Object val) {
        String valStr = String.valueOf(val);
        if (StringUtils.isBlank(valStr) || StringUtils.equalsIgnoreCase(valStr, "null")) {
            return null;
        }
        return val;
    }

    /**
     * 构造字段的路径
     *
     * @param basePath
     * @param name
     * @return
     */
    public static String makePath(String basePath, String name) {
        if (basePath == null) {
            return name;
        }
        return basePath + '.' + name;
    }

    /**
     * 构数组行造字段路径
     *
     * @param basePath
     * @param name
     * @param arrayIndex
     * @return
     */
    public static String makePath(String basePath, String name, int arrayIndex) {
        if (basePath == null) {
            return name + '[' + arrayIndex + ']';
        }
        return basePath + '.' + name + '[' + arrayIndex + ']';
    }

    /**
     * 构数组行造字段路径
     *
     * @param basePath
     * @param name
     * @param arrayIndex
     * @return
     */
    public static String makePathOther(String basePath, String name, int arrayIndex) {
        if (basePath == null) {
            return name;
        }
        return basePath + '.' + name + '[' + arrayIndex + ']';
    }

    /**
     * 构造数组索引的路径
     *
     * @param basePath
     * @param arrayIndex
     * @return
     */
    public static String makePath(String basePath, int arrayIndex) {
        if (basePath == null) {
            return "[" + arrayIndex + ']';
        }
        return basePath + '[' + arrayIndex + ']';
    }

    public static Map<String, String> json2Map(JSONObject json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        Set<String> keyset = json.keySet();
        Map<String, String> result = new HashMap<>();
        for (String key : keyset) {
            Object value = json.get(key);
            if (value instanceof JSON) {
                JSON jsonV = (JSON) value;
                result.put(key, jsonV.toJSONString());
            } else {
                result.put(key, String.valueOf(value));
            }
        }
        return result;
    }
}
