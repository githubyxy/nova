package com.yxy.nova.service.cnblogs;

import com.alibaba.fastjson.JSONObject;
import com.yxy.nova.util.SimpleHttpClient;
import lombok.SneakyThrows;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CnblogServiceImpl implements CnblogService{

    @Autowired
    private SimpleHttpClient httpClient;

    private String client_id = "";
    private String client_secret = "";
    private String grant_type = "";

    private String accessToken;

    private long expiresAt;

//    /**
//     */
//    private Cache<String, String> accessTokenCache = CacheBuilder.newBuilder()
//            .build();


    private String getAccessToken() throws IOException {
        if (accessToken == null || isExpired()) {
            refreshToken();
        }
        return accessToken;
    }

    private boolean isExpired() {
        return System.currentTimeMillis() >= expiresAt;
    }


    @SneakyThrows
    public void refreshToken() {
        List<NameValuePair> request = new ArrayList();
        request.add(new BasicNameValuePair("client_id", client_id));
        request.add(new BasicNameValuePair("client_secret", client_secret));
        request.add(new BasicNameValuePair("grant_type", grant_type));

        String result = httpClient.post("https://api.cnblogs.com/token", request);
        JSONObject data = JSONObject.parseObject(result);
        accessToken = data.getString("access_token");
        expiresAt = data.getLong("expires_in") * 1000;
    }
}
