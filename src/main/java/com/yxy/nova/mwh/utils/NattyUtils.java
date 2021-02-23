package com.yxy.nova.mwh.utils;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by lktoken on 2016/10/27.
 */
public class NattyUtils {

    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT     = "yyyy-MM-dd";

    public static Date simpleParse(String text) {
        Parser p = new Parser();
        List<DateGroup> groups = p.parse(text);
        if (CollectionUtils.isEmpty(groups)) {
            return null;
        }
        DateGroup dateGroup = groups.get(0);
        List<Date> dates = dateGroup.getDates();
        if (CollectionUtils.isEmpty(dates) || dates.size() > 1) {
            throw new IllegalArgumentException("Date sting wrong");
        }

        return dates.get(0);
    }

    public static Date parse(String text) {
        try {
            return simpleParse(text);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date fparse(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        if (NumberUtils.isDigits(text)) {
            return new Date(NumberUtils.toLong(text));
        }
        try {
            return DateUtils.parseDate(text, DATE_FORMAT, DATETIME_FORMAT);
        } catch (ParseException e) {
            return null;
        }
    }

}
