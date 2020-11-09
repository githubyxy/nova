package com.yxy.nova.freeswitch.cmd;

/**
 * Created by xiaobin.wu on 2018/9/4 下午6:02.
 */
public class Sofia implements FSCmd{

    public interface Args {
        String STATUS_PROFILE_INTERNAL_REG = "status profile internal reg ";
    }

    private String args;

    public Sofia(String args) {
        this.args = args;
    }

    @Override
    public String cmd() {
        return "sofia";
    }

    @Override
    public String args() {
        return args;
    }
}
