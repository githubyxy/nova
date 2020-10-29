package test;

import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author yuxiaoyu
 * @date 2020/5/11 上午10:02
 * @Description
 */
public class RedisTest {

    @Test
    public void test() {

        Jedis jedis = new Jedis("120.48.8.74", 6379);
        jedis.set("a","b");
    }


}
