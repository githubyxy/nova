package com.yxy.nova.web;

import com.alibaba.fastjson.JSON;
import com.yxy.nova.bean.TestVo;
import com.yxy.nova.util.LinuxUtil;
import com.yxy.nova.util.QRCode;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author yuxiaoyu
 * @Description:
 * @date 2019-07-05 15:39
 */
@Controller
public class NovaController {

    @GetMapping(value = "index")
    public String gotoPage(Model model){
        try {
            model.addAttribute("fortune", LinuxUtil.fortune());
        } catch (Exception e) {
        }
        return "index";
    }

    @GetMapping(value = "download")
    public void download(String text, HttpServletRequest request, HttpServletResponse response) throws Exception{
        if (StringUtils.isBlank(text)) {
            PrintWriter out = null;
            try {
                //这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859
                response.setCharacterEncoding("utf-8");
                //这句话的意思，是让浏览器用utf8来解析返回的数据
                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                //这句话一定要在setCharacterEncoding之后
                out = response.getWriter();
                out.print(JSON.toJSONString("输入内容啦！"));
            } catch (IOException e) {

            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }

        String fileName = UUID.randomUUID() +".png";
        QRCode handler = new QRCode();

        OutputStream out = null;
        try {
            out = response.getOutputStream();
            String userAgent = request.getHeader("User-Agent").toUpperCase();
            if (userAgent.contains("MSIE") || userAgent.contains("EDGE")) {
                fileName = URLEncoder.encode(fileName, "UTF-8");// IE浏览器
            } else {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            }
            response.reset();
            response.setContentType("application/octet-stream; charset=utf-8");
            response.addHeader("Content-Disposition", String.format(" attachment; filename=\"%s\"", fileName));
            response.setCharacterEncoding("utf-8");
            handler.encoderQRCode(text, out, "png");
        } catch (IOException e) {

        }

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
