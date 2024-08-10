package test.doris;

import java.io.Serializable;

/**
 * @author shui.ren
 * @description: 城市区域DTO
 * @date 2024/7/30 10:34
 */
public class CityAreaDTO implements Serializable {

    private static final long serialVersionUID = 3786399140351387515L;

    /**
     * 城市区域编码
     */
    private long cityAreaCode;

    /**
     * 城市区域名称
     */
    private String city;

    public long getCityAreaCode() {
        return cityAreaCode;
    }

    public void setCityAreaCode(long cityAreaCode) {
        this.cityAreaCode = cityAreaCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
