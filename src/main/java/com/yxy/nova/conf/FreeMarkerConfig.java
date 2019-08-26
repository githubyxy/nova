package com.yxy.nova.conf;

import freemarker.template.TemplateModelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author yuxiaoyu
 * @Description:
 * @date 2019-08-26 16:27
 */
@Configuration
public class FreeMarkerConfig {
    @Autowired
    protected freemarker.template.Configuration configuration;

    /**
     * 添加自定义标签
     */
    @PostConstruct
    public void setSharedVariable() {
        try {
            configuration.setSharedVariable("name", "于晓宇");
        } catch (TemplateModelException e) {
            e.printStackTrace();
        }
    }

}
