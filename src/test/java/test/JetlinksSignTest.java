package test;

import com.alibaba.fastjson.JSONObject;
import com.yxy.nova.mwh.utils.MD5Util;
import com.yxy.nova.mwh.utils.time.DateTimeUtil;
import com.yxy.nova.util.SimpleHttpClient;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JetlinksSignTest {

    @Test
    public void test() {
        String secureKey = "8hcdymewy2ed7nHwarps7cA5"; //密钥
        SimpleHttpClient simpleHttpClient = new SimpleHttpClient();

        JSONObject post = new JSONObject();
        post.put("expires", 7200);

        Map<String, String> headMap = new HashMap<>();
        headMap.put("X-Client-Id", "JxwBDxt5dwP8nadJ");
        long time = System.currentTimeMillis();
        headMap.put("X-Timestamp", time + "");
        System.out.println(post.toJSONString()+time+secureKey);
        String sign = MD5Util.md5(post.toJSONString()+time+secureKey);
        headMap.put("X-Sign", sign);
        try {
            String s = simpleHttpClient.postJson("http://36.103.226.168:8088/jetlinks/api/v1/token", post, headMap);

            System.out.println(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
