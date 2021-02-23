package com.yxy.nova.mwh.utils;

import org.springframework.cglib.beans.BeanCopier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * getter/setter性能最好
 * spring包中的 BeanUtil 采用反射实现
 * cglib包中的  Beancopier   采用动态字节码实现，性能近乎getter/setter，但是BeanCopier的创建时消耗较大
 */
public class BeanUtil {

    private static final Map<String, BeanCopier> BEAN_COPIERS = new ConcurrentHashMap<>(32);

    public static void copyProperties(Object source, Object target) {
        if(null == source || null == target) {
            return;
        }

        String key = source.getClass().getSimpleName()+ "." + target.getClass().getSimpleName();

        BeanCopier copier = BEAN_COPIERS.get(key);
        if(null == copier) {
            synchronized (BEAN_COPIERS) {
                copier = BEAN_COPIERS.get(key);
                if(null != copier) {
                    copier = BeanCopier.create(source.getClass(), target.getClass(), false);
                    BEAN_COPIERS.put(key, copier);
                }
            }
        }

        copier.copy(source, target, null);
    }
}
