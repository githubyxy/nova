package testMethod.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hslf.model.Sheet;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

public class GenerateExcel2 {

    private String[] titles = new String[]{"客户姓名", "客户手机号"};
    private int contentSize = 200000;
    private String[] contents = new String[]{"李艺", "11111"};
    private String fileName = "/tmp/b.xlsx";

    @Test
    public void test() throws FileNotFoundException, IOException {
        System.err.println("time=" + System.currentTimeMillis());
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();


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
//		XSSFSheet sheet1 = xssfWorkbook.createSheet();
//		
//		XSSFRow row0_1 = sheet1.createRow(0);
//		for(int i = 0; i < titles.length; i ++) {
//			XSSFCell cell = row0_1.createCell(i);
//			cell.setCellValue(titles[i]);
//			cell.setCellStyle(createCellStyle);
//		}
//		// 具体内容
//		for(int i = 1; i <= contentSize; i++) {
//			XSSFRow row = sheet1.createRow(i);
//			
//			for(int j = 0; j < contents.length; j ++) {
//				XSSFCell cell = row.createCell(j);
//				cell.setCellValue(i + "A" + contents[j]);
//				cell.setCellStyle(createCellStyle);
//			}
//		}

        workbook.write(new FileOutputStream(new File(fileName)));
        System.err.println("time=" + System.currentTimeMillis());
        System.out.println(sheet.getLastRowNum());
    }

}
