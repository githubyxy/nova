package test;

import cn.hutool.core.date.DateUtil;
import org.junit.Test;

import java.util.Date;

public class HuToolTest {

    @Test
    public void test() throws Exception {
        String s = DateUtil.formatDateTime(new Date());
        System.out.println(s);
    }
}
