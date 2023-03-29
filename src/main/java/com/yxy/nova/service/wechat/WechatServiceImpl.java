package com.yxy.nova.service.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yxy.nova.bean.wechat.TextMessage;
import com.yxy.nova.mwh.utils.DataUtil;
import com.yxy.nova.mwh.utils.text.TextUtil;
import com.yxy.nova.service.impl.MyOpenAiService;
import com.yxy.nova.service.impl.MyPowerService;
import com.yxy.nova.util.MessageUtil;
import com.yxy.nova.util.SimpleHttpClient;
import com.yxy.nova.util.mobile.MobileHelper;
import com.yxy.nova.util.wechat.weather.TianQiCityID;
import com.yxy.nova.util.wechat.weather.TianQiWeatherHelper;
import com.yxy.nova.web.util.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * @author yuxiaoyu
 * @date 2021/1/22 上午11:38
 * @Description
 */
@Service
public class WechatServiceImpl implements WechatService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static JSONObject accessToken = new JSONObject();

    @Value("${wechat-AppId}")
    private String appleId;
    @Value("${wechat-AppSecret}")
    private String appSecret;
    @Autowired
    private SimpleHttpClient simpleHttpClient;
    @Autowired
    private MobileHelper mobileHelper;
    @Autowired
    private MyPowerService myPowerService;
    @Autowired
    private MyOpenAiService myOpenAiService;



    /**
     * 处理微信发来的请求
     *
     * @param request
     * @return
     */
    @Override
    public String weixinPost(HttpServletRequest request) {
        String respMessage = null;
        try {

            // xml请求解析
            Map<String, String> requestMap = MessageUtil.xmlToMap(request);

            // 发送方帐号（open_id）
            String fromUserName = requestMap.get("FromUserName");
            // 公众帐号
            String toUserName = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");
            // 消息内容
            String content = requestMap.get("Content");

            LOGGER.info("FromUserName is:" + fromUserName + ", ToUserName is:" + toUserName + ", MsgType is:" + msgType);

            // 文本消息
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
                //这里根据关键字执行相应的逻辑，只有你想不到的，没有做不到的
                TextMessage text = new TextMessage();
                text.setToUserName(fromUserName);
                text.setFromUserName(toUserName);
                text.setCreateTime(System.currentTimeMillis() + "");
                text.setMsgType(msgType);

                TianQiCityID ci = new TianQiCityID();
                if(ci.getCityIDMap().get(content) !=null){
                    TianQiWeatherHelper tianQiWeatherHelper = new TianQiWeatherHelper();
                    tianQiWeatherHelper.setSimpleHttpClient(simpleHttpClient);
                    text.setContent(tianQiWeatherHelper.getWeatherReportByCityName(content));
                } else if ("天气".equals(content)) {
                    TianQiWeatherHelper tianQiWeatherHelper = new TianQiWeatherHelper();
                    tianQiWeatherHelper.setSimpleHttpClient(simpleHttpClient);
                    text.setContent(tianQiWeatherHelper.getWeatherReportByIP(WebUtil.getRemoteAddr(request)));
                } else if (DataUtil.strongValidateMobile(content)) {
                    text.setContent(mobileHelper.queryMobileInfo(content));
                } else if (content.startsWith("mp")) {
                    if (content.startsWith("mp+")) {
                        myPowerService.insert(content.substring(3));
                        text.setContent("添加成功");
                    } else if (content.startsWith("mp=")) {
                        text.setContent(myPowerService.fuzzyQuery(content.substring(3)));
                    } else {
                        text.setContent(myPowerService.getAllPower());
                    }
                } else {
                    // 兜底走 chatgpt
                    text.setContent(TextUtil.join(myOpenAiService.chat(content), "\\n"));
                }

                respMessage = MessageUtil.textMessageToXml(text);
                //自动回复
//                TextMessage text = new TextMessage();
//                text.setContent("the text is" + content);
//                text.setToUserName(fromUserName);
//                text.setFromUserName(toUserName);
//                text.setCreateTime(System.currentTimeMillis() + "");
//                text.setMsgType(msgType);
//                respMessage = MessageUtil.textMessageToXml(text);

            }
//            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {// 事件推送
//                String eventType = requestMap.get("Event");// 事件类型
//
//                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {// 订阅
//                    respContent = "欢迎关注xxx公众号！";
//                    return MessageResponse.getTextMessage(fromUserName , toUserName , respContent);
//                } else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {// 自定义菜单点击事件
//                    String eventKey = requestMap.get("EventKey");// 事件KEY值，与创建自定义菜单时指定的KEY值对应
//                    logger.info("eventKey is:" +eventKey);
//                    return xxx;
//                }
//            }
            //开启微信声音识别测试 2015-3-30
//            else if(msgType.equals("voice"))
//            {
//                String recvMessage = requestMap.get("Recognition");
//                //respContent = "收到的语音解析结果："+recvMessage;
//                if(recvMessage!=null){
//                    respContent = TulingApiProcess.getTulingResult(recvMessage);
//                }else{
//                    respContent = "您说的太模糊了，能不能重新说下呢？";
//                }
//                return MessageResponse.getTextMessage(fromUserName , toUserName , respContent);
//            }
            //拍照功能
//            else if(msgType.equals("pic_sysphoto"))
//            {
//
//            }
//            else
//            {
//                return MessageResponse.getTextMessage(fromUserName , toUserName , "返回为空");
//            }
            // 事件推送
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                String eventType = requestMap.get("Event");// 事件类型
                // 订阅
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {

                    TextMessage text = new TextMessage();
                    text.setContent("欢迎关注，xxx");
                    text.setToUserName(fromUserName);
                    text.setFromUserName(toUserName);
                    text.setCreateTime(System.currentTimeMillis() + "");
                    text.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);

                    respMessage = MessageUtil.textMessageToXml(text);
                }
                // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
                else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {// 取消订阅


                }
                // 自定义菜单点击事件
                else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
                    String eventKey = requestMap.get("EventKey");// 事件KEY值，与创建自定义菜单时指定的KEY值对应
                    if (eventKey.equals("weather")) {
                        TextMessage text = new TextMessage();
                        text.setContent("请输入城市名称");
                        text.setToUserName(fromUserName);
                        text.setFromUserName(toUserName);
                        text.setCreateTime(System.currentTimeMillis() + "");
                        text.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);

                        respMessage = MessageUtil.textMessageToXml(text);
                    }
                }
            }
        }
        catch (Exception e) {
            LOGGER.error("error......");
        }
        return respMessage;
    }

    @Override
    public String getAccessToken() {

        String access_token = accessToken.getString("access_token");
        if (StringUtils.isBlank(access_token)) {
            flushWechatAccessToken();
            return accessToken.getString("access_token");
        } else {
            // 判断是否过期
            if (accessToken.getLongValue("expires_in") <= System.currentTimeMillis()) {
                flushWechatAccessToken();
            }
            return accessToken.getString("access_token");
        }
    }

    private void flushWechatAccessToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token";
        NameValuePair[] data ={
                new BasicNameValuePair("grant_type","client_credential"),
                new BasicNameValuePair("appid",appleId),
                new BasicNameValuePair("secret",appSecret)
        };

        String result = null;
        try {
            result = simpleHttpClient.get(url, data);
        } catch (IOException e) {
        }
        if (StringUtils.isNotBlank(result)) {
            JSONObject jsonObject = JSON.parseObject(result);
            if (StringUtils.isNotBlank(jsonObject.getString("access_token"))) {
                // 刷新accessToken
                accessToken.put("access_token", jsonObject.getString("access_token"));
                accessToken.put("expires_in", System.currentTimeMillis() + jsonObject.getLong("expires_in")*1000);
            }
        }
    }
}
