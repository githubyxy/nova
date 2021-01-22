package com.yxy.nova.util.wechat.weather;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author yuxiaoyu
 * @date 2021/1/22 下午5:39
 * @Description
 */
public class TianQiCity {

    public String getCitys() {

        ClassPathResource cpr = new ClassPathResource("city/city.json");
        try {

            byte[] bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
            return new String(bdata, StandardCharsets.UTF_8);
        } catch (Exception e) {

        }
        return "";
    }
}
