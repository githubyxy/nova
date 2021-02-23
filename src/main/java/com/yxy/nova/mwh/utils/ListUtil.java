package com.yxy.nova.mwh.utils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 *
 * @description:
 * @author: renshui
 * @date: 2020-06-01 5:06 下午
 */
public class ListUtil {

    /**
     * 判断两个列表是否相等，不考虑顺序
     * @param list1
     * @param list2
     * @return
     */
    public static boolean isEqualList(Collection list1, Collection list2) {
        if (CollectionUtils.isEmpty(list1) && CollectionUtils.isEmpty(list2)) {
            return true;
        } else if (list1 == list2) {
            return true;
        } else {
            return list1 != null
                    && list2 != null
                    && list1.size() == list2.size()
                    && list1.containsAll(list2);
        }
    }
}
