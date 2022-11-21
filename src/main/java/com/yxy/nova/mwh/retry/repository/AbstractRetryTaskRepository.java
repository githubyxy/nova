package com.yxy.nova.mwh.retry.repository;

import com.yxy.nova.mwh.retry.entity.RetryTask;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 实现一些公共方法
 * @author shui.ren
 */
public abstract class AbstractRetryTaskRepository implements RetryTaskRepository {

    /**
     * SqlSessionFactory
     */
    protected SqlSessionFactory sqlSessionFactory;

    /**
     * 表前缀
     */
    protected String databaseTablePrefix;

    /**
     * 总的分片数
     */
    protected Integer shardingTotalCount;

    protected final String STATEMENT_PREFIX = RetryTask.class.getName();

    public AbstractRetryTaskRepository(String databaseTablePrefix, Integer shardingTotalCount, SqlSessionFactory sqlSessionFactory) {
        this.databaseTablePrefix = databaseTablePrefix;
        this.sqlSessionFactory = sqlSessionFactory;
        this.shardingTotalCount = shardingTotalCount;
    }


    @Override
    public Integer insertSelective(RetryTask taskEntity) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("prefix", databaseTablePrefix);
        parameters.put("task", taskEntity);
        return openSession().insert(STATEMENT_PREFIX + ".insertSelective", parameters);
    }

    /**
     * 以乐观锁的方式设置任务开始, 如果更新失败返回false
     * @param shardingNumber
     * @param id
     * @param oldVersion
     * @param newState
     * @param startTime
     * @param newVersion
     * @return
     */
    @Override
    public boolean setTaskStart(Integer shardingNumber, Long id, String oldVersion, String newState, Date startTime, String newVersion) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("prefix", databaseTablePrefix);
        parameters.put("shardingNumber", shardingNumber);
        parameters.put("id", id);
        parameters.put("oldVersion", oldVersion);
        parameters.put("newState", newState);
        parameters.put("startTime", startTime);
        parameters.put("newVersion", newVersion);

        return openSession().update(STATEMENT_PREFIX + ".setTaskStart", parameters) == 1;
    }

    /**
     * 由于异常终止任务执行
     * @param shardingNumber
     * @param id
     * @param newState
     * @param message
     */
    @Override
    public boolean terminateTaskAbruptly(Integer shardingNumber, Long id, String newState, String message) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("prefix", databaseTablePrefix);
        parameters.put("shardingNumber", shardingNumber);
        parameters.put("id", id);
        parameters.put("newState", newState);
        parameters.put("message", message);

        return openSession().update(STATEMENT_PREFIX + ".terminateTaskAbruptly", parameters) == 1;
    }

    /**
     * 更新状态
     * @param shardingNumber
     * @param id
     * @param state
     */
    @Override
    public boolean updateState(Integer shardingNumber, Long id, String state) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("prefix", databaseTablePrefix);
        parameters.put("shardingNumber", shardingNumber);
        parameters.put("id", id);
        parameters.put("state", state);

        return openSession().update(STATEMENT_PREFIX + ".updateState", parameters) == 1;
    }

    /**
     * 任务执行完成后，更新记录
     * @param shardingNumber
     * @param id
     * @param newState
     * @param finished
     * @param endTime
     * @param message
     */
    @Override
    public boolean endTaskExcution(Integer shardingNumber, Long id, String newState, boolean finished, Date endTime, String message, Integer currentAttempt) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("prefix", databaseTablePrefix);
        parameters.put("shardingNumber", shardingNumber);
        parameters.put("id", id);
        parameters.put("newState", newState);
        parameters.put("finished", finished);
        parameters.put("endTime", endTime);
        parameters.put("message", message);
        parameters.put("currentAttempt", currentAttempt);

        return openSession().update(STATEMENT_PREFIX + ".endTaskExcution", parameters) == 1;
    }

    protected SqlSession openSession() {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 取消任务
     * @param shardingNumber
     * @param id
     * @return
     */
    @Override
    public boolean cancelRetryTask(Integer shardingNumber, Long id) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("prefix", databaseTablePrefix);
        parameters.put("shardingNumber", shardingNumber);
        parameters.put("id", id);
        return openSession().update(STATEMENT_PREFIX + ".cancelRetryTask", parameters) == 1;
    }
}
