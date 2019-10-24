package com.yxy.nova.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author yuxiaoyu
 * @Description:
 * @date 2019-08-27 10:21
 */
public class TestVo implements Serializable {
    private String str;
    private List<String> list;
    private Boolean bl;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Boolean getBl() {
        return bl;
    }

    public void setBl(Boolean bl) {
        this.bl = bl;
    }
}
