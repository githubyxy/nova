package com.yxy.nova.mwh.elasticsearch.policy;

import com.yxy.nova.mwh.elasticsearch.admin.Connection;
import com.yxy.nova.mwh.elasticsearch.dto.Config;
import com.yxy.nova.mwh.elasticsearch.exception.ESExceptionType;
import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import com.yxy.nova.mwh.elasticsearch.policy.partition.PartitionPolicy;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 策略工厂，根据配置获得具体策略对象
 * @author quyuanwen
 */
public class PolicyFactory {

    private static Logger logger = LoggerFactory.getLogger(PolicyFactory.class);;
    /**
     * 获取策略
     * @param config
     * @return
     * @throws ElasticsearchClientException
     */
    public final static Policy generate(Config config, Connection connection) throws ElasticsearchClientException{
        JSONObject policyJson = config.getPolicy();
        if(policyJson == null){
            throw new ElasticsearchClientException("获取路由策略为空", ESExceptionType.POLICY_NULL);
        }
        String policyClass = policyJson.getString("class");
        if(policyClass.equals("com.yxy.nova.mwh.elasticsearch.policy.SingleIndexPolicy")){
            try {
                Class clazz= Class.forName(policyClass);
                Constructor<SingleIndexPolicy> con = clazz.getConstructor(JSONObject.class, String.class) ;
                SingleIndexPolicy singleIndexPolicy= con.newInstance(policyJson, config.getClusterName());
                singleIndexPolicy.init(connection);
                return singleIndexPolicy;
            } catch (ClassNotFoundException e) {
                logger.error("路由策略类不存在,class:" + policyClass,e);
                throw new ElasticsearchClientException("没有配置路由策略类", ESExceptionType.POLICY_ERROR);
            }catch (NoSuchMethodException e) {
                logger.error("路由策略构造函数不存在,class:" + policyClass,e);
                throw new ElasticsearchClientException("路由策略构造函数错误", ESExceptionType.POLICY_ERROR);
            } catch (InstantiationException e) {
                logger.error("初始化路由策略错误,class:" + policyClass,e);
                throw new ElasticsearchClientException("初始化路由策略错误", ESExceptionType.POLICY_ERROR);
            } catch (IllegalAccessException e) {
                logger.error("初始化路由策略错误,class:" + policyClass,e);
                throw new ElasticsearchClientException("初始化路由策略错误", ESExceptionType.POLICY_ERROR);
            } catch (InvocationTargetException e) {
                logger.error("初始化路由策略错误,class:" + policyClass,e);
                throw new ElasticsearchClientException("初始化路由策略错误", ESExceptionType.POLICY_ERROR);
            }
        }
        Policy policy = new IndexRoutePolicy(policyJson, config.getClusterName());
        policy.init(connection);
        return policy;
    }


    /**
     * 获取路由策略
     *
     * @param indexConfige
     * @return
     * @throws ElasticsearchClientException
     */
    public final static PartitionPolicy getPartitionPolicy(JSONObject indexConfige) throws ElasticsearchClientException {
        String partitionClass = indexConfige.getString("class");
        if(StringUtils.isBlank(partitionClass)){
            throw new ElasticsearchClientException("没有配置路由策略类", ESExceptionType.POLICY_ERROR);
        }
        try {
            Class clazz= Class.forName(partitionClass);
            Constructor<PartitionPolicy> con = clazz.getConstructor(JSONObject.class) ;
            PartitionPolicy partitionPolicy= con.newInstance(indexConfige);
            return partitionPolicy;
        } catch (ClassNotFoundException e) {
            logger.error("路由策略类不存在,class:" + partitionClass,e);
            throw new ElasticsearchClientException("没有配置路由策略类", ESExceptionType.POLICY_ERROR);
        }catch (NoSuchMethodException e) {
            logger.error("路由策略构造函数不存在,class:" + partitionClass,e);
            throw new ElasticsearchClientException("路由策略构造函数错误", ESExceptionType.POLICY_ERROR);
        } catch (InstantiationException e) {
            logger.error("初始化路由策略错误,class:" + partitionClass,e);
            throw new ElasticsearchClientException("初始化路由策略错误", ESExceptionType.POLICY_ERROR);
        } catch (IllegalAccessException e) {
            logger.error("初始化路由策略错误,class:" + partitionClass,e);
            throw new ElasticsearchClientException("初始化路由策略错误", ESExceptionType.POLICY_ERROR);
        } catch (InvocationTargetException e) {
            logger.error("初始化路由策略错误,class:" + partitionClass,e);
            throw new ElasticsearchClientException("初始化路由策略错误", ESExceptionType.POLICY_ERROR);
        }
    }
}
