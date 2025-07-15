package com.yxy.nova.mwh.utils.text;

import com.yxy.nova.mwh.utils.exception.BizException;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @description: 文本处理相关的类
 * @author: renshui
 * @date: 2020-05-24 11:16 上午
 */
public class TextUtil {

    /**
     * 替换模板中的变量返回字符串。如果模板中含有没有传值的变量，会抛异常。
     * @param source
     * @param valueMap
     * @return
     */
    public static String replace(CharSequence source, Map<String, String> valueMap) {
        if (valueMap == null) {
            valueMap = new HashMap<>(0);
        }

        StringSubstitutor substitutor = new StringSubstitutor(valueMap);
        substitutor.setEnableUndefinedVariableException(false);
        return substitutor.replace(source);
    }

    /**
     * 读取类路径文件内容
     * @param filePath
     * @return
     */
    @SneakyThrows
    public static String readClassPathFileContent(String filePath) {
        return StreamUtils.copyToString( new ClassPathResource(filePath).getInputStream(), Charset.defaultCharset());
    }

    /**
     * 去除所有标点符号。只留下字母,数字,汉字  共3类
     * @param text
     * @return
     */
    public static String removePunctuation(String text) {
        return text.replaceAll("[\\s\\pP\\p{Punct}]","");
    }

    /**
     * 按照中英文分号分隔字符串
     * @param text
     * @return
     */
    public static List<String> split(String text) {
        return split(text, ",|，");
    }

    /**
     * 根据具体的分隔符 分隔字符串
     * @param text
     * @param separator
     * @return
     * @throws BizException if separator isBlank
     */
    public static List<String> split(String text, String separator) {

        if (StringUtils.isBlank(separator)) {
            throw BizException.instance("分隔符不能为空");
        }

        String[] parts = StringUtils.trimToEmpty(text).split(separator);
        List<String> resultList = new ArrayList<>(parts.length);
        for (String part : parts) {
            if (StringUtils.isNotBlank(part)) {
                resultList.add(StringUtils.trimToEmpty(part));
            }
        }
        return resultList;
    }

    /**
     * 拼接字符串, 使用英文逗号分隔
     * @param list
     * @return
     */
    public static String join(List<String> list) {
        return join(list, ",");
    }

    /**
     * 使用传入的分隔符拼接字符串
     * @param list
     * @param separator
     * @return
     * @throws BizException if separator isBlank
     */
    public static String join(List<String> list, String separator) {

        if (StringUtils.isBlank(separator)) {
            throw BizException.instance("分隔符不能为空");
        }

        if (list == null) {
            return "";
        } else {
            return StringUtils.join(list.iterator(), separator);
        }
    }
}
