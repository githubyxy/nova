package com.yxy.nova.mwh.utils;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

/**
 * @Author qbq-pc on 16/6/24.
 */
public final class TrimUtil {
    private TrimUtil(){

    }

    /**
     * 对一个对象的字符串类型字段逐个进行trim操作，并将结果应用到当前对象上。对象类型字段进行递归此操作。
     * @param clazz
     * @param argument
     */
    public static void trimStringFieldInObject(Class<?> clazz, Object argument) {
        if (null == clazz || null == argument) {
            return;
        }

        // 基本类型，忽略
        if (ClassUtils.isPrimitiveOrWrapper(clazz)) {
            return;
        }

        if (Object.class.equals(clazz)) {
            return;
        }

        // 集合类，对相应部分进行操作
        if (Collection.class.isAssignableFrom(clazz)) {
            trimCollection(clazz, (Collection) argument);
        } else if (Map.class.isAssignableFrom(clazz)) {
            trimMap(clazz, (Map) argument);
        }

        // 简单Java对象，则对属性进行操作
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (Modifier.isFinal(declaredField.getModifiers()) || Modifier.isStatic(declaredField.getModifiers())) {
                continue;
            }

            if (!Modifier.isPublic(declaredField.getModifiers())) {
                declaredField.setAccessible(true);
            }

            try {
                Object o = declaredField.get(argument);
                if (String.class.equals(declaredField.getType())) {
                    String trimValue = StringUtils.trim((String) o);
                    if("null".equalsIgnoreCase((String)trimValue)){
                        trimValue = null;
                    }
                    declaredField.set(argument, trimValue);
                } else {
                    // 其他类型在对象上面进行trim
                    trimStringFieldInObject(declaredField.getType(), o);
                }
            } catch (IllegalAccessException e) {
                // ignore
            }
        }
        // 递归到父类
        trimStringFieldInObject(clazz.getSuperclass(), argument);
    }

    /*
     * 暂时输入没有集合类型，暂不考虑
     *
     * @param clazz
     * @param arg
     */
    private static void trimMap(Class<?> clazz, Map arg) {

    }

    /*
     * 暂时输入没有集合类型，暂不考虑
     *
     * @param clazz
     * @param arg
     */
    private static void trimCollection(Class<?> clazz, Collection arg) {

    }
}
