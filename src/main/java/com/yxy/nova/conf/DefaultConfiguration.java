package com.yxy.nova.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

/**
 * @author yuxiaoyu
 * @Description:
 * @date 2019-08-26 15:36
 */
@Configuration
public class DefaultConfiguration {
    @Bean
    public UrlBasedViewResolver setupViewResolver() {
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
        resolver.setPrefix("/");
        resolver.setSuffix(".ftl");
        resolver.setCache(true);
        resolver.setViewClass(JstlView.class);
        return resolver;
    }
}
