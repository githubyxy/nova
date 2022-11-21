package com.yxy.nova.web;

/**
 * Created by yuxiaoyu
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yxy.nova.service.wechat.WechatService;
import com.yxy.nova.util.SignUtil;
import com.yxy.nova.util.SimpleHttpClient;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@RestController
@RequestMapping(value="/wechat")
public class WechatController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";

    @Autowired
    private SimpleHttpClient simpleHttpClient;
    @Autowired
    private WechatService wechatService;

    /**
     * 微信-自定义菜单
     * https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Creating_Custom-Defined_Menu.html
     * @param
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/createMenu",method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public void connectWeixin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String accessToken = wechatService.getAccessToken();

        JSONObject data = new JSONObject();
        JSONArray button = new JSONArray();
        JSONObject memo = new JSONObject();
        memo.put("type", "click");
        memo.put("name", "今日天气");
        memo.put("key", "weather");

        button.add(memo);

        data.put("button", button.toJSONString());
        String result = simpleHttpClient.postJson(CREATE_MENU_URL + accessToken, data);
        LOGGER.info("connectWeixin result={}", result);
    }


}
