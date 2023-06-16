package test.util.codec;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

/**
 * @author yuxiaoyu
 * @date 2023/5/13 10:11 AM
 * @Description
 */
public class CommonTest {
    @Test
    public void test() {
        System.out.println(DigestUtils.md5Hex("13585934620"));
        System.out.println(DigestUtils.md5Hex("13585934620").equals("9c2e72a74e6133c2c5771236dc1b0472"));
        System.out.println(DigestUtils.sha256Hex("13585934620").substring(24, 40));
        System.out.println(DigestUtils.sha256Hex("13585934620").equals("c1d409104ecec13fe694b1fe0a83388fbb7e3eacdb65fc38b6ed5bf226ed9a8d"));

    }
}
