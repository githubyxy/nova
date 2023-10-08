package com.yxy.nova.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @description:
 * @author: renshui
 * @date: 2020-12-23 10:36 下午
 */
public class SmsTemplateUtil {

    private static Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{(.*?)}");

    /**
     * 验证短信内容content是否匹配传入的模板templateContent
     * @param templateContent
     * @param content
     * @return
     */
    public static boolean match(String templateContent, String content) {
        templateContent = StringUtils.trimToEmpty(templateContent);

        // 获取被${}分隔的纯文本片段
        List<String> literalTextPartList = getLiteralTextPartList(templateContent);

        // 去除纯文本中的标点符号（空格保留）
        literalTextPartList = literalTextPartList.stream()
                .map(text -> text.replaceAll("[\\pP\\p{Punct}]",""))
                .collect(Collectors.toList());

        // 拼装匹配content的正则表达式
        String regexContent = "";
        if (literalTextPartList.size() == 1) {
            regexContent = literalTextPartList.get(0) + "(.?)+";
        } else {
            regexContent = StringUtils.join(literalTextPartList, "(.?)+");
        }
        String regex = String.format("^%s$", regexContent);

        // 去除短信内容中的标点符号（空格保留）
        content = StringUtils.trimToEmpty(content);
        content = content.replaceAll("[\\pP\\p{Punct}]","");

        // 校验是否匹配
        return content.matches(regex);
    }

    public static void main(String[] args) {
        System.out.println(match("你好，", "你好，于晓宇，是的于晓宇2"));
    }

    /**
     * 返回被${}分隔的纯文本片段
     * @param templateContent
     * @return
     */
    private static List<String>  getLiteralTextPartList(String templateContent) {
        List<String> literalTextPartList = new ArrayList<>();

        int  offset  = 0;
        Matcher matcher = VARIABLE_PATTERN.matcher(templateContent);
        while (matcher.find()) {
            int start = matcher.start();
            if (offset < start) {
                // [offset, start)中的是纯文本
                String str = templateContent.substring(offset, start);
                if(StringUtils.isNotBlank(str)){
                    literalTextPartList.add(StringUtils.trimToEmpty(str));
                }
            }
            offset = matcher.end();
        }

        if (offset < templateContent.length()) {
            literalTextPartList.add(StringUtils.trimToEmpty(templateContent.substring(offset)));
        }

        return literalTextPartList;
    }

}
