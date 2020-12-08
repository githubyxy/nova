package com.yxy.nova.cmpp;


import com.yxy.nova.cmpp.pojo.SmsReplyResult;
import com.yxy.nova.cmpp.pojo.SmsReportResult;
import com.yxy.nova.cmpp.pojo.SmsSendResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by lqq on 2017/7/17.
 */
public interface SmsClient {

    SmsSendResult send(String mobile, String content);

    SmsSendResult send(String mobile, String content, String sendMark);

    List<SmsReportResult> report();

    List<SmsReplyResult> reply();

    String getSmsOperator();

    Map<String, Object> reportCallback(HttpServletRequest request);
    Map<String, Object> replyCallback(HttpServletRequest request);

    String queryBalance();

    /**
     * 支持哪些运营商
     * @return
     */
    List<String> getSuportOper();

    /**
     * 是否要将状态结果返回上级服务
     * @return
     */
    boolean isReportFlag();

    /**
     * 是否要将上行结果返回上级服务
     * @return
     */
    boolean isReplyFlag();

    long getChannelId();
    int getStatus();

}
