package com.yxy.nova.socket;

import com.google.common.cache.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Closeable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.*;

/**
 * @author maming.zhong
 * @date 2019-09-18 14:20
 * @description
 */
@Component
public class WebSocketRedisSubscriber extends MessageListenerAdapter implements Closeable {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String DESTINATION_MESSAGE = "/queue/sendUser";
    private static final CopyOnWriteArraySet<String> channelSet = new CopyOnWriteArraySet();
    @Resource(name = "webSocketRedisTemplate")
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RedisMessageListenerContainer webSocketMessageListenerContainer;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void close() {
        logger.info("<====== WebSocketRedisSubscriber close ======>");
        bizIdCache.invalidateAll();
        //移除所有订阅会话
        webSocketMessageListenerContainer.removeMessageListener(this);
        channelSet.clear();
        //等待任务处理完成
        for (final ExecutorService es : executorCache.asMap().values()) {
            try {
                es.shutdown();
                es.awaitTermination(1, TimeUnit.MINUTES);
            } catch (Exception e) {
                logger.error("failed to close executors", e);
            }
        }
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String channel = redisTemplate.getStringSerializer().deserialize(message.getChannel());
            String bizId = channel;
            ExecutorService executorService = executorCache.get(bizId);
            executorService.submit(() -> {
                try {
                    MDC.put("bizId", bizId);
                    String msg = redisTemplate.getStringSerializer().deserialize(message.getBody());
                    logger.info("------> redisSubscriber topic {} message {}", bizId, msg);
                    process(bizId, bizId + "$" + formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + "$" + msg);
                } catch (Exception e) {
                    logger.error("process interaction error", e);
                } finally {
                    MDC.clear();
                }
            });
        } catch (ExecutionException e) {
            logger.error("get executor from cache error", e);
        }
    }

    /**
     * 格式化日期成具体格式字符串
     * @param date
     * @param formatter
     * @return
     */
    private String formatDate(Date date, String formatter) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatter);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        String format = localDateTime.format(dateTimeFormatter);
        return format;
    }

    private void process(String bizId, String message) {

        convertAndSendToUser(bizId, message);
    }

    public void convertAndSendToUser(String userUuid, String message) {
        logger.info("<------ convertAndSendToUser Message {} {}", userUuid, message);
        messagingTemplate.convertAndSendToUser(userUuid, DESTINATION_MESSAGE, message);
    }

    public void subscribeBizId(String bizId) {
        logger.info("<====== subscribeBizId {} ======>", bizId);
        String channel = bizId;
        if(!channelSet.contains(channel)){
            channelSet.add(channel);
            bizIdCache.put(bizId.hashCode(), bizId);
            logger.info("<====== addMessageListener {} ======>", channel);
            webSocketMessageListenerContainer.addMessageListener(this, new ChannelTopic(channel));
        }
    }

    public void unsubscribeBizId(String bizId) {
        logger.info("<====== unsubscribeBizId {} ======>", bizId);
        String channel = bizId;
        if(channelSet.contains(channel)){
            channelSet.remove(channel);
            bizIdCache.invalidate(bizId.hashCode());
            logger.info("<====== removeMessageListener {} ======>", channel);
            webSocketMessageListenerContainer.removeMessageListener(this, new ChannelTopic(channel));
        }
    }

//    private void destroyTextInteractionSession(TrainingItemDO trainingItem) {
////        try {
////            TrainingStopRequest request = new TrainingStopRequest();
////            request.setBizId(trainingItem.getBizId());
////            request.setProd(trainingItem.getIsProd());
////            request.setTrainingType(trainingItem.getTrainingType());
////            trainingApplicationService.destroyTextInteractionSession(request);
////        } catch (Exception e) {
////            logger.warn("destroyTextInteractionSession error", e);
////        }
////    }

    private final LoadingCache<String, ExecutorService> executorCache = CacheBuilder.newBuilder()
            //设置并发级别为8，并发级别是指可以同时写缓存的线程数
            .concurrencyLevel(Runtime.getRuntime().availableProcessors())
            //设置缓存容器的初始容量为10
            .initialCapacity(10)
            //设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
            .maximumSize(100)
            //设置写缓存后n秒钟过期
            .expireAfterWrite(10*60, TimeUnit.SECONDS)
            //设置缓存的移除通知
            .removalListener(RemovalListeners.asynchronous(notification -> {
                logger.warn("executorCache {} {} removed, cause {}", notification.getKey(), notification.getValue(), notification.getCause());
            }, Executors.newSingleThreadExecutor()))
            //build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
            .build(new CacheLoader<String, ExecutorService>() {
                @Override
                public ExecutorService load(String key) {
                    return newSingleCachedThreadPool();
                }
            });

    private ExecutorService newSingleCachedThreadPool() {
        final ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 1,
                6 * 60 , TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>());
        pool.allowCoreThreadTimeOut(true);
        return pool;
    }

    /**
     * 缓存外呼批次策略
     */
    private Cache<Integer, String> bizIdCache = CacheBuilder.newBuilder()
            .concurrencyLevel(Runtime.getRuntime().availableProcessors())
            .maximumSize(1000)
            .expireAfterWrite(30*60, TimeUnit.SECONDS)
            .removalListener(RemovalListeners.asynchronous(notification -> {
                logger.warn(">>>>>> unsubscribeBizId {} bizIdCache {} removed, cause {}", notification.getValue(), notification.getKey(), notification.getCause());
                unsubscribeBizId(String.valueOf(notification.getValue()));
            }, Executors.newSingleThreadExecutor())).build();
}
