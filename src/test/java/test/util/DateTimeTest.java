package test.util;

import com.yxy.nova.mwh.utils.time.DateTimeUtil;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeTest {
    @Test
    public void test() {
        LocalDateTime parse = LocalDateTime.parse("2023-06-28T02:14:54+0390".substring(0,19), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date from = Date.from(parse.atZone(defaultZoneId).toInstant());
        System.out.println(DateTimeUtil.datetime18(from));
    }
}
