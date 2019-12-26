package com.yxy.nova.web.shiro.sesssion;

import com.yxy.nova.web.shiro.config.LoginConfig;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * shiro session dao
 *
 * @author yuxiaoyu
 * @date 2019/10/27 19:35
 */
@Component
public class RedisShiroSessionDAO extends AbstractSessionDAO {

    private Logger logger = LoggerFactory.getLogger(RedisShiroSessionDAO.class);

    @Resource(name = "novaSessionRedisTemplate")
    private RedisTemplate redisTemplate;

    @Resource
    private LoginConfig loginConfig;


    /**
     * 创建session并保存在redis中
     * @param session
     * @return
     */
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        final String key = getShiroSessionRedisKey(sessionId.toString());
        setShiroSession(key, session);
        session.setTimeout(loginConfig.getGlobalSessionTimeout());
        return sessionId;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        if (session == null || session.getId() == null) {
            logger.error("session or session id is null");
            return;
        }
        session.setTimeout(loginConfig.getGlobalSessionTimeout());
        final String key = getShiroSessionRedisKey(session.getId().toString());
        setShiroSession(key, session);
    }

    @Override
    public void delete(Session session) {
        if (session == null || session.getId() == null) {
            logger.error("session or session id is null");
            return;
        }
        final String key = getShiroSessionRedisKey(session.getId().toString());
        redisTemplate.delete(key);
    }

    @Override
    public Collection<Session> getActiveSessions() {
        return null;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        final String key = getShiroSessionRedisKey(sessionId.toString());
        Session session = getShiroSession(key);
        return session;
    }

    private Session getShiroSession(String key) {
        return (Session)redisTemplate.opsForValue().get(key);
    }

    private void setShiroSession(String key, Session session){
        redisTemplate.opsForValue().set(key, session);
        redisTemplate.expire(key, loginConfig.getGlobalSessionTimeout(), TimeUnit.MILLISECONDS);
    }

    /**
     * 获取保存shiro session信息的redis key
     * @param key
     * @return
     */
    public static String getShiroSessionRedisKey(String key){
        return "sessionid:" + key;
    }

}
