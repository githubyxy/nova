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

    private String client_id = "02f98ed2-3c85-43ac-b4cb-bce89cac192fy";
    private String client_secret = "-UspyAK-8gg8hmChm_89J_EPSdlnfI0zdahn10i0WCNqdO3NHnBA82zQfp98Dz2TWeeO7q88J4EfNZbGxy";
    private String grant_type = "client_credentials";

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

        String result = httpClient.post("https://oauth.cnblogs.com/connect/token", request);
        JSONObject data = JSONObject.parseObject(result);
        accessToken = data.getString("access_token");
        expiresAt = data.getLong("expires_in") * 1000;
    }


    @SneakyThrows
    public void authorize() {
        List<NameValuePair> request = new ArrayList();
        request.add(new BasicNameValuePair("client_id", client_id));
        request.add(new BasicNameValuePair("scope", "openid profile CnBlogsApi offline_access"));
        request.add(new BasicNameValuePair("response_type", "code id_token"));
        request.add(new BasicNameValuePair("redirect_uri", "https://oauth.cnblogs.com/auth/callback"));
        request.add(new BasicNameValuePair("state", "q12w2aj8627hd7"));
        request.add(new BasicNameValuePair("nonce", "u17j3g73hh3hg3jh3"));

        String result = httpClient.get("https://oauth.cnblogs.com/connect/authorize", request);


    }


}
