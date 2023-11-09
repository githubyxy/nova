package com.yxy.nova;

import com.alibaba.druid.pool.DruidDataSource;
import com.yxy.nova.doris.DorisTableStreamLoader;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author yxy
 * @description: 数据源配置
 * @date 2023/11/09 11:08
 */
@Configuration
public class DorisDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.doris")
    public DataSource dorisDataSource() {
        return new DruidDataSource();
    }

    @Bean
    public JdbcTemplate dorisJdbcTemplate(DataSource dorisDataSource) {
        return new JdbcTemplate(dorisDataSource);
    }

    @Bean(name="callRecordDorisTableStreamLoader")
    public DorisTableStreamLoader callRecordDorisTableStreamLoader() {
        return new DorisTableStreamLoader("call_record");
    }


}
