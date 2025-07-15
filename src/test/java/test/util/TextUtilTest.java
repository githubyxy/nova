package test.util;

import com.yxy.nova.mwh.utils.text.TextUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TextUtilTest {

    @Test
    public void test() {
        Map<String, String> valueMap = new HashMap<>();
        valueMap.put("syin_customerBatchUuid", null);
        valueMap.put("syin_customerBatchName", null);

        String replace = TextUtil.replace("${syin_customerBatchName}_${syin_customerBatchUuid}", valueMap);
        System.out.println(replace);
    }
}
