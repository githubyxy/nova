package com.yxy.nova.mwh.retry.config;

import com.yxy.nova.mwh.retry.repository.NonShardingRetryTaskRepository;
import com.yxy.nova.mwh.retry.repository.RetryTaskRepository;
import com.yxy.nova.mwh.retry.repository.ShardingRetryTaskRepository;
import com.google.common.io.Closeables;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Properties;

public class RetryConfiguration {

    public static final String DEFAULT_MYBATIS_MAPPING_FILE = "/mybatis/mybatis-config.xml";

    /**
     * 事务管理器
     */
    private PlatformTransactionManager transactionManager;

    /**
     * 数据源
     */
    private DataSource dataSource;

    /**
     * myBatis sqlsessionFactory
     */
    protected SqlSessionFactory sqlSessionFactory;

    /**
     * 表前缀
     */
    private String databaseTablePrefix;

    /**
     * 仓库
     */
    private RetryTaskRepository retryTaskRepository;

    /**
     * 总的分片数
     */
    private Integer shardingTotalCount;

    /**
     * 表是否分片
     */
    private boolean tableSharding;

    @PostConstruct
    public void init() {
        // 校验参数
        if (shardingTotalCount <= 0 || shardingTotalCount > 256) {
            throw new RuntimeException("shardingTotalCount的取值范围:[1,256]");
        }

        // 初始化MyBatis的sqlSessionFactory
        initSqlSessionFactory();

        // 初始化仓库
        initRepository();
    }

    /**
     * 初始化MyBatis的sqlSessionFactory
     */
    private void initSqlSessionFactory() {
        InputStream inputStream = null;
        try {
            inputStream = getClass().getResourceAsStream(DEFAULT_MYBATIS_MAPPING_FILE);
            Environment environment = new Environment("default", new SpringManagedTransactionFactory(), dataSource);
            Reader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            Properties properties = new Properties();
            Configuration configuration = initMybatisConfiguration(environment, reader, properties);
            sqlSessionFactory = new DefaultSqlSessionFactory(configuration);

        } catch (Exception e) {
            throw new RuntimeException("Error while building ibatis SqlSessionFactory: " + e.getMessage(), e);
        } finally {
            Closeables.closeQuietly(inputStream);
        }
    }

    /**
     * 初始化仓库
     */
    private void initRepository() {
        databaseTablePrefix = StringUtils.trimToEmpty(databaseTablePrefix);
        if (tableSharding) {
            retryTaskRepository = new ShardingRetryTaskRepository(databaseTablePrefix, shardingTotalCount, sqlSessionFactory);
        } else {
            retryTaskRepository = new NonShardingRetryTaskRepository(databaseTablePrefix, shardingTotalCount, sqlSessionFactory);
        }
    }

    protected Configuration initMybatisConfiguration(Environment environment, Reader reader, Properties properties) {
        XMLConfigBuilder parser = new XMLConfigBuilder(reader,"", properties);
        parser.parse();
        Configuration configuration = parser.getConfiguration();
        configuration.setEnvironment(environment);
        return configuration;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public RetryTaskRepository getRetryTaskRepository() {
        return retryTaskRepository;
    }

    public void setDatabaseTablePrefix(String databaseTablePrefix) {
        this.databaseTablePrefix = databaseTablePrefix;
    }

    public String getDatabaseTablePrefix() {
        return databaseTablePrefix;
    }

    public Integer getShardingTotalCount() {
        return shardingTotalCount;
    }

    public void setShardingTotalCount(Integer shardingTotalCount) {
        this.shardingTotalCount = shardingTotalCount;
    }

    public boolean isTableSharding() {
        return tableSharding;
    }

    public void setTableSharding(boolean tableSharding) {
        this.tableSharding = tableSharding;
    }
}
