package com.yxy.nova.web;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author yuxiaoyu
 * @Description:
 * @date 2019-07-05 15:39
 */
@Controller
@EnableAutoConfiguration
public class YxyController {

    @GetMapping(value = "page")
    public ModelAndView gotoPage(){
        return new ModelAndView("index");
    }

}
