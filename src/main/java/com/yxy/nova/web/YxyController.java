package com.yxy.nova.web;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yuxiaoyu
 * @Description:
 * @date 2019-07-05 15:39
 */
@Controller
public class YxyController {

    @GetMapping(value = "index")
    public String gotoPage(){
        return "index";
    }
    @GetMapping(value = "index/test1")
    @ResponseBody
    public JSONObject test1(){
        JSONObject object = new JSONObject();
        object.put("name", "my");
        return object;
    }

}
