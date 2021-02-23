package com.yxy.nova.mwh.elasticsearch.policy.partition;

import com.yxy.nova.mwh.elasticsearch.exception.ESExceptionType;
import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import com.yxy.nova.mwh.elasticsearch.policy.vo.PartitionFieldValue;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 按月分区策略
 * @author quyuanwen
 */
public class MonthPartitionPolicy extends AbstractTimePartitionPolicy {



    private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("yyyyMM");

    public MonthPartitionPolicy(JSONObject policyConfig) throws ElasticsearchClientException {
        super(policyConfig);
    }

    @Override
    public void analysis(JSONObject partitionConfig) {

    }

    @Override
    protected Set<String> generateReadAliases(PartitionFieldValue partitionFieldValue, String indexReadPrefix, long searchLimit, boolean ignoreLimit) throws ElasticsearchClientException {
        long startTime = this.getStartTime(partitionFieldValue);
        long endTime = this.getEndTime(partitionFieldValue);
        Set<String> readIndices = new HashSet<>();
        if(startTime != 0 || endTime != 0){
            if(startTime <= 0){
                startTime = endTime;
            }else if(endTime <= 0) {
                endTime = startTime;
            }
            String startMonth = DATE_FORMAT.format(startTime);
            String endMonth = DATE_FORMAT.format(endTime);
            if(startMonth.equals(endMonth)){
                readIndices.add(indexReadPrefix + startMonth);
            }else{
                int start = Integer.parseInt(startMonth);
                int end = Integer.parseInt(endMonth);

                for (; start < end; ) {
                    readIndices.add(indexReadPrefix + start);
                    //计算下一个日期
                    start++;
                    int month = start%100;
                    if(month > 12){
                        start += 88;
                    }
                }
                //获取别名
                readIndices.add(indexReadPrefix + endMonth);
            }

            return readIndices;
        }
        long time = this.getTime(partitionFieldValue);
        if(time != 0){
            readIndices.add(indexReadPrefix + DATE_FORMAT.format(time));
            return readIndices;
        }
        List<Long> times = this.getTimes(partitionFieldValue);
        if(times != null && times.size() > 0){
            for(long temp : times){
                readIndices.add(indexReadPrefix + DATE_FORMAT.format(temp));
            }
        }
        return readIndices;
    }

    @Override
    protected Set<String> generateDefaultReadAliases(String indexReadPrefix, boolean ignoreLimit) throws ElasticsearchClientException {

        Set<String> readIndices = new HashSet<>();
        if(!ignoreLimit){
            //默认查当前月份
            readIndices.add(indexReadPrefix + DATE_FORMAT.format(new Date().getTime()));
            return readIndices;
        }
        readIndices.add(indexReadPrefix + "*");
        return readIndices;
    }

    @Override
    protected String generateWriteAlias(PartitionFieldValue partitionFieldValue, String indexWritePrefix) throws ElasticsearchClientException {
        long time = this.getTime(partitionFieldValue);
        if(time != 0){
            return indexWritePrefix + DATE_FORMAT.format(time);
        }else{
            throw new ElasticsearchClientException("生产写索引错误", ESExceptionType.INDEX_ERROR);
        }
    }
}
