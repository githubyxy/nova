package com.yxy.nova.mwh.utils.time;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateTimeUtil {

    /**
     * yyyyMMdd格式
     */
    private static DateTimeFormatter DATE8_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * yyyy-MM-dd格式
      */
    private static DateTimeFormatter DATE10_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * HHmmss格式
     */
    private static DateTimeFormatter TIME6_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");

    /**
     * HH:mm:ss格式
     */
    private static DateTimeFormatter TIME8_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * yyyyMMddHHmmss格式
     */
    private static DateTimeFormatter DATETIME14_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * yyyy-MM-dd HH:mm:ss格式
     */
    private static DateTimeFormatter DATETIME18_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    /**
     * 以yyyyMMdd的格式返回当前日期
     * @return
     */
    public static String date8() {
        return date8(new Date());
    }

    /**
     * 以yyyyMMdd的格式返回指定的日期
     * @return
     */
    public static String date8(Date date) {
        return asLocalDateTIme(date).format(DATE8_FORMATTER);
    }

    /**
     * 将yyyyMMdd的格式解析成Date
     * @param date
     * @return
     */
    public static Date parseDate8(String date) {
        return asDate(LocalDate.parse(date, DATE8_FORMATTER).atStartOfDay());
    }

    /**
     * 校验传入的日期是否是合法的yyyyMMdd格式
     * @param date
     * @return
     */
    public static boolean validateDate8(String date) {
        try {
            if (date.length() != 8) {
                return false;
            }

            return date.equals(date8(parseDate8(date)));

        } catch (Throwable thr) {
            return false;
        }
    }

    /**
     * 以yyyy-MM-dd的格式返回当前日期
     * @return
     */
    public static String date10() {
        return date10(new Date());
    }

    /**
     * 以yyyy-MM-dd的格式返回指定的日期
     * @return
     */
    public static String date10(Date date) {
        return asLocalDateTIme(date).format(DATE10_FORMATTER);
    }

    /**
     * 以yyyy-MM-dd的格式返回当前日期的后一天
     * @return
     */
    public static String nextDate10() {
        return nextDate10(new Date());
    }

    /**
     * 以yyyy-MM-dd的格式返回指定日期的前一天
     * @return
     */
    public static String nextDate10(Date date) {
        return asLocalDateTIme(date).plusDays(1).format(DATE10_FORMATTER);
    }

    /**
     * 以yyyy-MM-dd的格式返回当前日期的后一天
     * @return
     */
    public static String previousDate10() {
        return previousDate10(new Date());
    }

    /**
     * 以yyyy-MM-dd的格式返回指定日期的前一天
     * @return
     */
    public static String previousDate10(Date date) {
        return asLocalDateTIme(date).minusDays(1).format(DATE10_FORMATTER);
    }

    /**
     * 增加指定的月份数
     * @param date
     * @param months
     * @return
     */
    public static Date plusMonths(Date date, int months) {
        return asDate(asLocalDateTIme(date).plusMonths(months));
    }

    /**
     * 减少指定的月份数
     * @param date
     * @param months
     * @return
     */
    public static Date minusMonths(Date date, int months) {
        return asDate(asLocalDateTIme(date).minusMonths(months));
    }

    /**
     * 增加指定的天数
     * @param date
     * @param days
     * @return
     */
    public static Date plusDays(Date date, int days) {
        return asDate(asLocalDateTIme(date).plusDays(days));
    }

    /**
     * 增加指定的天数
     * @param date 格式: yyyy-MM-dd
     * @param days
     * @return
     */
    public static String plusDate10(String date, int days) {
        return date10(plusDays(parseDate10(date), days));
    }

    /**
     * 减少指定的天数
     * @param date 格式: yyyy-MM-dd
     * @param days
     * @return
     */
    public static String minusDate10(String date, int days) {
        return date10(minusDays(parseDate10(date), days));
    }

    /**
     * 减少指定的天数
     * @param date
     * @param days
     * @return
     */
    public static Date minusDays(Date date, int days) {
        return asDate(asLocalDateTIme(date).minusDays(days));
    }

    /**
     * 增加指定的小时数
     * @param date
     * @param hours
     * @return
     */
    public static Date plusHours(Date date, int hours) {
        return asDate(asLocalDateTIme(date).plusHours(hours));
    }

    /**
     * 减少指定的小时数
     * @param date
     * @param hours
     * @return
     */
    public static Date minusHours(Date date, int hours) {
        return asDate(asLocalDateTIme(date).minusHours(hours));
    }

    /**
     * 增加指定的分钟数
     * @param date
     * @param minutes
     * @return
     */
    public static Date plusMinutes(Date date, int minutes) {
        return asDate(asLocalDateTIme(date).plusMinutes(minutes));
    }

    /**
     * 减少指定的小时数
     * @param date
     * @param minutes
     * @return
     */
    public static Date minusMinutes(Date date, int minutes) {
        return asDate(asLocalDateTIme(date).minusMinutes(minutes));
    }

    /**
     * 增加指定的秒数
     * @param date
     * @param seconds
     * @return
     */
    public static Date plusSeconds(Date date, int seconds) {
        return asDate(asLocalDateTIme(date).plusSeconds(seconds));
    }

    /**
     * 减少指定的秒数
     * @param date
     * @param seconds
     * @return
     */
    public static Date minusSeconds(Date date, int seconds) {
        return asDate(asLocalDateTIme(date).minusSeconds(seconds));
    }

    /**
     * 获取今天的开始时间
     * @return
     */
    public static Date startOfDay() {
        return startOfDay(new Date());
    }

    /**
     * 获取指定日期的开始时间
     * @param date
     * @return
     */
    public static Date startOfDay(Date date) {
        return asDate(asLocalDateTIme(date).truncatedTo(ChronoUnit.DAYS));
    }

    /**
     * 获取明天的开始时间
     * @return
     */
    public static Date startOfTomorrow() {
        return startOfTomorrow(new Date());
    }

    /**
     * 获取明天的开始时间
     * @param date
     * @return
     */
    public static Date startOfTomorrow(Date date) {
        return startOfDay(DateTimeUtil.plusDays(date, 1));
    }

    /**
     * 验证传入的date是否是指定的pattern
     * @param date
     * @param pattern
     * @return
     */
    public static boolean validate(String date, String pattern) {
        try {
            return date.equals(format(asDate(LocalDateTime.parse(date, DateTimeFormatter.ofPattern(pattern))), pattern));
        } catch (Throwable thr) {
            return false;
        }
    }

    /**
     * 校验传入的日期是否是合法的yyyy-MM-dd格式
     * @param date
     * @return
     */
    public static boolean validateDate10(String date) {
        try {
            if (date.length() != 10) {
                return false;
            }

            return date.equals(date10(parseDate10(date)));

        } catch (Throwable thr) {
            return false;
        }
    }

    /**
     * 校验传入的日期是否是合法的yyyy-MM-dd HH:mm:ss格式
     * @param date
     * @return
     */
    public static boolean validateDate18(String date) {
        try {
            if (date.length() != 19) {
                return false;
            }

            return date.equals(datetime18(parseDatetime18(date)));

        } catch (Throwable thr) {
            return false;
        }
    }

    /**
     * 返回HHmmss格式的时间
     * @return
     */
    public static String time6() {
        return time6(new Date());
    }

    /**
     * 返回HHmmss格式的时间
     * @return
     */
    public static String time6(Date date) {
        return asLocalDateTIme(date).format(TIME6_FORMATTER);
    }

    /**
     * 返回HH:mm:ss格式的时间
     * @return
     */
    public static String time8() {
        return time8(new Date());
    }

    /**
     * 返回HH:mm:ss格式的时间
     * @return
     */
    public static String time8(Date date) {
        return asLocalDateTIme(date).format(TIME8_FORMATTER);
    }

    /**
     * 返回yyyyMMddHHmmss格式的时间
     * @return
     */
    public static String datetime14() {
        return datetime14(new Date());
    }

    /**
     * 返回yyyyMMddHHmmss格式的时间
     * @param date
     * @return
     */
    public static String datetime14(Date date) {
        return asLocalDateTIme(date).format(DATETIME14_FORMATTER);
    }

    /**
     * 返回yyyy-MM-dd HH:mm:ss格式的时间
     * @return
     */
    public static String datetime18() {
        return datetime18(new Date());
    }

    /**
     * 返回yyyy-MM-dd HH:mm:ss格式的时间
     * @param date
     * @return
     */
    public static String datetime18(Date date) {
        return asLocalDateTIme(date).format(DATETIME18_FORMATTER);
    }

    /**
     * 格式化日期
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        return asLocalDateTIme(date).format(DateTimeFormatter.ofPattern(pattern));
    }


    /**
     * 将yyyy-MM-dd的格式解析成Date
     * @param date
     * @return
     */
    public static Date parseDate10(String date) {
        return asDate(LocalDate.parse(date, DATE10_FORMATTER).atStartOfDay());
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss的格式解析成Date
     * @param date
     * @return
     */
    public static Date parseDatetime18(String date) {
        return asDate(LocalDateTime.parse(date, DATETIME18_FORMATTER));
    }

    /**
     * 将yyyyMMddHHmmss的格式解析成Date
     * @param date
     * @return
     */
    public static Date parseDatetime14(String date) {
        return asDate(LocalDateTime.parse(date, DATETIME14_FORMATTER));
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss的格式解析成Date
     * @param date
     * @return
     */
    public static Date parse(String date, String pattern) {
        return asDate(LocalDateTime.parse(date, DateTimeFormatter.ofPattern(pattern)));
    }


    /**
     * 将Date转换成LocalDateTime
     * @param date
     */
    public static LocalDateTime asLocalDateTIme(Date date) {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        return date.toInstant().atZone(defaultZoneId).toLocalDateTime();
    }

    /**
     * 将LocalDateTime转换为Date
     * @param localDateTime
     * @return
     */
    public static Date asDate(LocalDateTime localDateTime) {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        return Date.from(localDateTime.atZone(defaultZoneId).toInstant());
    }

    /**
     * 获取当前的年份
     * @return
     */
    public static int getYear() {
        return asLocalDateTIme(new Date()).getYear();
    }

    /**
     * 获取当前的月份。一月份返回1, 十二月返回12
     * @return
     */
    public static int getMonth() {
        return asLocalDateTIme(new Date()).getMonthValue();
    }

    /**
     * 获取当前是周几。周一返回1， 周日返回7
     * @return
     */
    public static int getDayOfWeek() {
        return asLocalDateTIme(new Date()).getDayOfWeek().getValue();
    }

    /**
     * 获取当前的小时
     * @return
     */
    public static int getHour() {
        return asLocalDateTIme(new Date()).getHour();
    }

    /**
     * 获取指定日期的小时
     * @param date
     * @return
     */
    public static int getHour(Date date) {
        return asLocalDateTIme(new Date()).getHour();
    }

    /**
     * 返回两个日期的差异，单位：天, 向下取整
     * @param d1
     * @param d2
     * @return
     */
    public static long diffInDay(Date d1, Date d2) {
        Duration duration = Duration.between(asLocalDateTIme(d1), asLocalDateTIme(d2));
        return Math.abs(duration.toDays());
    }

    /**
     * 返回两个日期的差异，单位：小时, 向下取整
     * @param d1
     * @param d2
     * @return
     */
    public static long diffInHour(Date d1, Date d2) {
        Duration duration = Duration.between(asLocalDateTIme(d1), asLocalDateTIme(d2));
        return Math.abs(duration.toHours());
    }

    /**
     * 返回两个日期的差异，单位：分钟, 向下取整
     * @param d1
     * @param d2
     * @return
     */
    public static long diffInMinute(Date d1, Date d2) {
        Duration duration = Duration.between(asLocalDateTIme(d1), asLocalDateTIme(d2));
        return Math.abs(duration.toMinutes());
    }

    /**
     * 返回两个日期的差异，单位：秒, 向下取整
     * @param d1
     * @param d2
     * @return
     */
    public static long diffInSecond(Date d1, Date d2) {
        Duration duration = Duration.between(asLocalDateTIme(d1), asLocalDateTIme(d2));
        return Math.abs(duration.toMillis() / 1000);
    }

    /**
     * 返回整点。 比如返回: 2019-06-29 18:00:00
     * @param date
     * @param hour
     * @return
     */
    public static Date atHour(Date date, int hour) {
        return plusHours(startOfDay(date), hour);
    }

    /**
     * 返回今日整点。 比如返回: 2019-06-29 18:00:00
     * @param hour
     * @return
     */
    public static Date atHour(int hour) {
        return atHour(new Date(), hour);
    }
}