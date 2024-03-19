package test;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsTest {

    @Test
    public void test() {
        String smsContent = "【平安保险】尾号3830用户，您的保障已送达！请及时领取，戳 c5h.cn/2ydbe 确认，谨防失效，拒收请回复R";
        String signature = extractSignature(smsContent);
        System.out.println("签名是：" + signature);
    }

    public String extractSignature(String smsContent) {
        // 匹配中文、英文、数字、空格以及一些特殊字符，签名位于【】之间
        Pattern pattern = Pattern.compile("【(.*?)】");
        Matcher matcher = pattern.matcher(smsContent);
        if (matcher.find()) {
            return matcher.group(1); // 返回匹配到的签名内容
        }
        return null; // 如果未匹配到，则返回null
    }
}
