package test.yxy;

import java.io.Serializable;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @description: 分时指标
 * @author: shui.ren
 * @date: 2019-07-08 下午2:44
 */
public class TimeMetric implements Serializable {

    private static final long serialVersionUID = -8907188836555209332L;

    public TimeMetric(String time, long count) {
        this.time = time;
        this.count = count;
    }
    /**
     * 格式：HH:mm
     */
    private String time;

    /**
     * 总个数
     */
    private long count;

    /**
     * 指标值的和
     */
    private long sum;

    /**
     * 指标数据
     */
    private Map<String, Long> metric;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Map<String, Long> getMetric() {
        return metric;
    }

    public void setMetric(Map<String, Long> metric) {
        this.metric = metric;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getSum() {
        return sum;
    }

    public void setSum(long sum) {
        this.sum = sum;
    }
}
