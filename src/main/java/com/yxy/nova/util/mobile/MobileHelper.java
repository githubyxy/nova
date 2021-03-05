package com.yxy.nova.util.mobile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yxy.nova.mwh.utils.MobileUtil;
import com.yxy.nova.util.SimpleHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuxiaoyu
 * @date 2021/3/5 下午5:27
 * @Description
 */
@Component
@Slf4j
public class MobileHelper {
    @Autowired
    private SimpleHttpClient simpleHttpClient;


    public String queryMobileInfo(String phoneNumber) {
        // 把手机号码补全11位
        while (phoneNumber.length() < 11) {
            phoneNumber = phoneNumber + "0";
        }

        List<NameValuePair> paramList = new ArrayList<>();
        SimpleHttpClient simpleHttpClient = new SimpleHttpClient();
        paramList.add(new BasicNameValuePair("phone", phoneNumber));

        try {
            log.info("查询手机号码归属地");
            String result = simpleHttpClient.post("http://www.189.cn/trade/recharge/captcha/type.do", paramList);
            log.info("查询手机号码归属地返回结果:【{}】", result);

            JSONObject jsonObject = JSON.parseObject(result);
            if (jsonObject.getString("code").equals("0")) {
                JSONObject dataObject = jsonObject.getJSONObject("dataObject");
                StringBuffer sb = new StringBuffer();
                sb.append("手机号码：").append(phoneNumber).append("\n");
                sb.append("省份：").append(dataObject.getString("province")).append("\n");
                sb.append("城市：").append(dataObject.getString("city")).append("\n");
                sb.append("城市行政编码：").append(dataObject.getString("cityCode").substring(1)).append("\n");
                sb.append("区域代码：").append(dataObject.getString("areaCode")).append("\n");
                sb.append("运营商：").append(MobileUtil.getOperator(phoneNumber).getDesc());
                return sb.toString();
            }
        } catch (Exception e) {
            log.error("查询手机号码归属地异常", e);
            return null;
        }
        return null;
    }

}
