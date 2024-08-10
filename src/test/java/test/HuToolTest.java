package test;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
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
}
