package com.yxy.nova.elastic.job;

import com.alibaba.fastjson.JSON;
import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.plugin.job.type.simple.AbstractSimpleElasticJob;
import com.yxy.nova.dal.mysql.dataobject.TaskItemExecCallDO;
import com.yxy.nova.dal.mysql.mapper.TaskItemExecCallMapper;
import com.yxy.nova.mwh.utils.UUIDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yuxiaoyu
 * @date 2020/5/8 上午10:51
 * @Description
 */
@Slf4j
public class TestJob extends AbstractSimpleElasticJob {

    @Autowired
    private TaskItemExecCallMapper taskItemExecCallMapper;

    @Override
    public void process(JobExecutionMultipleShardingContext jobExecutionMultipleShardingContext) {
        log.info("处理业务中……");
//        while (System.currentTimeMillis() < c + (60 * 1000)) {
//
//        }
        List<TaskItemExecCallDO> list = new ArrayList<>();
        for (int i=0; i< 500; i++) {
            TaskItemExecCallDO taskItemExecCallDO = new TaskItemExecCallDO();
            taskItemExecCallDO.setTaskItemExecUuid(UUIDGenerator.generate());
            taskItemExecCallDO.setTaskItemUuid(UUIDGenerator.generate());
            taskItemExecCallDO.setPolicyUuid("12391gehjqe1237i");
            taskItemExecCallDO.setTaskBatchUuid(i + 1);
            taskItemExecCallDO.setTaskUuid("100");
            taskItemExecCallDO.setPartnerCode("p");
            taskItemExecCallDO.setAppCode("a");
            taskItemExecCallDO.setRobotDuration(20);
            taskItemExecCallDO.setCustomerDuration(10);
            taskItemExecCallDO.setStatus("COMPLETED");
            taskItemExecCallDO.setInteractionDetail("contentcontentcontentcontentcontentcontentcontentcontent");
            taskItemExecCallDO.setIsHalted(false);
            taskItemExecCallDO.setTalkType("AI");
            taskItemExecCallDO.setRobotPhone("13585934620");
            taskItemExecCallDO.setGmtCreate(new Date());
            taskItemExecCallDO.setGmtModify(new Date());
            list.add(taskItemExecCallDO);
        }
        taskItemExecCallMapper.insertList(list);
        log.info("处理业务结束……");
    }
}
