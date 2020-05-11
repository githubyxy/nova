package com.yxy.nova;

import com.yxy.nova.web.shiro.sesssion.GodSessionSerializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @description:
 * @author: yuxiaoyu
 * @date: 2018-04-18 下午6:43
 */
@Configuration
public class RedisConfig {

    // 哨兵模式
//    @Value("${redis.password}")
//    private String password;
//    @Value("${redis.sentinels}")
//    private String sentinels;
//    @Value("${redis.master}")
//    private String master;

    // 单机redis
    @Value("${redis.host}")
    private String host;
    @Value("${redis.port}")
    private Integer port;
    @Value("${redis.password}")
    private String password;


    @Bean
    public RedisSerializer<?> redisValueSerializer() {
        return new GodSessionSerializer();
    }
    
    @Bean
    public RedisSerializer<?> redisKeySerializer() {
        return new StringRedisSerializer();
    }


    @Bean
    public RedisSerializer<?> prefixedRedisKeySerializer() {
        return new PrefixedStringRedisSerializer();
    }


    @Bean
    public JedisConnectionFactory novaRedisConnectionFactory() {


//        Set<String> sentinelSet = new HashSet<>();
//        for (String sentinel : sentinels.split(",")) {
//            sentinelSet.add(StringUtils.trimToEmpty(sentinel));
//        }
//
//        RedisSentinelConfiguration rsc=new RedisSentinelConfiguration(master, sentinelSet);
//        rsc.setPassword(RedisPassword.of(password));
//
//
//        JedisConnectionFactory jedisConnectionFactory=new JedisConnectionFactory(rsc);

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);
        config.setPassword(password);

        return new JedisConnectionFactory(config);
    }

    @Bean("novaSessionRedisTemplate")
    public RedisTemplate<?, ?> novaSessionRedisTemplate() throws Exception {
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(novaRedisConnectionFactory());
        redisTemplate.setKeySerializer(prefixedRedisKeySerializer());
        redisTemplate.setHashKeySerializer(redisKeySerializer());
        redisTemplate.setHashValueSerializer(redisValueSerializer());
        redisTemplate.setValueSerializer(redisValueSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean("redisTemplate")
    public RedisTemplate<?, ?> novaRedisTemplate() throws Exception {
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(novaRedisConnectionFactory());
        redisTemplate.setKeySerializer(prefixedRedisKeySerializer());
        redisTemplate.setHashKeySerializer(redisKeySerializer());
        redisTemplate.setHashValueSerializer(novaRedisValueSerializer());
        redisTemplate.setValueSerializer(novaRedisValueSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean("novaRedisValueSerializer")
    public RedisSerializer<?> novaRedisValueSerializer() {
        return new StringRedisSerializer();
    }


    /**
     * 序列化时固定加前缀，反序列化时固定去掉前缀
     */
    private class PrefixedStringRedisSerializer extends StringRedisSerializer {

        /**
         * 固定的前缀
         */
        private final String PREFIX = "NOVA:";

        @Override
        public String deserialize(@Nullable byte[] bytes) {
            String prefixedString = super.deserialize(bytes);
            if (prefixedString == null || prefixedString.length() < PREFIX.length()) {
                // 不去前缀
                return prefixedString;
            } else {
                return prefixedString.substring(PREFIX.length());
            }
        }

        @Override
        public byte[] serialize(@Nullable String string) {
            if (string == null) {
                return null;
            } else {
                return super.serialize(PREFIX + string);
            }
        }
    }


}
