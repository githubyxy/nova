package test.turbo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.didiglobal.turbo.engine.annotation.EnableTurboEngine;
import com.didiglobal.turbo.engine.common.FlowDefinitionStatus;
import com.didiglobal.turbo.engine.dao.FlowDefinitionDAO;
import com.didiglobal.turbo.engine.dao.FlowDeploymentDAO;
import com.didiglobal.turbo.engine.dao.mapper.InstanceDataMapper;
import com.didiglobal.turbo.demo.util.EntityBuilder;
import com.didiglobal.turbo.engine.engine.ProcessEngine;
import com.didiglobal.turbo.engine.entity.FlowDefinitionPO;
import com.didiglobal.turbo.engine.entity.FlowDeploymentPO;
import com.didiglobal.turbo.engine.entity.InstanceDataPO;
import com.didiglobal.turbo.engine.model.*;
import com.didiglobal.turbo.engine.param.*;
import com.didiglobal.turbo.engine.result.*;
import com.google.common.collect.Lists;
import com.yxy.nova.NovaApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;

@SpringBootTest(classes = NovaApplication.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@EnableTurboEngine
public class MysqlTest {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    /**
     * 仅做业务区分,与逻辑无关
     */
    public static final String tenant = "testTenant";
    /**
     * 仅做业务区分,与逻辑无关
     */
    public static final String caller = "testCaller";
    /**
     * 操作人
     */
    public static final String operator = "yxy";

    @Resource
    private ProcessEngine processEngine;
    @Resource
    private FlowDefinitionDAO flowDefinitionDAO;

    private CreateFlowParam createFlowParam;
    private CreateFlowResult createFlowResult;
    private UpdateFlowResult updateFlowResult;
    private DeployFlowResult deployFlowResult;

    @Test
    public void mapperTest() {

        FlowDefinitionPO flowDefinitionPO = new FlowDefinitionPO();
//        BeanUtils.copyProperties(createFlowParam, flowDefinitionPO);
        String flowModuleId = "a";
        flowDefinitionPO.setFlowModuleId(flowModuleId);
        flowDefinitionPO.setStatus(FlowDefinitionStatus.INIT);
        Date date = new Date();
        flowDefinitionPO.setCreateTime(date);
        flowDefinitionPO.setModifyTime(date);

        int rows = flowDefinitionDAO.insert(flowDefinitionPO);

        flowDefinitionDAO.getBaseMapper().selectById("a");
        FlowDefinitionPO f = flowDefinitionDAO.selectByModuleId("a");
        System.out.println("FlowDefinitionPO:" + JSONObject.toJSONString(f));
    }

    /**
     * 案例1：某团购售后流程
     * 用户A在订单列表中选择订单，判断订单状态，如果状态为未发货，则直接跳转至退款申请页，
     * 如果状态为待收货则提示不支持售后，跳转至物流信息页，如果状态为已收货，则跳转至售后页填写售后信息并提交。
     * <p>
     * 未发货
     * --->   用户节点（申请取消）     --->
     * 未收到货
     * 开始节点 ->  用户节点（输入订单相关信息) --->排他节点(判断订单状态)  --->   用户节点（展示物流信息）  --->   结束节点
     * 已收到货
     * --->    用户节点（填写售后原因） --->
     */
    @Test
    public void test2() {
        createFlow();

        updateFlow();

        deployFlow();

        startProcessToEnd();
    }

    private void createFlow() {
        createFlowParam = new CreateFlowParam(tenant, caller);
        createFlowParam.setFlowKey("person_time");
        createFlowParam.setFlowName("请假SOP");
        createFlowParam.setRemark("demo");
        createFlowParam.setOperator(operator);
        createFlowResult = processEngine.createFlow(createFlowParam);
        System.out.println("createFlow.||createFlowResult=" + createFlowResult);
//        LOGGER.info("createFlow.||createFlowResult={}", createFlowResult);
    }

    private void updateFlow() {
        UpdateFlowParam updateFlowParam = new UpdateFlowParam(tenant, caller);
        updateFlowParam.setFlowModuleId(createFlowResult.getFlowModuleId());

        List<FlowElement> flowElementList = Lists.newArrayList();
        // 开始
        StartEvent startEvent1 = new StartEvent();
        startEvent1.setKey("StartEvent_0ofi5hg");
        startEvent1.setType(2);
        List<String> outgoings = new ArrayList();
        outgoings.add("SequenceFlow_1udf5vg");
        startEvent1.setOutgoing(outgoings);
        flowElementList.add(startEvent1);

        UserTask userTask1 = new UserTask();
        userTask1.setKey("UserTask_1625vn7");
        userTask1.setType(4);
        List<String> utIncomings = new ArrayList();
        utIncomings.add("SequenceFlow_1udf5vg");
        userTask1.setIncoming(utIncomings);
        List<String> utOutgoings = new ArrayList();
        utOutgoings.add("SequenceFlow_06uq82c");
        userTask1.setOutgoing(utOutgoings);
        flowElementList.add(userTask1);

        ExclusiveGateway exclusiveGateway1 = new ExclusiveGateway();
        exclusiveGateway1.setKey("ExclusiveGateway_1l0d11b");
        exclusiveGateway1.setType(6);
        List<String> egIncomings = new ArrayList();
        egIncomings.add("SequenceFlow_06uq82c");
        exclusiveGateway1.setIncoming(egIncomings);
        List<String> egOutgoings = new ArrayList();
        egOutgoings.add("SequenceFlow_15vyyaj");
        egOutgoings.add("SequenceFlow_168uou3");
        exclusiveGateway1.setOutgoing(egOutgoings);
        Map<String, Object> properties = new HashMap();
        properties.put("hookInfoIds", "");
        exclusiveGateway1.setProperties(properties);
        flowElementList.add(exclusiveGateway1);

        UserTask userTask2 = new UserTask();
        userTask2.setKey("UserTask_0j0wc1o");
        userTask2.setType(4);
        List<String> utIncomings2 = new ArrayList();
        utIncomings2.add("SequenceFlow_15vyyaj");
        userTask2.setIncoming(utIncomings2);
        List<String> utOutgoings2 = new ArrayList();
        utOutgoings2.add("SequenceFlow_18y740t");
        userTask2.setOutgoing(utOutgoings2);
        flowElementList.add(userTask2);

        UserTask userTask3 = new UserTask();
        userTask3.setKey("UserTask_05t37q8");
        userTask3.setType(4);
        List<String> utIncomings3 = new ArrayList();
        utIncomings3.add("SequenceFlow_168uou3");
        userTask3.setIncoming(utIncomings3);
        List<String> utOutgoings3 = new ArrayList();
        utOutgoings3.add("SequenceFlow_086u2jq");
        userTask3.setOutgoing(utOutgoings3);
        flowElementList.add(userTask3);

        EndEvent endEvent1 = new EndEvent();
        endEvent1.setKey("EndEvent_1m02l29");
        endEvent1.setType(3);
        List<String> incomings = new ArrayList();
        incomings.add("SequenceFlow_18y740t");
        endEvent1.setIncoming(incomings);
        flowElementList.add(endEvent1);

        EndEvent endEvent2 = new EndEvent();
        endEvent2.setKey("EndEvent_07cgwru");
        endEvent2.setType(3);
        List<String> incomings2 = new ArrayList();
        incomings2.add("SequenceFlow_086u2jq");
        endEvent2.setIncoming(incomings2);
        flowElementList.add(endEvent2);

        SequenceFlow sequenceFlow1 = new SequenceFlow();
        sequenceFlow1.setKey("SequenceFlow_1udf5vg");
        sequenceFlow1.setType(1);
        List<String> sfIncomings1 = new ArrayList();
        sfIncomings1.add("StartEvent_0ofi5hg");
        sequenceFlow1.setIncoming(sfIncomings1);
        List<String> sfOutgoings1 = new ArrayList();
        sfOutgoings1.add("UserTask_1625vn7");
        sequenceFlow1.setOutgoing(sfOutgoings1);
        Map<String, Object> properties1 = new HashMap();
        properties1.put("defaultConditions", "false");
        properties1.put("conditionsequenceflow", "");
        sequenceFlow1.setProperties(properties1);
        flowElementList.add(sequenceFlow1);

        SequenceFlow sequenceFlow2 = new SequenceFlow();
        sequenceFlow2.setKey("SequenceFlow_06uq82c");
        sequenceFlow2.setType(1);
        List<String> sfIncomings2 = new ArrayList();
        sfIncomings2.add("UserTask_1625vn7");
        sequenceFlow2.setIncoming(sfIncomings2);
        List<String> sfOutgoings2 = new ArrayList();
        sfOutgoings2.add("ExclusiveGateway_1l0d11b");
        sequenceFlow2.setOutgoing(sfOutgoings2);
        Map<String, Object> properties2 = new HashMap();
        properties2.put("defaultConditions", "false");
        properties2.put("conditionsequenceflow", "");
        sequenceFlow2.setProperties(properties2);
        flowElementList.add(sequenceFlow2);

        SequenceFlow sequenceFlow3 = new SequenceFlow();
        sequenceFlow3.setKey("SequenceFlow_18y740t");
        sequenceFlow3.setType(1);
        List<String> sfIncomings3 = new ArrayList();
        sfIncomings3.add("UserTask_0j0wc1o");
        sequenceFlow3.setIncoming(sfIncomings3);
        List<String> sfOutgoings3 = new ArrayList();
        sfOutgoings3.add("EndEvent_1m02l29");
        sequenceFlow3.setOutgoing(sfOutgoings3);
        Map<String, Object> properties3 = new HashMap();
        properties3.put("defaultConditions", "false");
        properties3.put("conditionsequenceflow", "");
        sequenceFlow3.setProperties(properties3);
        flowElementList.add(sequenceFlow3);

        SequenceFlow sequenceFlow4 = new SequenceFlow();
        sequenceFlow4.setKey("SequenceFlow_086u2jq");
        sequenceFlow4.setType(1);
        List<String> sfIncomings4 = new ArrayList();
        sfIncomings4.add("UserTask_05t37q8");
        sequenceFlow4.setIncoming(sfIncomings4);
        List<String> sfOutgoings4 = new ArrayList();
        sfOutgoings4.add("EndEvent_07cgwru");
        sequenceFlow4.setOutgoing(sfOutgoings4);
        Map<String, Object> properties4 = new HashMap();
        properties4.put("defaultConditions", "false");
        properties4.put("conditionsequenceflow", "");
        sequenceFlow4.setProperties(properties4);
        flowElementList.add(sequenceFlow4);

        SequenceFlow sequenceFlow5 = new SequenceFlow();
        sequenceFlow5.setKey("SequenceFlow_15vyyaj");
        sequenceFlow5.setType(1);
        List<String> sfIncomings5 = new ArrayList();
        sfIncomings5.add("ExclusiveGateway_1l0d11b");
        sequenceFlow5.setIncoming(sfIncomings5);
        List<String> sfOutgoings5 = new ArrayList();
        sfOutgoings5.add("UserTask_0j0wc1o");
        sequenceFlow5.setOutgoing(sfOutgoings5);
        Map<String, Object> properties5 = new HashMap();
        properties5.put("defaultConditions", "false");
        properties5.put("conditionsequenceflow", "message.equals(\"open\")");
        sequenceFlow5.setProperties(properties5);
        flowElementList.add(sequenceFlow5);

        SequenceFlow sequenceFlow6 = new SequenceFlow();
        sequenceFlow6.setKey("SequenceFlow_168uou3");
        sequenceFlow6.setType(1);
        List<String> sfIncomings6 = new ArrayList();
        sfIncomings6.add("ExclusiveGateway_1l0d11b");
        sequenceFlow6.setIncoming(sfIncomings6);
        List<String> sfOutgoings6 = new ArrayList();
        sfOutgoings6.add("UserTask_05t37q8");
        sequenceFlow6.setOutgoing(sfOutgoings6);
        Map<String, Object> properties6 = new HashMap();
        properties6.put("defaultConditions", "true");
        properties6.put("conditionsequenceflow", "");
        sequenceFlow6.setProperties(properties6);
        flowElementList.add(sequenceFlow6);

        FlowModel flowModel = new FlowModel();
        flowModel.setFlowElementList(flowElementList);

        updateFlowParam.setFlowModel(JSON.toJSONString(flowModel));
        updateFlowResult = processEngine.updateFlow(updateFlowParam);
        System.out.println("updateFlow.||updateFlowResult=" + updateFlowResult);

//        LOGGER.info("updateFlow.||updateFlowResult={}", updateFlowResult);
    }

    private void deployFlow() {
        DeployFlowParam param = new DeployFlowParam(tenant, caller);
        param.setFlowModuleId(createFlowResult.getFlowModuleId());
        deployFlowResult = processEngine.deployFlow(param);
        System.out.println("deployFlow.||deployFlowResult=" + deployFlowResult);

//        LOGGER.info("deployFlow.||deployFlowResult={}", deployFlowResult);
    }

    private void startProcessToEnd() {
        StartProcessResult startProcess = startProcess();
        CommitTaskResult commitTaskResult = inputTime(startProcess);
        RollbackTaskResult rollbackTaskResult = rollbackToInputTime(commitTaskResult);
        CommitTaskResult result = inputTimeAgain(rollbackTaskResult);
        commitCompleteProcess(result);
    }

    // 用户拉起请假sop
    private StartProcessResult startProcess() {
        StartProcessParam startProcessParam = new StartProcessParam();
        startProcessParam.setFlowDeployId(deployFlowResult.getFlowDeployId());
        List<InstanceData> variables = new ArrayList<>();
        variables.add(new InstanceData("user_name", "请假人名字"));
        startProcessParam.setVariables(variables);
        StartProcessResult startProcessResult = processEngine.startProcess(startProcessParam);

        LOGGER.info("startProcess.||startProcessResult={}", startProcessResult);
        return startProcessResult;
    }

    // 输入请假天数
    private CommitTaskResult inputTime(StartProcessResult startProcessResult) {
        CommitTaskParam commitTaskParam = new CommitTaskParam();
        commitTaskParam.setFlowInstanceId(startProcessResult.getFlowInstanceId());
        commitTaskParam.setTaskInstanceId(startProcessResult.getActiveTaskInstance().getNodeInstanceId());
        List<InstanceData> variables = new ArrayList<>();
        variables.add(new InstanceData("user_name", "请假人名字"));
        variables.add(new InstanceData("n", 1));
        commitTaskParam.setVariables(variables);

        CommitTaskResult commitTaskResult = processEngine.commitTask(commitTaskParam);
        LOGGER.info("inputTime.||commitTaskResult={}", commitTaskResult);
        return commitTaskResult;
    }

    // 请假撤回
    private RollbackTaskResult rollbackToInputTime(CommitTaskResult commitTaskResult) {
        RollbackTaskParam rollbackTaskParam = new RollbackTaskParam();
        rollbackTaskParam.setFlowInstanceId(commitTaskResult.getFlowInstanceId());
        rollbackTaskParam.setTaskInstanceId(commitTaskResult.getActiveTaskInstance().getNodeInstanceId());
        RollbackTaskResult rollbackTaskResult = processEngine.rollbackTask(rollbackTaskParam);

        LOGGER.info("rollbackToInputTime.||rollbackTaskResult={}", rollbackTaskResult);
        return rollbackTaskResult;
    }

    // 填写请假天数
    private CommitTaskResult inputTimeAgain(RollbackTaskResult rollbackTaskResult) {
        CommitTaskParam commitTaskParam = new CommitTaskParam();
        commitTaskParam.setFlowInstanceId(rollbackTaskResult.getFlowInstanceId());
        commitTaskParam.setTaskInstanceId(rollbackTaskResult.getActiveTaskInstance().getNodeInstanceId());
        List<InstanceData> variables = new ArrayList<>();
        variables.add(new InstanceData("user_name", "请假人名字"));
        variables.add(new InstanceData("n", 2));
        commitTaskParam.setVariables(variables);

        CommitTaskResult commitTaskResult = processEngine.commitTask(commitTaskParam);
        LOGGER.info("inputTimeAgain.||commitTaskResult={}", commitTaskResult);
        return commitTaskResult;
    }

    // BadCase：已完成流程，继续执行流程会失败。
    private CommitTaskResult commitCompleteProcess(CommitTaskResult commitTaskResult) {
        CommitTaskParam commitTaskParam = new CommitTaskParam();
        commitTaskParam.setFlowInstanceId(commitTaskResult.getFlowInstanceId());
        commitTaskParam.setTaskInstanceId(commitTaskResult.getActiveTaskInstance().getNodeInstanceId());
        List<InstanceData> variables = new ArrayList<>();
        variables.add(new InstanceData("user_name", "请假人名字"));
        variables.add(new InstanceData("n", 4));
        commitTaskParam.setVariables(variables);

        CommitTaskResult result = processEngine.commitTask(commitTaskParam);
        LOGGER.info("inputTimeBadCase.||CommitTaskResult={}", result);
        return result;
    }

}
