package com.yxy.nova.web;

import com.yxy.nova.bean.TestVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author yuxiaoyu
 * @Description:
 * @date 2019-07-05 15:39
 */
@Controller
public class YxyController {

    @GetMapping(value = "index")
    public String gotoPage(Model model){
        model.addAttribute("name", "yuxiaoyu");
        return "index";
    }

    @GetMapping(value = "index/test1")
    @ResponseBody
    public TestVo test1(@RequestParam(required = false) String str){
        // System.out.println(str);
        return new TestVo();
    }

    @PostMapping(value = "index/test2")
    @ResponseBody
    public TestVo test2(@RequestBody TestVo vo){
        // System.out.println(JSON.toJSONString(vo));
        return new TestVo();
    }

    /**
     * 用于接收前端上传文件
     * @param file
     */
    // @RequestMapping(value = "upload", method = RequestMethod.POST)
    // public void dome1(MultipartFile file) throws Exception{
    //     // System.out.println(file.getSize());
    // }

}
