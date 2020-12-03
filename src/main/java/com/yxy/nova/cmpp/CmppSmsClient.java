package com.yxy.nova.cmpp;

import com.yxy.nova.cmpp.pojo.SmsReplyResult;
import com.yxy.nova.cmpp.pojo.SmsReportResult;
import com.yxy.nova.cmpp.pojo.SmsSendResult;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

/**
 * 睿动短信通道
 *
 * @author tianlong.li
 */
public class CmppSmsClient implements SmsClient {

    protected static final String CHARSET            = "UTF-8";
    protected static final int    CONNECTION_TIMEOUT = 1000;
    protected static final int    READ_TIMEOUT       = 5000;
    private static final int      MAX_CONNECTION     = 500;
    private static final int      SLEEP_TIME         = 1000;


    protected String              environment;
    /**
     * 通道ID
     */
    private long                  channelId;
    /**
     * 第三方短信服务商名称
     */
    private String                smsOperator;
    /**
     * 退订提醒
     */
    private String unsubscribeInfo;
    /**
     * 签名
     */
    private String sign;

    private List<String>  suportOper = new ArrayList<>();

    private boolean reportFlag = false;
    private boolean replyFlag = false;

    private String workingStartTime;
    private String workingEndTime;
    /**
     * 渠道状态
     */
    private int status;

//    protected SmsErrorCodeService smsErrorCodeService;
    private static final Logger log = LoggerFactory.getLogger(CmppSmsClient.class);
    public SmsProxy smsProxy;


    public CmppSmsClient(Map configMap, long channelId){
        smsProxy=new SmsProxy(configMap,channelId);

    }


    @Override
    public SmsSendResult send(String mobile, String content) {
        return send(mobile, content, null);
    }

    @Override
    public SmsSendResult send(String mobile, String content, String sendMark) {
        content=buildMessage(content);
        Map<String, Object> param = Maps.newHashMap();
        int count =countSmsSplit(content);
        Map<String,String> resMap=null;
        if (count!=1){
            resMap=smsProxy.longMessageSend(smsProxy,content,mobile,sendMark);
        }else {
            resMap=smsProxy.simpleMessageSend(smsProxy,content,mobile,sendMark);
        }

        if (resMap != null) {
            SmsSendResult sendResult = new SmsSendResult();
            sendResult.setSmsOperator(getSmsOperator());
            sendResult.setContent(buildMessage(content));
            sendResult.setMobile(mobile);
            sendResult.setSendMark(sendMark);
            sendResult.setChannelId(getChannelId());
            sendResult.setSendTime(new Timestamp(System.currentTimeMillis()));
            if (!resMap.get("result").equals("0")) {
                log.info("短信发送失败{}", resMap);
                sendResult.setSendFlag(false);
                sendResult.setMsgId(UUID.randomUUID().toString().replace("-", ""));
                sendResult.setErrorDesc(resMap.get("error"));
            } else {
                if (resMap.get("msgId") != null) {
                    sendResult.setSendFlag(true);
                    sendResult.setMsgId(resMap.get("msgId"));
                } else {
                    return null;
                }
            }
            return sendResult;
        }
        return null;
    }

    public String buildMessage(String content) {
        String message = sign + content;
        if (StringUtils.isNotEmpty(unsubscribeInfo)) {
            message = message + unsubscribeInfo;
        }
        return message;
    }

    @Override
    public List<SmsReportResult> report() {
        return new ArrayList<>(0);
    }

    @Override
    public List<SmsReplyResult> reply() {
        return new ArrayList<>(0);
    }

    @Override
    public String getSmsOperator() {
        return null;
    }

    @Override
    public Map<String, Object> reportCallback(HttpServletRequest request) {
        return new HashMap<>(0);
    }

    @Override
    public Map<String, Object> replyCallback(HttpServletRequest request) {
        return new HashMap<>(0);
    }

    @Override
    public String queryBalance() {
        Map<String, Object> param = Maps.newHashMap();
//        param.put("accesskey", name);
//        param.put("secret", password);
//        String balanceRes = null;
//        try {
//            balanceRes = httpClient.doPost(BALANCE_URL, param, CHARSET, CHARSET, null);
//        } catch (Exception e) {
//            log.error("get balance error, url:" + BALANCE_URL + " params:" + JSONObject.toJSONString(param), e);
//            balanceRes = redirectPost(BALANCE_URL, param);
//        }
//        JSONObject jsonObject = JSON.parseObject(balanceRes);
//        if (!jsonObject.get("code").equals("0")) {
//            return "-1(条)";
//        }
//        JSONObject object = JSON.parseObject(jsonObject.get("data").toString());
//        return object.get("营销短信") == null ? "-1(条)" : jsonObject.get("营销短信") + "(条)";
        return "-1(条)";
    }

    /**
     * 支持哪些运营商
     *
     * @return
     */
    @Override
    public List<String> getSuportOper() {
        return null;
    }

    /**
     * 是否要将状态结果返回上级服务
     *
     * @return
     */
    @Override
    public boolean isReportFlag() {
        return false;
    }

    /**
     * 是否要将上行结果返回上级服务
     *
     * @return
     */
    @Override
    public boolean isReplyFlag() {
        return false;
    }

    @Override
    public long getChannelId() {
        return 0;
    }

    @Override
    public int getStatus() {
        return 0;
    }

    /**
     * 短信拆分条数
     *
     * @param content
     * @return
     */
    private int countSmsSplit(String content) {
        if (StringUtils.isEmpty(content)) {
            return 0;
        }

        int size = content.length();
        if (size <= 70) {
            return 1;
        } else if (size >= 350) {
            return -1;
        } else {
            return size % 67 == 0 ? size / 67 : (size / 67 + 1);
        }
    }


    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setSmsOperator(String smsOperator) {
        this.smsOperator = smsOperator;
    }

    public void setReportFlag(boolean reportFlag) {
        this.reportFlag = reportFlag;
    }

    public void setReplyFlag(boolean replyFlag) {
        this.replyFlag = replyFlag;
    }

    public void setWorkingStartTime(String workingStartTime) {
        this.workingStartTime = workingStartTime;
    }

    public void setWorkingEndTime(String workingEndTime) {
        this.workingEndTime = workingEndTime;
    }

    public void setUnsubscribeInfo(String unsubscribeInfo) {
        this.unsubscribeInfo = unsubscribeInfo;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

//    public void setStatus(int status) {
//        this.status = status;
//    }
//    public SmsErrorCodeService getSmsErrorCodeService() {
//        return smsErrorCodeService;
//    }
//
//    public void setSmsErrorCodeService(SmsErrorCodeService smsErrorCodeService) {
//        this.smsErrorCodeService = smsErrorCodeService;
//    }
}
