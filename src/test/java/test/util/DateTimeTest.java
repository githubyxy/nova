package test.util;

import com.yxy.nova.mwh.utils.text.TextUtil;
import com.yxy.nova.mwh.utils.time.DateTimeUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTimeTest {
    @Test
    public void test() {
        LocalDateTime parse = LocalDateTime.parse("2023-06-28T02:14:54+0390".substring(0,19), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date from = Date.from(parse.atZone(defaultZoneId).toInstant());
        System.out.println(DateTimeUtil.datetime18(from));
    }

    @Test
    public void test2() {
        System.out.println(DateTimeUtil.date10(DateTimeUtil.plusMonths(new Date(), -7)) );


        BigDecimal divide = new BigDecimal("12700").divide(new BigDecimal(3600 * 1000), 0, BigDecimal.ROUND_HALF_UP);
        System.out.println(divide.toPlainString());

        System.out.println(DateTimeUtil.getWeekNumber("2023-08-24"));

        Pattern compile = Pattern.compile("^-?\\d+(\\.\\d+)?$");
        System.out.println(compile.matcher("12").matches());
        System.out.println(compile.matcher("12.0").matches());
        System.out.println(compile.matcher("12.23").matches());
        System.out.println(compile.matcher("-12").matches());
        System.out.println(compile.matcher("-12.388").matches());

        System.out.println(DateTimeUtil.getHour());
    }

    @Test
    public void tset3() {
        int minute = DateTimeUtil.asLocalDateTIme(new Date()).getMinute();
//        System.out.println(minute);
//        if (minute % 10 == 0) {
//            System.out.println("触发");
//        } else {
//            System.out.println("不触发");
//        }

        BigDecimal divide = new BigDecimal(3209).divide(new BigDecimal(3467), 4, BigDecimal.ROUND_HALF_UP);
        System.out.println(divide.toPlainString());
        System.out.println(divide.compareTo(new BigDecimal(0.9)) < 0);

    }
}
