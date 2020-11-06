package com.yxy.nova.web;

import com.yxy.nova.web.util.MyStringUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yuxiaoyu
 * @date 2020/11/6 上午11:05
 * @Description
 */
@Controller
@RequestMapping(value = "/fs")
public class FreeSwitchController {
    private Logger logger = LoggerFactory.getLogger(getClass());

//    public static void main(String[] args) throws IOException, DocumentException {
//        createDirection();
//    }

    @RequestMapping(value = "/directory")
    @ResponseBody
    public String directory(HttpServletRequest request) throws IOException, DocumentException {
        logger.info("directory ………………{}", request.getQueryString());
        Map<String, String> map = new HashMap<>();
        map.put("sip", "66600000");
        map.put("password", "1234");

        Path path = Paths.get("/etc/freeswitch/directory/direction_example.xml");
        String template = MyStringUtil.replaceArgsNew(new String(Files.readAllBytes(path)), map);

        return generateXml("/tmp/66600000.xml", template).asXML();

    }






    @GetMapping("/createdirection")
    public void createDirection() throws IOException, DocumentException {
        Map<String, String> map = new HashMap<>();
        map.put("sip", "66600000");
        map.put("password", "1234");

        Path path = Paths.get("/tmp/direction_example_back.xml");
        String template = MyStringUtil.replaceArgsNew(new String(Files.readAllBytes(path)), map);

        generateXml("/tmp/66600000.xml", template);

    }

    private Document generateXml(String s, String template) throws DocumentException {
        Document document = DocumentHelper.parseText(template);
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setNewLineAfterDeclaration(false);
        /** 指定XML编码 */
        format.setEncoding("UTF-8");
        format.setSuppressDeclaration(true);       //删除xml头
        /** 将document中的内容写入文件中 */
        XMLWriter writer;
        try {
            writer = new XMLWriter(new FileWriter(new File(s)), format);
            writer.write(document);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
        return document;
    }

}
