package com.yxy.nova.mwh.elasticsearch.policy.partition;

import com.yxy.nova.mwh.elasticsearch.basic.where.*;
import com.yxy.nova.mwh.elasticsearch.exception.ESExceptionType;
import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import com.yxy.nova.mwh.elasticsearch.policy.vo.PartitionFieldValue;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 按日分区策略
 * @author quyuanwen
 */
public abstract class AbstractTimePartitionPolicy extends AbstractPartitionPolicy {


   public AbstractTimePartitionPolicy(JSONObject policyConfig) throws ElasticsearchClientException {
        super(policyConfig);
    }

    @Override
    public void analysis(JSONObject partitionConfig) {

    }
    /**
     * 获取开始时间
     * @param partitionFieldValue
     * @return
     * @throws ElasticsearchClientException
     */
    protected long getStartTime(PartitionFieldValue partitionFieldValue)  throws ElasticsearchClientException{
        WhereGreater greater =  partitionFieldValue.getGreater();
        Object value = null;
        if(greater == null){
            WhereGreaterOrEqual greaterOrEqual = partitionFieldValue.getGreaterOrEqual();
            if(greaterOrEqual != null){
                value = greaterOrEqual.getValue();
            }
        }else{
            value = greater.getValue();
        }
        if(value != null ){
            return transformDate(value);
        }
        return 0;
    }

    /**
     * 获取结束时间
     * @param partitionFieldValue
     * @return
     * @throws ElasticsearchClientException
     */
    protected long getEndTime(PartitionFieldValue partitionFieldValue)  throws ElasticsearchClientException{
        WhereLess less = partitionFieldValue.getLess();
        Object value = null;
        if(less == null){
            WhereLessOrEqual lessOrEqual = partitionFieldValue.getLessOrEqual();
            if(lessOrEqual != null){
                value = lessOrEqual.getValue();
            }
        }else{
            value = less.getValue();
        }
        if(value !=null ){
            return transformDate(value);
        }
        return 0;
    }

    /**
     * 得到日期信息
     *
     * @param partitionFieldValue
     * @return
     * @throws ElasticsearchClientException
     */
    protected long getTime(PartitionFieldValue partitionFieldValue)  throws ElasticsearchClientException{
        WhereEquals equals = partitionFieldValue.getEquals();
        Object value = null;
        if(equals != null ){
            value = equals.getValue();
        }
        if(value !=null ){
            return transformDate(value);
        }
        return 0;
    }

    /**
     * 得到全部日志信息
     * @param partitionFieldValue
     * @return
     * @throws ElasticsearchClientException
     */
    protected List<Long> getTimes(PartitionFieldValue partitionFieldValue)  throws ElasticsearchClientException{
        WhereIn in = partitionFieldValue.getIn();
        if(in != null ){
            Collection<Object> values = in.getValue();
            if(!CollectionUtils.isEmpty(values)){
                List<Long> dates = new ArrayList<>(values.size());
                for(Object value:values){
                    if(value !=null ){
                        dates.add(transformDate(value));
                    }
                }
                return dates;
            }
        }
        return null;
    }


    /**
     * 日期转换
     * @param value
     * @return
     * @throws ElasticsearchClientException
     */
    protected long transformDate(Object value) throws ElasticsearchClientException{
        if(value instanceof String){
            String str = (String)value;
            if(StringUtils.isNumeric(str)){
                return Long.parseLong(str);
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                return formatter.parse(str).getTime();
            } catch (ParseException e) {
                throw new ElasticsearchClientException("时间分区的字段格式应为:yyyy-MM-dd HH:mm:ss", ESExceptionType.POLICY_ERROR,e);
            }
        }else if(value instanceof Long){
            return (Long) value;
        }else{
            throw new ElasticsearchClientException("时间分区的字段类型错误", ESExceptionType.POLICY_ERROR);
        }
    }
}
