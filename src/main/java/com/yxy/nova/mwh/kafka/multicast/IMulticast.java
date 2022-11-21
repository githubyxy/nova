package com.yxy.nova.mwh.kafka.multicast;

import com.yxy.nova.mwh.kafka.object.ProducerException;

import java.io.Closeable;

/**
 * 广播接口
 *
 * 其中channel和topic由管理员统一分配
 *
 * Created by xiazhen on 18/7/2.
 */
public interface IMulticast extends Closeable {

    /**
     * 同步发送广播,阻塞直到服务器返回成功
     */
    void multicast(final String message) throws ProducerException;

    /**
     * 设置业务独享的频道号
     */
    void setChannel(int channel);

    /**
     * 设置topic
     */
    void setTopic(String topic);
}
