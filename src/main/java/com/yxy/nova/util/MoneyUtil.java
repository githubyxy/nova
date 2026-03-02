package com.yxy.nova.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @author yuxiaoyu
 * @date 2019-01-29 09:56
 * @Description
 */
public class MoneyUtil {
    /**
     * 去掉金额中的千隔符
     */
    public static String parseMoney(String money) {
        if (null == money)
            return null;
        return money.replaceAll("[^\\d\\.-]", "");
    }


    /**
     * BigDecimal 除法运算，保留scale位小数
     *
     * @param num
     * @param num2
     * @param scale
     * @return
     */
    public static BigDecimal divide(BigDecimal num, BigDecimal num2, int scale) {
        if (BigDecimal.ZERO.compareTo(num2) == 0) {
            return BigDecimal.ZERO;
        }
        return num.divide(num2, scale, BigDecimal.ROUND_HALF_UP);
    }


    public static BigDecimal divide(Integer num, Integer num2, int scale) {
        if (num == null || num2 == null) {
            return BigDecimal.ZERO;
        }
        return divide(new BigDecimal(num), new BigDecimal(num2), scale);
    }


    public static BigDecimal mul(BigDecimal num, BigDecimal num2) {
        return num.multiply(num2);
    }

    /**
     * 转化利率
     * 乘上100，并保留指定位数然后加上%
     * @param interestRate
     */
    public static String convertRateToString(BigDecimal interestRate, int scale) {
        String value = mul(interestRate, new BigDecimal(100)).setScale(scale, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString();
        return value + "%";
    }

    /**
     * 转化利率,最大保留五位小数:
     * 10.1% to 0.101
     * 10.1 to 0.101
     * 10.10% to 0.101
     * 10.10 to 0.101
     */
    public static BigDecimal convertStringToInterestRate(String interestRate) {
        if (StringUtils.isBlank(interestRate)) {
            return null;
        }
        double doubleIn;
        if (-1 != interestRate.indexOf("%")) {
            doubleIn = Double.parseDouble(interestRate.substring(0, interestRate.length() - 1));
        } else {
            doubleIn = Double.parseDouble(interestRate);
        }
        BigDecimal bigDecimal = new BigDecimal(doubleIn).divide(new BigDecimal(100), 5, BigDecimal.ROUND_HALF_UP);
        return bigDecimal.stripTrailingZeros();
    }


}
