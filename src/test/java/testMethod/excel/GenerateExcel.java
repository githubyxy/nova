package testMethod.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

public class GenerateExcel {

    private String[] titles = new String[]{"MD5_P", "MD5_I", "XM"};
    private int contentSize = 2;
    private String[] contents = new String[]{"9C2E72A74E6133C2C77121B0472", "582BA4C6D9FEEC5653F369A66B7", "姓名有"};
    private String fileName = "/tmp/3.xlsx";

    @Test
    public void test() throws FileNotFoundException, IOException {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        XSSFSheet sheet = xssfWorkbook.createSheet();

        XSSFCellStyle createCellStyle = xssfWorkbook.createCellStyle();
        createCellStyle.setAlignment(createCellStyle.ALIGN_CENTER);

        XSSFRow row0 = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            XSSFCell cell = row0.createCell(i);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(createCellStyle);
        }
        // 具体内容
        for (int i = 1; i <= contentSize; i++) {
            XSSFRow row = sheet.createRow(i);

            for (int j = 0; j < contents.length; j++) {
                XSSFCell cell = row.createCell(j);
                cell.setCellValue(i + contents[j]);
                cell.setCellStyle(createCellStyle);
            }
        }

//        HSSFFont font = wb.createFont();
//        font.setColor(HSSFColor.RED.index);
//        HSSFRichTextString ts = new HSSFRichTextString(displayName);
//        // 从4开始
//        ts.applyFont(displayName.length()-changeComment.length(),displayName.length(),font);
//        cell.setCellValue(ts);

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

        xssfWorkbook.write(new FileOutputStream(new File(fileName)));
    }

}
