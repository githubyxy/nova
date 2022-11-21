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
 * 按日分区策略
 * @author quyuanwen
 */
public class DayPartitionPolicy extends AbstractTimePartitionPolicy {

    /**
     * 默认查7天数据
     */
    public final static long DEFAULT_SPACE = 604800000L;

    /**
     * 按天递增
     */
    public final static long ONE_DAY = 86400000L;


    private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("yyyyMMdd");

    public DayPartitionPolicy(JSONObject policyConfig) throws ElasticsearchClientException {
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
                startTime = endTime - DEFAULT_SPACE;
            }else if(endTime <= 0){
                endTime = startTime + DEFAULT_SPACE;
            }else  if(endTime - startTime > searchLimit ){//判断查询范围是不是超过限制
                if(!ignoreLimit){
                    throw new ElasticsearchClientException("查询时间范围过大", ESExceptionType.INDEX_ERROR);
                }
            }

            //获取别名
            for (; startTime < endTime; startTime += ONE_DAY) {
                readIndices.add(indexReadPrefix + DATE_FORMAT.format(startTime));
            }
            readIndices.add(indexReadPrefix + DATE_FORMAT.format(startTime));
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
            long endTime = new Date().getTime();
            long startTime = endTime - DEFAULT_SPACE;
            //获取别名
            for (; startTime < endTime; startTime += ONE_DAY) {
                readIndices.add(indexReadPrefix + DATE_FORMAT.format(startTime));
            }
            readIndices.add(indexReadPrefix + DATE_FORMAT.format(startTime));
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
