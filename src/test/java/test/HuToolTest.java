package test;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Date;

public class HuToolTest {

    @Test
    public void test() throws Exception {
        String s = DateUtil.formatDateTime(new Date());
        System.out.println(s);

        LocalDateTime localDateTime = DateUtil.parseLocalDateTime("2024-09", DatePattern.NORM_MONTH_PATTERN).plusMonths(1).plusDays(-1);
        String data10 = DateUtil.format(localDateTime, DatePattern.NORM_DATE_PATTERN);
        System.out.println(data10);

    }

    @Test
    public void test2() {
        Date deleteDate = DateUtil.offsetDay(new Date(), -0);
        System.out.println(deleteDate);

        System.out.println(DateUtil.offsetDay(new Date(), -1).getTime() < deleteDate.getTime());

        String allowTimeWindows = "00:00:00-06:00:00";
        String[] split = allowTimeWindows.split("-");
        String s = DateUtil.formatTime(new Date());
        System.out.println(s.compareTo(split[0])>=0 && s.compareTo(split[1])<=0);

        String s1="561";
        String s2="00561";
        String s3="010";
        System.out.println(s1.replaceFirst("^0*",""));
        System.out.println(s2.replaceFirst("^0*",""));
        System.out.println(s3.replaceFirst("^0*",""));

    }

}
