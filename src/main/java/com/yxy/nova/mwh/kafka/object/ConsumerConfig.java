package com.yxy.nova.mwh.kafka.object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

/**
 * Created by xiazhen on 18/6/26.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsumerConfig {

    private String name;
    private List<String> owners;
    private int pollInterval = 100;
    private Map<String, SubscriberConfig> subscribed;
    private SubscriberConfig defaultOne = new SubscriberConfig();

    public SubscriberConfig getOne(final String topic, final boolean allowDefault) {
        if (subscribed == null || !subscribed.containsKey(topic)) {
            if (allowDefault) {
                return defaultOne;
            } else {
                return null;
            }
        }
        return subscribed.get(topic);
    }

    public SubscriberConfig getDefault() {
        final SubscriberConfig sc = new SubscriberConfig();
        return sc;
    }

    public int getPollInterval() {
        return pollInterval;
    }

    public void setPollInterval(int pollInterval) {
        this.pollInterval = pollInterval;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubscribed(Map<String, SubscriberConfig> subscribed) {
        this.subscribed = subscribed;
    }

    public List<String> getOwners() {
        return owners;
    }

    public void setOwners(List<String> owners) {
        this.owners = owners;
    }
}
