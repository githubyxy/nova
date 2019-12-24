package com.yxy.nova;

import com.yxy.nova.interceptor.BaseFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yuxiaoyu
 * @Description:
 * @date 2019-10-28 13:50
 */
@Configuration
public class FilterConfig {

//    @Bean
//    public FilterRegistrationBean filterRegistrationBean(){
////        FilterRegistrationBean bean = new FilterRegistrationBean();
//////        bean.setFilter(new BaseFilter());
////        bean.addUrlPatterns("/auth/*", "/biz/*");
////        return bean;
//    }
}
