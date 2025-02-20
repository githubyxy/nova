package test;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.yxy.nova.mwh.utils.time.DateTimeUtil;
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

    @Test
    public void test3() {
        DateTime dateTime = DateUtil.beginOfMonth(new Date());
        String s = DateUtil.formatDateTime(dateTime);
        System.out.println(s);


        String s1 = DateUtil.formatDateTime(DateUtil.beginOfMonth(DateUtil.lastMonth()));
        System.out.println(s1);
        String s2 = DateUtil.formatDateTime(DateUtil.endOfMonth(DateUtil.lastMonth()));
        System.out.println(s2);

        int i = DateUtil.dayOfMonth(new Date());
        System.out.printf(i + "");
    }

    @Test
    public void test4() {
        int minute = DateTimeUtil.asLocalDateTIme(new Date()).getMinute();
        int minute2 = DateTimeUtil.asLocalDateTIme(DateTimeUtil.parseDatetime18("2025-02-18 09:01:59")).getMinute();
        System.out.println(minute);
        System.out.println(minute2);


        String s = "【闪应科技】1.业务名称:你我贷;任务id:23201/23191,当天存在23945数据未分配。\n" +
                "2.业务名称:宜享花;任务id:21271/20791/20631/20411/16951,当天存在95491数据未分配。\n" +
                "3.业务名称:测试专用;任务id:23061,当天存在2数据未分配。\n";
        System.out.println(s.replaceAll("】.*", "】"));
    }

}
