package test.yxy;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yuxiaoyu
 * @description:
 * @date 2024/8/14
 */
public class HutoolUtilTest {

    @Test
    public void test() {
        String startDate = "2024-09-15";
        String endDate = "2024-09-15";

        int l = (int) (DateUtil.betweenDay(DateUtil.parseDate(startDate), DateUtil.parseDate(endDate), true) + 1);
        System.out.println(l);
        DateTime start1 = DateUtil.offsetDay(DateUtil.parseDate(startDate), -l);
        DateTime end1 = DateUtil.offsetDay(DateUtil.parseDate(endDate), -l);

        System.out.println(start1);
        System.out.println(end1);
    }

    @Test
    public void test2() {
        long prevTaskItemCount = 2322;
        long taskItemCount = 3335;
        String s = String.format("%.2f", (taskItemCount - prevTaskItemCount) * 100.0 / prevTaskItemCount) + "%";
        System.out.println(s);

        BigDecimal divide = new BigDecimal(Math.abs(taskItemCount - prevTaskItemCount)).divide(new BigDecimal(prevTaskItemCount), 5, BigDecimal.ROUND_HALF_UP);
        System.out.println(divide.toPlainString());

        String value = divide.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString();
        System.out.println(value);
    }

    @Test
    public void test3() {
        String s = "2024-4-1";
        String s1 = "2024-1-30";

        int i = DateUtil.weekOfYear(DateUtil.parseDate(s));
        System.out.println("weekOfYear:"  + i);

        System.out.println(DateUtil.formatDate(DateUtil.beginOfWeek(DateUtil.parseDate(s), true)));
        System.out.println(DateUtil.formatDate(DateUtil.endOfWeek(DateUtil.parseDate(s), true)));


        System.out.println(DateUtil.quarter(DateUtil.parseDate(s)));

        int month = DateUtil.month(DateUtil.parseDate(s));
        System.out.println(month);

        System.out.println(DateUtil.year(DateUtil.parseDate(s)));
        System.out.println(DateUtil.dayOfMonth(DateUtil.parseDate(s)));
        System.out.println(DateUtil.dayOfMonth(DateUtil.endOfMonth(DateUtil.parseDate(s))));

        System.out.println(DateUtil.month(DateUtil.parseDate(s)));
        System.out.println(DateUtil.dayOfMonth(DateUtil.endOfMonth(DateUtil.parseDate(s))));

        System.out.println(DateUtil.formatDate(DateUtil.endOfMonth(DateUtil.parseDate( "2024-09-01"))));

        System.out.println(DateUtil.getLastDayOfMonth(DateUtil.parseDate(s)));
    }

    @Test
    public void test4() {
        String startReportDate = "2024-09-18";
        String endReportDate = "2024-09-18";
        int betweenDays = (int) (DateUtil.betweenDay(DateUtil.parseDate(startReportDate), DateUtil.parseDate(endReportDate), true) + 1);
        String prevStartReportDate = DateUtil.formatDate(DateUtil.offsetDay(DateUtil.parseDate(startReportDate), -betweenDays));
        String prevEndReportDate = DateUtil.formatDate(DateUtil.offsetDay(DateUtil.parseDate(endReportDate), -betweenDays));

        System.out.println(prevStartReportDate);
        System.out.println(prevEndReportDate);

        System.out.println((int)Math.ceil( 240 / 1000D));
    }
}
