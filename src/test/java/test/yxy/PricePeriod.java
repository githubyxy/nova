package test.yxy;

import java.io.Serializable;

/**
 * @author yxy
 * @description: 每个时间段的价格
 * @date 2024/7/29 17:30
 */
public class PricePeriod implements Serializable {


    private static final long serialVersionUID = -5025400762419330569L;

    /**
     * 开始时间， 格式: yyyy-MM-dd
     */
    private String startTime;

    /**
     * 价格(单位：元)
     */
    private String price;

    /**
     * 价格(单位：厘)
     */
    private Long priceLi;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Long getPriceLi() {
        return priceLi;
    }

    public void setPriceLi(Long priceLi) {
        this.priceLi = priceLi;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
