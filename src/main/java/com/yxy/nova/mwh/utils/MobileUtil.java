package com.yxy.nova.mwh.utils;

import com.yxy.nova.mwh.utils.constant.ISPEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @description:
 * @author: renshui
 * @date: 2020-06-02 11:30 下午
 */
public class MobileUtil {
    /**
     * 每家运营商的前缀库
     */
    private static Map<ISPEnum, List<String>> operatorPrefixLib = new HashMap<>(4);

    static {
        operatorPrefixLib.put(ISPEnum.CMCC, Arrays.asList(new String[] {"134","135","136","137","138","139","147","150","151","152","157","158","159","172","178","182","183","184","187","188","195","197","198","1705"}));
        operatorPrefixLib.put(ISPEnum.CUCC, Arrays.asList(new String[] {"130","131","132","145","155","156","166","167","171","175","176","185","186","196","1709"}));
        operatorPrefixLib.put(ISPEnum.CTCC, Arrays.asList(new String[] {"133","149","153","1700","173","174","177","180","181","189","190","191","193","199"}));
    }

    /**
     * 获取手机号归属的运营商
     * @param mobile
     * @return
     */
    public static ISPEnum getOperator(String mobile) {
        mobile = StringUtils.trimToEmpty(mobile);
        // 先匹配前4位
        String prefix4 = mobile.substring(0,4);
        Optional<ISPEnum> operatorType = prefixMatch(prefix4);
        if (operatorType.isPresent()) {
            return operatorType.get();
        }

        // 再匹配前3位
        String prefix3 = mobile.substring(0,3);
        return prefixMatch(prefix3).orElse(null);
    }

    /**
     * 返回匹配指定前缀的运营商
     * @param prefix
     * @return
     */
    private static Optional<ISPEnum> prefixMatch(String prefix) {
        return operatorPrefixLib.entrySet()
                .stream()
                .filter(entry -> entry.getValue().contains(prefix))
                .map(entry -> entry.getKey())
                .findFirst();
    }


}
