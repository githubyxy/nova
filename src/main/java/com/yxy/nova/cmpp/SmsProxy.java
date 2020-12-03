package com.yxy.nova.cmpp;

import com.yxy.nova.cmpp.common.Args;
import com.yxy.nova.cmpp.common.PException;
import com.yxy.nova.cmpp.message.*;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SmsProxy extends SMProxy {
    private static final Logger log = LoggerFactory.getLogger(SmsProxy.class);
    private Map<String,String> configMap;
    public SmsProxy(Args args) {
        super(args);
    }
    public SmsProxy(Map args, long channelId) {
        super(args);
        configMap=args;
        this.channelId=channelId;
    }
    private long channelId  ;
    public static void main(String[] args) throws Exception {

//        String path = Thread.currentThread().getContextClassLoader().getResource("cmpp.xml").getPath().toString();
//        Args args1 = new Cfg(path).getArgs("ismg");
//        //SmsProxy smProxy = new SmsProxy(args1);
//
//
//        //simpleMessageSend(smProxy);
//
//        Thread.sleep(100000000);
    }

    public  Map<String,String> simpleMessageSend(SmsProxy smProxy,String content,String mobile,String  sendMark) {
        try{
        String[] dest_Terminal_Id = {mobile};
        byte[] msg_Content = content.getBytes("UnicodeBigUnmarked");
        CMPPSubmitMessage submitMsg = new CMPPSubmitMessage(
                1,//@pk_Total 相同msg_Id消息总条数,短短信这里是1
                1,//@pk_Number 相同msg_Id的消息序号
                1,//@registered_Delivery 是否要求返回状态报告
                1,//@msg_Level  信息级别
                "",// @service_Id 业务类型 用户自定义 用来分类查询
                2,//@fee_UserType 0对目的终端计费；1对源终端计费；2对SP计费;
                "",//@fee_Terminal_Id 被计费用户的号码
                0,//@tp_Pid GSM协议类型 一般文本的时候设0,铃声图片设1
                0,//@tp_Udhi GSM协议类型 0不包含头部信息 1包含头部信息
                8,//@msg_Fmt 消息格式
                configMap.get("msgSrc"),//@msg_Src 消息内容来源 6位的企业代码,这里需修改
                "02",// @fee_Type 资费类别 一般为02：按条计信息费
                "0",//@fee_Code 资费代码(以分为单位)
                null,//@valid_Time 存活有效期
                null,//@at_Time 定时发送时间
                configMap.get("srcTerminalId")+sendMark,//@src_Terminal_Id 移动所提供的服务代码  此处需修改
                dest_Terminal_Id,//@dest_Terminal_Id 接收业务的MSISDN号码,就是接收短信的手机号,String数组
                msg_Content,//@msg_Content 消息内容 byte[],发送的消息内容,需要转化为byte[]
                "" //预留
        );

        CMPPSubmitRepMessage sub = (CMPPSubmitRepMessage) smProxy.send(submitMsg);//这里的smProxy就是第2点中用单例创建的smProxy对象
        Map<String,String> returnMap=new HashMap<>();
            if (sub.getResult() == 0) {
                //本条发送成功
                returnMap.put("result",0+"");
                String rawMsgId=bytesToHexStr(sub.getMsgId());
                //因为cmpp如果拆分成3条会返回3条回复  可能存在拆分后丢失  但是如果拆分成3条短信我们只记录一条 msgId 只能吧把最后两位截调加手机号可以基本保证短信能更新到
                log.info("submit rawMsgId:{}",rawMsgId);
                String msgId=rawMsgId.substring(0,rawMsgId.length()-3);
                returnMap.put("msgId",msgId+mobile);

            }else {
                returnMap.put("result",sub.getResult()+"");
                returnMap.put("error",sub.toString());
            }
        return returnMap ;
        }catch (PException pe){
            log.error("cmpp error channelId:{}",channelId);
            log.error("cmppp connection error :",pe);
            //重新build 短信通道  再重新build的时候回重连 最好能单个通道build  但是bugfix 时间比较紧没想到啥好的办法
            rebuildChannels();
            try {
                //等待6秒  如果断时间的网络连接失败  直接重发也不会成功 3个心跳检测时间 每个检测时间2s
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                log.error("InterruptedException :",e);
            }
            //simpleMessageSend(this,content,mobile,sendMark);
            return null;
        }catch (Exception e){
            log.error("send message error :",e);
            return null;
        }
    }

    private void rebuildChannels() {
        WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        SmsClientFactory smsClientFactory= (SmsClientFactory) context.getBean("smsClientFactory");
//        ChannelsConfig channelsConfig= (ChannelsConfig) context.getBean("channelsConfig");
        // TODO cmpp 配置
        smsClientFactory.buildClients("");
    }

    public   Map<String,String> longMessageSend(SmsProxy smProxy,String content,String mobile,String  sendMark )  {
        try{
            String[] dest_Terminal_Id = {mobile};
            List<byte[]> list = LongMessageByte.getLongByte(content);
            //拆分成多条以后 只需要最后一条的messageId  因为正常情况下message拆分成多条后的状态应该是一致的
            Map<String,String> returnMap=new HashMap<>();
            for (int i = 0; i < list.size(); i++) {
                CMPPSubmitMessage submitMsg = new CMPPSubmitMessage(
                        list.size(),//@pk_Total 相同msg_Id消息总条数
                        i + 1,//@pk_Number 相同msg_Id的消息序号
                        1,//@registered_Delivery 是否要求返回状态报告
                        1,//@msg_Level  信息级别
                        "",// @service_Id 业务类型 用户自定义 用来分类查询
                        2,//@fee_UserType 0对目的终端计费；1对源终端计费；2对SP计费;
                        "",//@fee_Terminal_Id 被计费用户的号码
                        0,//@tp_Pid GSM协议类型 一般文本的时候设0,铃声图片设1
                        1,//@tp_Udhi  0不包含头部信息 1包含头部信息 必须为1
                        8,//@msg_Fmt 消息格式 设为UCS2编码
                        configMap.get("msgSrc"),//@msg_Src 消息内容来源 6位的企业代码
                        "02",// @fee_Type 资费类别 一般为02：按条计信息费
                        "0",//@fee_Code 资费代码(以分为单位)
                        null,//@valid_Time 存活有效期
                        null,//@at_Time 定时发送时间
                        configMap.get("srcTerminalId")+sendMark,//@src_Terminal_Id 移动所提供的服务代码 此处需修改
                        dest_Terminal_Id,//@dest_Terminal_Id 接收业务的MSISDN号码
                        list.get(i),//@msg_Content 消息内容 byte[]
                        "" //预留   3.0这里是String LinkID,点播业务使用的LinkID，非点播类业务的MT流程不使用该字段。
                );

                CMPPSubmitRepMessage sub = (CMPPSubmitRepMessage) smProxy.send(submitMsg);//这里的smProxy就是第2点中用单例创建的smProxy对象
                log.info("CMPPSubmitRepMessage:{}", JSON.toJSONString(sub));
                if (sub.getResult() == 0) {
                    //本条发送成功
                    returnMap.put("result",0+"");
                    String rawMsgId=bytesToHexStr(sub.getMsgId());
                    //因为cmpp如果拆分成3条会返回3条回复  可能存在拆分后丢失  但是如果拆分成3条短信我们只记录一条 msgId 只能吧把最后两位截调加手机号可以基本保证短信能更新到
                    log.info("submit rawMsgId:{}",rawMsgId);
                    String msgId=rawMsgId.substring(0,rawMsgId.length()-3);
                    returnMap.put("msgId",msgId+mobile);

                }else {
                    returnMap.put("result",sub.getResult()+"");
                    returnMap.put("error",sub.toString());
                }

            }
            return returnMap;
        }catch (PException pe){
            log.error("cmpp error channelId:{}",channelId);
            log.error("cmppp connection error :",pe);
            //重新build 短信通道  再重新build的时候回重连 最好能单个通道build  但是bugfix 时间比较紧没想到啥好的办法
            rebuildChannels();
            try {
                //等待6秒  如果断时间的网络连接失败  直接重发也不会成功 3个心跳检测时间 每个检测时间2s
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                log.error("cmpp InterruptedException channelId:{},mobile:{}",channelId,mobile);
                log.error("InterruptedException :",e);
            }
            //longMessageSend(this,content,mobile,sendMark);
            return null;
        }catch (Exception e){
            log.error("send message error channelId:{},mobile:{}",channelId,mobile);
            log.error("send message error :",e);
            return null;
        }

    }

    private static String bytesToHexStr(byte[] b)
    {
        if (b == null) return "";
        StringBuffer strBuffer = new StringBuffer(b.length * 3);
        for (int i = 0; i < b.length; i++)
        {
            strBuffer.append(Integer.toHexString(b[i] & 0xff));
        }
        return strBuffer.toString();
    }


    @Override
    public CMPPMessage onDeliver(CMPPDeliverMessage msg) {
        return new CMPPDeliverRepMessage(msg.getMsgId(), 0);
//        try {
//        ApplicationContext context = null;
//        SendRecordService sendRecordService= (SendRecordService) context.getBean("sendRecordService");
//        //判断是返回状态还是上行
//        if (msg.getRegisteredDeliver()==1){
//            SmsReportResult reportResult = new SmsReportResult();
//                reportResult.setChannelId(getChannelId());
//                reportResult.setReportTime(new Timestamp(System.currentTimeMillis()));
//                String rawMsgId=bytesToHexStr(msg.getStatusMsgId());
//                log.info("report rawMsgId:{}",rawMsgId);
//                //因为cmpp如果拆分成3条会返回3条回复  可能存在拆分后丢失  但是如果拆分成3条短信我们只记录一条 msgId 只能吧把最后两位截调加手机号可以基本保证短信能更新到
//                String msgId=rawMsgId.substring(0,rawMsgId.length()-3);
//                reportResult.setMsgId(msgId+msg.getSrcterminalId());
//                reportResult.setMobile(msg.getSrcterminalId());
//                if (msg.getStat().equals("DELIVRD")) {
//                    reportResult.setReportFlag(Boolean.TRUE);
//                } else {
//                    reportResult.setReportFlag(Boolean.FALSE);
//                    reportResult.setErrorCode(msg.getStat());
//                    reportResult.setErrorDesc(msg.toString());
//                }
//            sendRecordService.updateForReport(reportResult);
//        }else if(msg.getRegisteredDeliver()==0){
//                SmsReplyResult replyResult = new SmsReplyResult();
//                replyResult.setMobile(msg.getSrcterminalId());
//                replyResult.setMoId(UUID.randomUUID().toString().replace("-", ""));
//                try {
//                    if (msg.getMsgFmt()==0){
//                        replyResult.setContent(new String(msg.getMsgContent(),"utf-8"));
//                    }else {
//                        replyResult.setContent(new String(msg.getMsgContent(),"utf-16"));
//                    }
//
//                } catch (UnsupportedEncodingException e) {
//                    log.error("url decode mobile:{},channelId:{}:",msg.getSrcterminalId(),getChannelId());
//                    log.error("url decode error", e);
//                }
//                replyResult.setReplyTime(new Timestamp(System.currentTimeMillis()));
//                replyResult.setChannelId(getChannelId());
//                String dstphone = msg.getDestnationId();
//                if(dstphone != null && dstphone.contains(configMap.get("srcTerminalId"))) {
//                    replyResult.setSendMark(dstphone.substring(configMap.get("srcTerminalId").length()));
//                    log.info("send mark {}  send mark equal ''",replyResult.getSendMark(),replyResult.getSendMark().equals(""));
//                }
//
//
//            sendRecordService.updateForReply(replyResult);
//        }
//
//        return new CMPPDeliverRepMessage(msg.getMsgId(), 0);
//        }catch (Exception e){
//            log.error("cmpp ondeliver mobile:{},channelId:{}:",msg.getSrcterminalId(),getChannelId());
//            log.error("cmpp ondeliver error:",e);
//            return new CMPPDeliverRepMessage(msg.getMsgId(), 0);
//        }
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }
}
