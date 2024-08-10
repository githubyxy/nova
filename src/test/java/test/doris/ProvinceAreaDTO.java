package test.doris;

import java.io.Serializable;
import java.util.List;

/**
 * @author shui.ren
 * @description: 省区域DTO
 * @date 2024/7/30 10:34
 */
public class ProvinceAreaDTO implements Serializable {

    private static final long serialVersionUID = 3786399140351387515L;

    /**
     * 省份区域编码
     */
    private long provinceAreaCode;

    /**
     * 省份区域名称
     */
    private String province;

    /**
     * 城市区域集合
     */
    private List<CityAreaDTO> cities;



    public long getProvinceAreaCode() {
        return provinceAreaCode;
    }

    public void setProvinceAreaCode(long provinceAreaCode) {
        this.provinceAreaCode = provinceAreaCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public List<CityAreaDTO> getCities() {
        return cities;
    }

    public void setCities(List<CityAreaDTO> cities) {
        this.cities = cities;
    }
}
