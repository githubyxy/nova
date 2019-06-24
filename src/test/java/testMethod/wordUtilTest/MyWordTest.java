package testMethod.wordUtilTest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class MyWordTest {

    public static void main(String[] args) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {

            HWPFDocument doc = new HWPFDocument(new FileInputStream(new File("D:\\java\\1.doc")));
            Map<String, String> paramMap = new HashMap<>();
            Range range = doc.getOverallRange();
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                range.replaceText(entry.getKey(), entry.getValue());
            }
            doc.write(outputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }

//		readAndWriterTest4();
		
		 /*File file = new File("D:\\java\\1.doc");
	        String str = "";
	        try {
	            FileInputStream fis = new FileInputStream(file);
	            HWPFDocument doc = new HWPFDocument(fis);
	            String doc1 = doc.getDocumentText();
	            System.out.println(doc1);
	            StringBuilder doc2 = doc.getText();
	            System.out.println(doc2);
	            Range rang = doc.getRange();
	            String doc3 = rang.text();
	            System.out.println(doc3);
	            fis.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }*/
    }

    public static void readAndWriterTest3() throws IOException {
        File file = new File("D:\\java\\1.doc");
        String str = "";
        try {
            FileInputStream fis = new FileInputStream(file);
            HWPFDocument doc = new HWPFDocument(fis);
            String doc1 = doc.getDocumentText();
            System.out.println(doc1);
            StringBuilder doc2 = doc.getText();
            System.out.println(doc2);
            Range rang = doc.getRange();
            String doc3 = rang.text();
            System.out.println(doc3);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void readAndWriterTest4() throws IOException {
        File file = new File("D:\\java\\3.docx");
        String str = "";
        try {
            FileInputStream fis = new FileInputStream(file);
            XWPFDocument xdoc = new XWPFDocument(fis);
            XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
            String doc1 = extractor.getText();
            System.out.println(doc1);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
