package test.kafka;

import com.alibaba.fastjson.JSONObject;
import com.yxy.nova.mwh.kafka.consumer.IConsumer;
import com.yxy.nova.mwh.utils.log.TraceIdUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.MDC;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created with IntelliJ IDEA.
 *
 * @description:
 * @author: renshui
 * @date: 2020-02-20 4:02 下午
 */
public class ConsumerWithMonitor implements IConsumer {

    @Override
    public void doConsume(List<ConsumerRecord<String, byte[]>> list) {
        try {
            System.out.println("ConsumerWithMonitor");
            List<String> messageList = list.stream()
                    .map(str -> {
                        try {
                            return new String(str.value(), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(toList());
            System.out.println("ConsumerWithMonitor" + JSONObject.toJSONString(messageList));
        } catch (Exception e) {
            throw e;
        }
    }

}
