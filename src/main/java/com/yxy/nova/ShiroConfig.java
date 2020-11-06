package com.yxy.nova;

import com.yxy.nova.web.shiro.config.LoginConfig;
import com.yxy.nova.web.shiro.filter.CustomizedFormAuthenticationFilter;
import com.yxy.nova.web.shiro.filter.OpenApiFilter;
import com.yxy.nova.web.shiro.realm.NovaRealm;
import com.yxy.nova.web.shiro.sesssion.RedisShiroSessionDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro的配置文件
 *
 * @author yuxiaoyu
 * @date 2019/10/27 10:02
 */
@Configuration
public class ShiroConfig {

    @Bean("sessionManager")
    public SessionManager sessionManager(RedisShiroSessionDAO redisShiroSessionDAO, LoginConfig loginConfig){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //设置session过期时间为1小时(单位：毫秒)，默认为30分钟
        sessionManager.setGlobalSessionTimeout(loginConfig.getGlobalSessionTimeout());
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdUrlRewritingEnabled(false);

        sessionManager.setSessionIdCookie(new SimpleCookie("novasessionid"));

        sessionManager.setSessionDAO(redisShiroSessionDAO);
        return sessionManager;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(NovaRealm novaRealm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(novaRealm);
        securityManager.setSessionManager(sessionManager);

        return securityManager;
    }


    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager,
                                              OpenApiFilter openapiFilter) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        shiroFilter.setLoginUrl("/login");

        // 处理ajax请求未登录
        CustomizedFormAuthenticationFilter customizedFormAuthenticationFilter = new CustomizedFormAuthenticationFilter();

        Map<String, Filter> filters = shiroFilter.getFilters();
        filters.put("authc", customizedFormAuthenticationFilter);
        filters.put("openapiFilter", openapiFilter);

        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/statics/**", "anon");
        filterMap.put("/code/**", "anon");
        filterMap.put("/css/**", "anon");
        filterMap.put("/datas/**", "anon");
        filterMap.put("/images/**", "anon");
        filterMap.put("/img/**", "anon");
        filterMap.put("/js/**", "anon");
        filterMap.put("/layui/**", "anon");
        filterMap.put("/plugins.layui/**", "anon");
        filterMap.put("/svg/**", "anon");
        filterMap.put("/ok.htm", "anon");
        filterMap.put("/innercallback/**", "anon");
        filterMap.put("/innerapi/**", "anon");
        filterMap.put("/openapi/**", "openapiFilter");
        filterMap.put("/login", "anon");
        filterMap.put("/novaWeb/login", "anon");
        filterMap.put("/logout", "logout");
        filterMap.put("/error/**", "anon");
        filterMap.put("/fs", "anon");
        filterMap.put("/**", "authc");

        shiroFilter.setFilterChainDefinitionMap(filterMap);


        return shiroFilter;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

//    @Bean
//    public RedisShiroSessionDAO redisShiroSessionDAO() {
//        return new RedisShiroSessionDAO();
//    }


    /**
     * 开启权限验证的注解支持
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

//    @Bean("loginConfig")
//    public LoginConfig loginConfig(){
//        return new LoginConfig();
//    }

    @Bean("openapiFilter")
    public OpenApiFilter openapiFilter() {
        return new OpenApiFilter();
    }

}
