package test.util;

import com.alibaba.fastjson.JSON;
import com.yxy.nova.web.util.PasswordUtil;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 *
 * @description:
 * @author: shui.ren
 * @date: 2019-11-25 上午11:24
 */
public class PasswordUtilTest {
    @Test
    public void encryptPassword() throws Exception {
        System.out.println(JSON.toJSONString(PasswordUtil.encryptPassword("123456")));
    }

}