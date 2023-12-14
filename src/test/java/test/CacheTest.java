package test;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class CacheTest {

    @Test
    public void test() {
         Cache<String, String> talkConfig2templateCache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build();

        talkConfig2templateCache.put("a", "aaaa");

        talkConfig2templateCache.invalidate("b");


    }

}
