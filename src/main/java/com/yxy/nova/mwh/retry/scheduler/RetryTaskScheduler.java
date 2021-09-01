package com.yxy.nova.mwh.retry.scheduler;

import com.yxy.nova.mwh.retry.api.IdcResolver;
import com.yxy.nova.mwh.retry.api.RetryTaskHandler;
import com.yxy.nova.mwh.retry.api.event.EventTypeEnum;
import com.yxy.nova.mwh.retry.api.event.RetryTaskEventListener;
import com.yxy.nova.mwh.retry.config.RetryConfiguration;
import com.yxy.nova.mwh.utils.log.TraceIdUtil;
import com.yxy.nova.mwh.utils.time.DateTimeUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.ScheduleJobBootstrap;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RetryTaskScheduler implements BeanNameAware {

    private String beanName;
    private RetryConfiguration configuration;
    private CoordinatorRegistryCenter regCenter;
    private String cron;
    private Integer workerThreadPoolSize;
    private Boolean disabled;
    private Boolean overwrite;
    private String jobShardingStrategyType;
    /**
     * 重试框架已经有自定义的线程池，默认将线程池策略-单线程
     */
    private String jobExecutorServiceHandlerType="SINGLE_THREAD";
    private String shardingItemParameters;
    private Map<String, RetryTaskHandler> handlers;
    private Map<String, RetryTaskEventListener> typedEventListeners;
    private Map<String, String> retentionPolicies;
    private IdcResolver idcResolver;
    /**
     * 每种任务类型的数据保存的天数
     */
    private Map<String, Integer> retentionDays;
    private RetryTaskEventDispatcher eventDispatcher;
    private ThreadPoolExecutor executor;

    private static final String DEFAULT_RETENTION_POLICY_KEY = "default";

    private static Pattern RETENTION_POLICY_PATTERN = Pattern.compile("(\\d+)([md])");

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @PostConstruct
    public void init() throws Exception{
        // 校验属性配置
        checkProperties();
        // 初始化事件分配器
        initEventDispatcher();
        // 初始化线程池
        initExecutor();
        // 启动elasticJob调度器
        startElasticJobScheduler();
        // 启动删除历史数据的后台线程
        if (MapUtils.isNotEmpty(retentionDays)) {
            startDeleteHistoryTaskThread();
        }
    }



    private void initExecutor() {
        executor = new ThreadPoolExecutor(workerThreadPoolSize, workerThreadPoolSize, 5L, TimeUnit.MINUTES,
                new LinkedBlockingQueue<Runnable>(1000),
                new CustomizableThreadFactory(beanName + "-retry-task-thread"));
        executor.allowCoreThreadTimeOut(true);

    }

    /**
     * 校验属性是否配置正确
     */
    private void checkProperties() {
        if (configuration == null) {
            throw new RuntimeException("configuration未配置");
        }

        if (regCenter == null) {
            throw new RuntimeException("regCenter未配置");
        }

        if (StringUtils.isBlank(cron)) {
            throw new RuntimeException("cron未配置");
        }

        if (disabled == null) {
            throw new RuntimeException("disabled未配置");
        }

        if (overwrite == null) {
            throw new RuntimeException("overwrite未配置");
        }

        if (workerThreadPoolSize == null) {
            // 设置线程数的默认值
            workerThreadPoolSize = Runtime.getRuntime().availableProcessors() * 2;
        }

        if (workerThreadPoolSize < 1) {
            throw new RuntimeException("workerThreadPoolSize必须大于0");
        }

        if (MapUtils.isEmpty(handlers)) {
            throw new RuntimeException("handlerMap未配置");
        }

        if (MapUtils.isNotEmpty(retentionPolicies)) {
            for (Map.Entry<String, String> entry : retentionPolicies.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if (!DEFAULT_RETENTION_POLICY_KEY.equals(key) && !handlers.containsKey(key)) {
                    throw new RuntimeException("retentionPolicies的key值或者为" + DEFAULT_RETENTION_POLICY_KEY
                            + ", 或者与handlers中的key值保持一致");
                }

                if (!RETENTION_POLICY_PATTERN.matcher(value).matches()) {
                    throw new RuntimeException("retentionPolicies的value值格式不正确");
                }

            }

            retentionDays = convertRetentionPolicies(retentionPolicies);
        }

    }


    private void initEventDispatcher() throws Exception{

        eventDispatcher = new RetryTaskEventDispatcher();

        if (MapUtils.isEmpty(typedEventListeners)) {
            return;
        }

        for (Map.Entry<String, RetryTaskEventListener> entry : typedEventListeners.entrySet()) {
            for (String eventTypeStr : entry.getKey().split(",")) {
                EventTypeEnum eventTypeEnum = EventTypeEnum.valueOf(StringUtils.trimToEmpty(eventTypeStr));
                eventDispatcher.addEventListener(eventTypeEnum, entry.getValue());
            }
        }

    }

    private void startElasticJobScheduler() {

        String jobName = beanName;

//        SimpleJobConfiguration.SimpleJobConfigurationBuilder jobConfigurationBuilder =
//                new SimpleJobConfiguration.SimpleJobConfigurationBuilder(jobName, RetryTaskElasticJob.class, configuration.getShardingTotalCount(), cron);
//        jobConfigurationBuilder.disabled(disabled);
//        jobConfigurationBuilder.overwrite(overwrite);
//        if (StringUtils.isNotBlank(shardingItemParameters)) {
//            jobConfigurationBuilder.shardingItemParameters(shardingItemParameters);
//        }
//        if (StringUtils.isNotBlank(jobShardingStrategyClass)) {
//            jobConfigurationBuilder.jobShardingStrategyClass(jobShardingStrategyClass);
//        }

        int shardingTotalCount = 1;
        if (configuration.isTableSharding()) {
            shardingTotalCount = configuration.getShardingTotalCount();
        }
        JobConfiguration.Builder builder = JobConfiguration.newBuilder(jobName, shardingTotalCount);
        builder.cron(cron);
        builder.disabled(disabled);
        builder.overwrite(overwrite);
        if (StringUtils.isNotBlank(shardingItemParameters)) {
            builder.shardingItemParameters(shardingItemParameters);
        }
        if (StringUtils.isNotBlank(jobShardingStrategyType)) {
            builder.jobShardingStrategyType(jobShardingStrategyType);
        }
        if (StringUtils.isNotBlank(jobExecutorServiceHandlerType)) {
            builder.jobExecutorServiceHandlerType(jobExecutorServiceHandlerType);
        }

//        JobScheduler jobScheduler = new JobScheduler(regCenter, jobConfigurationBuilder.build(), new ElasticJobListener[0]);
//        jobScheduler.init();
        ScheduleJobBootstrap scheduleJobBootstrap = new ScheduleJobBootstrap(regCenter, new RetryTaskElasticJob(), builder.build());
        scheduleJobBootstrap.schedule();
        // 注册此调度器
        RetryTaskSchedulerRegistry.register(jobName, this);

    }

    private void startDeleteHistoryTaskThread() {

        if (MapUtils.isEmpty(retentionPolicies)) {
            return;
        }

        Thread deleteHistoryTaskThread = new Thread("retrytask-delete-history-task-thread-" + beanName) {
            @Override
            public void run() {
                while (true) {
                    MDC.put(TraceIdUtil.TRACEID_NAME, TraceIdUtil.genId());
                    logger.info("删除历史任务开始");
                    try {
                        for (Map.Entry<String, Integer> entry : retentionDays.entrySet()) {
                            String taskType = entry.getKey();
                            int days = entry.getValue();
                            while (true) {
                                int rows = configuration.getRetryTaskRepository().deleteHistoryTasks(taskType, DateTimeUtil.minusDays(new Date(), days));
                                logger.info("删除任务[{}]的条数:{}", taskType, rows);
                                if (rows == 0) {
                                    break;
                                }
                            }
                        }
                    } catch (Throwable throwable) {
                        logger.error("删除历史任务失败", throwable);
                    } finally {
                        try {
                            Thread.sleep(TimeUnit.MINUTES.toMillis(5));
                        } catch (InterruptedException e) {
                            logger.error("", e);
                        }
                    }
                }
            }
        };

        deleteHistoryTaskThread.setDaemon(true);
        deleteHistoryTaskThread.start();

    }

    /**
     * 转换retentionPolicy
     * @param retentionPolicies
     * @return
     */
    private Map<String, Integer> convertRetentionPolicies(Map<String, String> retentionPolicies) {
        Map<String, Integer> finalRetentionPolicies = new HashMap<>();

        for (Map.Entry<String, String> originPolicyEntry : retentionPolicies.entrySet()) {
            if (!DEFAULT_RETENTION_POLICY_KEY.equals(originPolicyEntry.getKey())) {
                finalRetentionPolicies.put(originPolicyEntry.getKey(), parseRetentionTime(originPolicyEntry.getValue()));
            }
        }

        // 未配置retentionPolicy的任务类型使用默认值
        String defaultRetentionPolicy = retentionPolicies.get(DEFAULT_RETENTION_POLICY_KEY);
        if (StringUtils.isNotBlank(defaultRetentionPolicy)) {
            for (String handlerTaskType : handlers.keySet()) {
                if (!finalRetentionPolicies.containsKey(handlerTaskType)) {
                    finalRetentionPolicies.put(handlerTaskType, parseRetentionTime(defaultRetentionPolicy));
                }
            }
        }

        return finalRetentionPolicies;
    }

    /**
     * 解析保存时间文本，返回对应的天数
     * @param text
     * @return
     */
    private Integer parseRetentionTime(String text) {

        Matcher m = RETENTION_POLICY_PATTERN.matcher(text);

        if (m.find()) {
            int quantity = Integer.parseInt(m.group(1));
            String timeUnit = m.group(2);
            if ("m".equals(timeUnit)) {
                return quantity * 30;
            } else {
                return quantity;
            }
        } else {
            throw new RuntimeException("无法解析retentionPolicies");
        }

    }


    public RetryConfiguration getConfiguration() {
        return configuration;
    }

    public RetryTaskEventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    public Integer getWorkerThreadPoolSize() {
        return workerThreadPoolSize;
    }

    public Map<String, RetryTaskHandler> getHandlers() {
        return handlers;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setConfiguration(RetryConfiguration configuration) {
        this.configuration = configuration;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public void setRegCenter(CoordinatorRegistryCenter regCenter) {
        this.regCenter = regCenter;
    }

    public void setWorkerThreadPoolSize(Integer workerThreadPoolSize) {
        this.workerThreadPoolSize = workerThreadPoolSize;
    }

    public void setHandlers(Map<String, RetryTaskHandler> handlers) {
        this.handlers = handlers;
    }

    public void setTypedEventListeners(Map<String, RetryTaskEventListener> typedEventListeners) {
        this.typedEventListeners = typedEventListeners;
    }

    public ThreadPoolExecutor getExecutor() {
        return executor;
    }

    public Boolean getOverwrite() {
        return overwrite;
    }

    public void setOverwrite(Boolean overwrite) {
        this.overwrite = overwrite;
    }

    public void setJobShardingStrategyType(String jobShardingStrategyType) {
        this.jobShardingStrategyType = jobShardingStrategyType;
    }

    public void setShardingItemParameters(String shardingItemParameters) {
        this.shardingItemParameters = shardingItemParameters;
    }

    public void setRetentionPolicies(Map<String, String> retentionPolicies) {
        this.retentionPolicies = retentionPolicies;
    }

    public IdcResolver getIdcResolver() {
        return idcResolver;
    }

    public void setIdcResolver(IdcResolver idcResolver) {
        this.idcResolver = idcResolver;
    }

    public void setJobExecutorServiceHandlerType(String jobExecutorServiceHandlerType) {
        this.jobExecutorServiceHandlerType = jobExecutorServiceHandlerType;
    }
}
