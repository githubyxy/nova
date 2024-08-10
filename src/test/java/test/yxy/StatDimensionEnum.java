package test.yxy;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;

import java.time.LocalDateTime;

/**
 * @author yxy
 * @description: 统计维度枚举
 * @date 2023/6/24 8:28 PM
 */
public enum StatDimensionEnum {
    DAY("日"),
    MONTH("月"),
    ;

    private String desc;

    StatDimensionEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 将yyyy-MM-dd日期 转换为 具体统计维度的格式
     * @param date10
     * @return
     */
    public String convertDate(String date10) {
        if (DAY == this) {
            return date10;
        }
        if (MONTH == this) {
            return date10.substring(0,7);
        }
        return "";
    }

    /**
     * 获取 具体统计维度的 最小的yyyy-MM-dd日期
     * @param dateStr
     * @return
     */
    public String getMinDate10(String dateStr) {
        if (DAY == this) {
            return dateStr;
        }
        if (MONTH == this) {
            return dateStr + "-01";
        }
        return dateStr;
    }

    /**
     * 获取 具体统计维度的 最大的yyyy-MM-dd日期
     * @param dateStr
     * @return
     */
    public String getMaxDate10(String dateStr) {
        if (DAY == this) {
            return dateStr;
        }
        if (MONTH == this) {
            LocalDateTime localDateTime = DateUtil.parseLocalDateTime(dateStr, DatePattern.NORM_MONTH_PATTERN).plusMonths(1).plusDays(-1);
            return DateUtil.format(localDateTime, DatePattern.NORM_DATE_PATTERN);
        }
        return dateStr;
    }

}
