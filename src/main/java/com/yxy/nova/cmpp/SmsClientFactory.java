package com.yxy.nova.cmpp;


import com.yxy.nova.cmpp.pojo.Channel;

import java.util.List;

/**
 * Created by erqing.zhang on 2017/7/20.
 */
public interface SmsClientFactory {

    /**
     * 构建发送渠道map
     *
     * @param channelString 字符串类型的渠道配置
     */
    void buildClients(String channelString);

    /**
     * 选举发送渠道
     *
     * @param smsParam 发送参数
     * @return 可用的渠道；否则null
     */
    Channel chooseChannel(String smsParam);

    /**
     * 生成发送实例
     *
     * @param channelId 渠道ID
     * @return 可用的渠道
     */
    SmsClient produce(Long channelId);

    /**
     * 获取所有发送端
     *
     * @return
     */
    List<SmsClient> allClient();

    /**
     * 获取config下面所有通道
     * @param configId
     * @return
     */
    List<Channel> getChannelByConfigId(List<Long> configId);

    Channel getChannel(Long channelId);

    /**
     * 获取所有通道数量
     */
    List<Channel>  allChannel();
}
