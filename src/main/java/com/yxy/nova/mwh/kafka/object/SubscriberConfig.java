package com.yxy.nova.mwh.kafka.object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.Properties;

/**
 * Created by xiazhen on 18/6/28.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriberConfig extends HashMap<String, Object> {

    public SubscriberConfig() {
        put("max.poll.records", 500);
        put("max.partition.fetch.bytes", 5 * 1024 * 1024);
        // concurrentConsumer
        put("cc", 1);
    }

    public void mergeTo(final Properties props) {
        for (Entry<String, Object> e : this.entrySet()) {
            props.put(e.getKey(), e.getValue());
        }
    }
}
