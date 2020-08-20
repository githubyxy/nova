package com.yxy.nova.web.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.yxy.nova.dal.mysql.dataobject.UserDO;
import com.yxy.nova.util.SpringApplicationContext;
import com.yxy.nova.web.shiro.sesssion.NovaPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @description:
 * @author: shui.ren
 * @date: 2018-04-24 下午5:08
 */
public class SecurityUtil {

    /**
     * 存储loginId与HertzPrincipal的对应关系
     */
    private static LoadingCache<String, NovaPrincipal> novaPrincipalCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build(new CacheLoader<String, NovaPrincipal>() {
                @Override
                public NovaPrincipal load(String loginId) throws Exception {
                    return loadPrincipal(loginId);
                }
            });

    private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

    /**
     * 返回当前登录用户
     * @return
     */
    public static NovaPrincipal getPrincipal() {
        String loginId = (String) SecurityUtils.getSubject().getPrincipal();

        if (StringUtils.isBlank(loginId)) {
            return null;
        }
        // 不从缓存读取，直接读取数据库
        // HertzPrincipal principal = getPrincipalByloginId(loginId);
        NovaPrincipal principal = loadPrincipal(loginId);

        Session session = SecurityUtils.getSubject().getSession();
        if (session != null) {
            principal.setSessionId(session.getId().toString());
        }

        return principal;
    }

    /**
     * 根据loginId查找HertzPrincipal
     * @param loginId
     * @return
     */
    public static NovaPrincipal getPrincipalByloginId(String loginId) {
        return novaPrincipalCache.getUnchecked(loginId);
    }

    /**
     * 清除缓存
     * @param loginId
     */
    public static void removeCache(String loginId){
        novaPrincipalCache.invalidate(loginId);
    }

    /**
     * 加载principal数据
     * @return
     */
    private static NovaPrincipal loadPrincipal(String loginId) {
        UserDO userDO = SpringApplicationContext.getUserBaseService().findUserByLoginId(loginId);

        // 用户是用户
        NovaPrincipal principal = new NovaPrincipal();
        principal.setLoginId(userDO.getLoginId());
        principal.setRealName(userDO.getRealName());
        principal.setUserUuid(userDO.getUserUuid());
//        if (UserStatusEnum.NORMAL.name().equals(userDO.getStatus())) {
//            principal.setPrivilegeCodeSet(SpringApplicationContext.getPrivilegeBaseService().listPrivilegeCodeByUserUuid(userDO.getUserUuid()));
//        } else {
//            logger.info("用户[{}]已被禁用或删除", userDO.getLoginId());
//            principal.setPrivilegeCodeSet(new HashSet<>(0));
//        }
//        principal.setRoleName(SpringApplicationContext.getUserBaseService().listRoleName(userDO).get(0));
        return principal;

    }


}
