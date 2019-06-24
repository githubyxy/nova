package testMethod.wordUtilTest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.apache.poi.hwpf.HWPFDocument;

import org.apache.poi.hwpf.usermodel.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WordUtil {

    private static Logger logger = LoggerFactory.getLogger(WordUtil.class);

    /**
     * 灏囨暟鎹～鍏呭埌doc鏂囦欢
     *
     * @param dataMap      word涓渶瑕佸睍绀虹殑鍔ㄦ�佹暟鎹紝鐢╩ap闆嗗悎鏉ヤ繚瀛�
     * @param templatePath word妯℃澘缁濆璺緞锛屼緥濡傦細/home/tangyh/cana/front/vbam-front-biz/src/main/resources/template/contractTemplate.doc
     * @author hu 淇敼璁╂浛鎹㈠唴瀹瑰寘鎷〉鐪夐〉鑴�
     */
    public static byte[] getFilledDocTemplateAsBytes(String templatePath, Map<String, String> paramMap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            HWPFDocument doc = new HWPFDocument(new FileInputStream(new File(templatePath)));
            Range range = doc.getOverallRange();
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                range.replaceText(entry.getKey(), entry.getValue());
            }
            doc.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("灏囨暟鎹～鍏呭埌doc鏂囦欢澶辫触!{}", e);
        }
        return null;

    }
}
