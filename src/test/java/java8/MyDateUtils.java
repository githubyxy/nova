package java8;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class MyDateUtils {


    public static String getDateNow() {
        return LocalDate.now().toString();
    }

    public static Integer getDateByUnit(ChronoUnit unit) {
        LocalDate now = LocalDate.now();
        switch (unit) {
            case YEARS:
                return now.getYear();
            case MONTHS:
                return now.getMonthValue();
            case DAYS:
                return now.getDayOfMonth();
            default:
                break;
        }
        return null;
    }

    public static String formatDate(Date date, String formatter) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatter);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        String format = localDateTime.format(dateTimeFormatter);
        return format;
    }

    public static Date parseDateTime(String date, String formatter) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatter);
        LocalDateTime localDateTime = LocalDateTime.parse(date, dateTimeFormatter);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static Date parseDate(String date, String formatter) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatter);
        LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static Date addDate(Date date, long addNum, ChronoUnit unit) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDateTime newDate = localDateTime.plus(addNum, unit);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = newDate.atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static void main(String[] args) {
        System.out.println(formatDate(new Date(), "yyyy-MM-dd"));
        System.out.println(formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }

}
