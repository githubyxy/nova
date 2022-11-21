package com.yxy.nova.mwh.retry.scheduler;

import com.yxy.nova.mwh.retry.api.event.EventTypeEnum;
import com.yxy.nova.mwh.retry.api.event.RetryTaskEvent;
import com.yxy.nova.mwh.retry.api.event.RetryTaskEventListener;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RetryTaskEventDispatcher {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Map<EventTypeEnum, List<RetryTaskEventListener>> typedListeners = new HashMap<>();


    public void addEventListener(EventTypeEnum eventType, RetryTaskEventListener listenerToAdd) {
        List<RetryTaskEventListener> listeners = typedListeners.get(eventType);

        if (listeners == null) {
            listeners = new ArrayList<>();
            typedListeners.put(eventType, listeners);
        }

        listeners.add(listenerToAdd);
    }

    public void dispatchEvent(RetryTaskEvent event) {
        List<RetryTaskEventListener> typed = typedListeners.get(event.getEventType());

        if (CollectionUtils.isEmpty(typed)) {
            return;
        }

        for (RetryTaskEventListener listener : typed) {
            dispatchEvent(event, listener);
        }
    }

    public void dispatchEvent(RetryTaskEvent event, RetryTaskEventListener listener) {
        try {
            listener.onEvent(event);
        } catch (Throwable t) {
            logger.error("event listener处理异常", t);
        }
    }
}
