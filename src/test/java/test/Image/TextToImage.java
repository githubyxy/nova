package test.Image;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TextToImage {
    public static void main(String[] args) {
        try {
            // 定义表头
            String[] headers = {"客户业务", "进件数", "进件数对比", "推送执行数", "推送执行数对比","接通率","接通率对比","A数据","A占比","A占比对比","计费分钟数","计费分钟数对比","短信发送数","短信比例","短信比例对比","短信成功数",
                    "短信成功率","短信成功率对比","短信计费条数","线路成本"};

            // 准备数据
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"客户业务", "进件数", "进件数对比", "推送执行数", "推送执行数对比","接通率","接通率对比","A数据","A占比","A占比对比","计费分钟数","计费分钟数对比","短信发送数","短信比例","短信比例对比","短信成功数",
                    "短信成功率","短信成功率对比","短信计费条数","线路成本"});
            data.add(new String[]{"G客户P2", "37", "37","37", "37","-37", "37","37", "37","37", "37","37", "37", "13.51%", "提件执行率对比上一周期0%"});
            data.add(new String[]{"     G客户P2G客户P2G客户P2", "37", "37","37", "37","+37", "37","37", "37","37", "37","37", "37", "13.51%", "提件执行率对比上一周期0%"});


            // 保存图片
            String today = DateUtil.formatDate(new Date());
            String todayPath = "/taskItemCustomerDetailReport/" + today + "/" + DatePattern.PURE_DATETIME_FORMAT.format(new Date()) + ".png";
            File file = new File("/tmp" + todayPath);
            FileUtil.mkParentDirs(file);
            ImageIO.write(TableImageGenerator.generateTableImage(headers, data), "png", file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
