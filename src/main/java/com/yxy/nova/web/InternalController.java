package com.yxy.nova.web;

/**
 * Created by shenjing on 19/5/20.
 */

import com.alibaba.fastjson.JSON;
import com.yxy.nova.cmpp.CmppSmsClient;
import com.yxy.nova.cmpp.pojo.SmsSendResult;
import com.yxy.nova.service.wechat.WechatService;
import com.yxy.nova.util.SignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value="/innerapi")
public class InternalController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final String DNBX_TOKEN = "wechatabsd13i1dbdwomio95cozx1";

    @Autowired
    private WechatService wechatService;

    @GetMapping("/testcmpp")
    @ResponseBody
    public String testcmpp(@RequestParam("level") String level) throws Exception {
        Map<String,String> configMap=new HashMap<>();
        //InfoX主机地址,与移动签合同时移动所提供的地址
        configMap.put("host","8.129.229.252");
        configMap.put("local-host","120.48.8.74");
        //InfoX主机端口号 cmpp2.0默认为7890,cmpp3.0为7891
        configMap.put("port","7890");
        configMap.put("local-port","8080");
        //(登录帐号SP…ID)与移动签合同时所提供的企业代码 6位
        configMap.put("source-addr","30001");
        //登录密码 默认为空
        configMap.put("shared-secret","Fgjhjk");
        configMap.put("msgSrc","30001");
        configMap.put("srcTerminalId","01"); //Src_Id
        setCmccDefaultConfig(configMap);


        CmppSmsClient smsClient = new CmppSmsClient(configMap, 1L);
        smsClient.setSign("【360保险】");
        smsClient.setSmsOperator("CMCC");
        smsClient.setUnsubscribeInfo("");
        smsClient.setChannelId(1L);
//        smsClient.setSmsErrorCodeService(null);
        smsClient.setStatus(1);
        smsClient.setReplyFlag(false);
        smsClient.setReportFlag(false);
        smsClient.setWorkingEndTime("channel.getWorkingEndTime()");
        smsClient.setWorkingStartTime("channel.getWorkingStartTime()");

        SmsSendResult send = smsClient.send("13585934620", "用户phoneNum，感谢您接听，您600万保额医疗险已于今日到达，快戳 2dlj.cn/hm 领取 退回T");

        return JSON.toJSONString(send);
    }

    private void setCmccDefaultConfig(Map<String, String> configMap) {
        //心跳信息发送间隔时间(单位：秒)
        configMap.put("heartbeat-interval","2");
        //连接中断时重连间隔时间(单位：秒)
        configMap.put("reconnect-interval","2");
        //需要重连时，连续发出心跳而没有接收到响应的个数（单位：个)
        configMap.put("heartbeat-noresponseout","3");
        //操作超时时间(单位：秒)
        configMap.put("transaction-timeout","5");
        //双方协商的版本号(大于0，小于256)
        configMap.put("version","1");
        //是否属于调试状态,true表示属于调试状态，所有的消息被打印输出到屏幕，false表示不属于调试状态，所有的消息不被输出
        configMap.put("debug","false");
    }

    /**
     * 微信接入
     * @param
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/wechat/connect",method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public void connectWeixin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
        request.setCharacterEncoding("UTF-8");  //微信服务器POST消息时用的是UTF-8编码，在接收时也要用同样的编码，否则中文会乱码；
        response.setCharacterEncoding("UTF-8"); //在响应消息（回复消息给用户）时，也将编码方式设置为UTF-8，原理同上；boolean isGet = request.getMethod().toLowerCase().equals("get");

        PrintWriter out = response.getWriter();

        try {
//            boolean isGet = request.getMethod().toLowerCase().equals("get");
            if (RequestMethod.GET.name().equals(request.getMethod())) {
                String signature = request.getParameter("signature");// 微信加密签名
                String timestamp = request.getParameter("timestamp");// 时间戳
                String nonce = request.getParameter("nonce");// 随机数
                String echostr = request.getParameter("echostr");//随机字符串

                // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
                if (SignUtil.checkSignature(DNBX_TOKEN, signature, timestamp, nonce)) {
                    LOGGER.info("Connect the weixin server is successful.");
                    response.getWriter().write(echostr);
                } else {
                    LOGGER.error("Failed to verify the signature!");
                }
            }else{
                String respMessage = "异常消息！";

                try {
                    respMessage = wechatService.weixinPost(request);
                    out.write(respMessage);
                    LOGGER.info("The request completed successfully");
                    LOGGER.info("to weixin server "+respMessage);
                } catch (Exception e) {
                    LOGGER.error("Failed to convert the message from weixin!");
                }

            }
        } catch (Exception e) {
            LOGGER.error("Connect the weixin server is error.");
        }finally{
            out.close();
        }
}


}
