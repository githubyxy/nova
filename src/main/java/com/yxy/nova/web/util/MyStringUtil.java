package com.yxy.nova.web.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yuxiaoyu
 * @date 2020/11/6 上午11:18
 * @Description
 */
public class MyStringUtil {
    public static String replaceArgsNew(String template, Map<String, String> data) {

        // sb用来存储替换过的内容，它会把多次处理过的字符串按源字符串序 存储起来。

        StringBuffer sb = new StringBuffer();

        try {

            Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");

            Matcher matcher = pattern.matcher(template);

            while (matcher.find()) {

                String name = matcher.group(1);// 键名

                String value = (String) data.get(name);// 键值

                if (value != null) {

                    value = value.replace("\\", "\\\\\\");

                    value = value.replace("$", "\\$");

                    matcher.appendReplacement(sb, value);

                }

            }

            matcher.appendTail(sb);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return sb.toString();
    }
}
