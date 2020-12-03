package com.yxy.nova.cmpp;

import com.yxy.nova.cmpp.pojo.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yuxiaoyu
 * @date 2020/12/3 上午10:35
 * @Description
 */
@Slf4j
public class SmsClientFactoryImpl implements SmsClientFactory {
    private static ConcurrentHashMap<Long, SmsClient> smsClientMap = new ConcurrentHashMap();
    private static ConcurrentHashMap<Long,SmsClient> connectClientMap=new ConcurrentHashMap<>();
    /**
     * 构建发送渠道map
     *
     * @param channelString 字符串类型的渠道配置
     */
    @Override
    public void buildClients(String channelString) {
        Channel channel = new Channel();
        //杭州机房不创建cmpp长连接防止 接收状态 然后双机房数据不一致导致问题
        log.info("ruidong channel build ");
        Map<String,String> configMap=new HashMap<>();
        String[] configInfos=channel.getName().split(",");
        //InfoX主机地址,与移动签合同时移动所提供的地址
        configMap.put("host",configInfos[0]);
        //InfoX主机端口号 cmpp2.0默认为7890,cmpp3.0为7891
        configMap.put("port",configInfos[1]);
        //(登录帐号SP…ID)与移动签合同时所提供的企业代码 6位
        configMap.put("source-addr",configInfos[2]);
        //登录密码 默认为空
        configMap.put("shared-secret",channel.getPassword());
        configMap.put("msgSrc",configInfos[3]);
        configMap.put("srcTerminalId",configInfos[4]);
        setCmccDefaultConfig(configMap);

        CmppSmsClient smsClient = new CmppSmsClient(configMap,channel.getId());
        smsClient.setSign(channel.getSign());
        smsClient.setSmsOperator(channel.getSmsOperator());
        smsClient.setUnsubscribeInfo(channel.getUnsubscribeInfo());
        smsClient.setChannelId(channel.getId());
        smsClient.setSmsErrorCodeService(null);
        smsClient.setStatus(channel.getStatus());
        smsClient.setReplyFlag(channel.isReplyFlag());
        smsClient.setReportFlag(channel.isReportFlag());
        smsClient.setWorkingEndTime(channel.getWorkingEndTime());
        smsClient.setWorkingStartTime(channel.getWorkingStartTime());
        connectClientMap.put(channel.getId(),smsClient);
        smsClientMap.put(channel.getId(), smsClient);
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
     * 选举发送渠道
     *
     * @param smsParam 发送参数
     * @return 可用的渠道；否则null
     */
    @Override
//    public Channel chooseChannel(SmsParam smsParam) {
    public Channel chooseChannel(String smsParam) {
        return null;
    }

    /**
     * 生成发送实例
     *
     * @param channelId 渠道ID
     * @return 可用的渠道
     */
    @Override
    public SmsClient produce(Long channelId) {
        return null;
    }

    /**
     * 获取所有发送端
     *
     * @return
     */
    @Override
    public List<SmsClient> allClient() {
        return null;
    }

    /**
     * 获取config下面所有通道
     *
     * @param configId
     * @return
     */
    @Override
    public List<Channel> getChannelByConfigId(List<Long> configId) {
        return null;
    }

    @Override
    public Channel getChannel(Long channelId) {
        return null;
    }

    /**
     * 获取所有通道数量
     */
    @Override
    public List<Channel> allChannel() {
        return null;
    }
}
