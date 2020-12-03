package com.yxy.nova.cmpp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LongMessageByte {
    public static List<byte[]> getLongByte(String message) {
        List<byte[]> list = new ArrayList<byte[]>();
        try {
            byte[] messageUCS2 = message.getBytes("UnicodeBigUnmarked");//转换为byte[]
            int messageUCS2Len = messageUCS2.length;// 长短信长度
            int maxMessageLen = 140;
            if (messageUCS2Len > maxMessageLen) {// 长短信发送
                //int tpUdhi = 1;
                //长消息是1.短消息是0
                //int msgFmt = 0x08;//长消息不能用GBK
                int messageUCS2Count = messageUCS2Len / (maxMessageLen - 6) + 1;// 长短信分为多少条发送
                byte[] tp_udhiHead = new byte[6];
                Random random = new Random();
                random.nextBytes(tp_udhiHead);//随机填充tp_udhiHead[3] 标识这批短信
                tp_udhiHead[0] = 0x05;
                tp_udhiHead[1] = 0x00;
                tp_udhiHead[2] = 0x03;
//				tp_udhiHead[3] = 0x0A;
                tp_udhiHead[4] = (byte) messageUCS2Count;
                tp_udhiHead[5] = 0x01;// 默认为第一条
                for (int i = 0; i < messageUCS2Count; i++) {
                    tp_udhiHead[5] = (byte) (i + 1);
                    byte[] msgContent;
                    if (i != messageUCS2Count - 1) {// 不为最后一条
                        msgContent = byteAdd(tp_udhiHead, messageUCS2, i * (maxMessageLen - 6), (i + 1) * (maxMessageLen - 6));
                        list.add(msgContent);
                    } else {
                        msgContent = byteAdd(tp_udhiHead, messageUCS2, i * (maxMessageLen - 6), messageUCS2Len);
                        list.add(msgContent);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private static byte[] byteAdd(byte[] tpUdhiHead, byte[] messageUCS2, int i, int j) {
        byte[] msgb = new byte[j - i + 6];
        System.arraycopy(tpUdhiHead, 0, msgb, 0, 6);
        System.arraycopy(messageUCS2, i, msgb, 6, j - i);
        return msgb;
    }


}