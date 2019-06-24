package testMethod.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.Test;

public class GenerateExcel3 {

    private String[] titles = new String[]{"客户姓名", "客户手机号"};
    private int contentSize = 200000;
    private String[] contents = new String[]{"李艺", "11111"};
    private String fileName = "/tmp/b.xlsx";

    @Test
    public void test() throws FileNotFoundException, IOException {
        System.err.println("time=" + System.currentTimeMillis());
        // 大数据量速度更快
        SXSSFWorkbook workbook = new SXSSFWorkbook(100);
        Sheet sheet = workbook.createSheet();


        Row row0 = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = row0.createCell(i);
            cell.setCellValue(titles[i]);
        }
        // 具体内容
        for (int i = 1; i <= contentSize; i++) {
            Row row = sheet.createRow(i);

            String perfix = i + "";
            while (perfix.length() < 6) {
                perfix = "0" + perfix;
            }

            for (int j = 0; j < contents.length; j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(contents[j] + (j == 1 ? perfix : ""));
            }
        }
        System.err.println("time=" + System.currentTimeMillis());

        workbook.write(new FileOutputStream(new File(fileName)));
        System.err.println("time=" + System.currentTimeMillis());
        System.out.println(sheet.getLastRowNum());
    }

}
