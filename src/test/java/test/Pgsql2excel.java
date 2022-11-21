package test;

import com.alibaba.fastjson.JSON;
import com.yxy.nova.mwh.utils.text.TextUtil;
import com.yxy.nova.mwh.utils.time.DateTimeUtil;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.junit.Test;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Pgsql2excel {

    @Test
    public void test() {
        String sql = "password character varying(255) NOT NULL,\n" +
                "    salt character varying(255) NOT NULL,\n" +
                "    create_time bigint,\n" +
                "    creator_id character varying(255),\n" +
                "    name character varying(128) NOT NULL,\n" +
                "    id character varying(64) PRIMARY KEY,\n" +
                "    type character varying(255),\n" +
                "    status smallint,\n" +
                "    username character varying(128) NOT NULL";
        String comment = "COMMENT ON TABLE s_user IS '用户信息';\n" +
                "COMMENT ON COLUMN s_user.password IS '密码';\n" +
                "COMMENT ON COLUMN s_user.salt IS '加密盐值';\n" +
                "COMMENT ON COLUMN s_user.create_time IS '创建时间';\n" +
                "COMMENT ON COLUMN s_user.creator_id IS '创建者ID';\n" +
                "COMMENT ON COLUMN s_user.name IS '姓名';\n" +
                "COMMENT ON COLUMN s_user.id IS 'id';\n" +
                "COMMENT ON COLUMN s_user.type IS '用户类型';\n" +
                "COMMENT ON COLUMN s_user.status IS '用户状态';\n" +
                "COMMENT ON COLUMN s_user.username IS '用户名';";
        List<String> sqlList = TextUtil.split(sql);
        List<String> commentList = TextUtil.split(comment, ";");
        boolean hasTableName = commentList.size() > sqlList.size();
//        System.out.println(JSON.toJSON(sqlList));
//        System.out.println(JSON.toJSON(commentList));


        excel(sqlList, commentList, hasTableName);
    }

    private void excel(List<String> sqlList, List<String> commentList, boolean hasTableName) {
        List<String> title = new ArrayList<>();
        title.add("字段名");
        title.add("类型");
        title.add("长度");
        title.add("主键");
        title.add("索引");
        title.add("不可空");
        title.add("默认值");
        title.add("说明");
            try {
                XSSFWorkbook workbook = new XSSFWorkbook();

                FileOutputStream fileOut = new FileOutputStream("/Users/yuxiaoyu/Downloads/" + DateTimeUtil.datetime18() + ".xlsx");

                List<List<String>> allExcelContent = new ArrayList<>();
                for (int i=0; i < sqlList.size(); i++) {
                    List<String> excelContent = new ArrayList<>();
                    String s = sqlList.get(i);
                    boolean idIndex = s.indexOf("PRIMARY KEY") > 0;
                    boolean notNull = s.indexOf("NOT NULL") > 0;
                    String[] split = s.replace("NOT NULL", "").split(" ");
                    // split 要有七个参数
                    for (int j=0; j < 7; j++) {
                        if (idIndex) {
                            if (j ==3 || j == 4) {
                                excelContent.add("√");
                                continue;
                            }
                        }
                        if (notNull) {
                            if (j ==5) {
                                excelContent.add("√");
                                continue;
                            }
                        }
                        if (split.length > j) {
                            excelContent.add(split[j]);
                        } else {
                            excelContent.add("");
                        }
                    }
                    if (!CollectionUtils.isEmpty(commentList)) {
                        String comment = commentList.get(hasTableName ? i+1 : i);
                        String[] split1 = comment.split("'");
                        excelContent.add(split1[1]);
                    }
                    //
                    allExcelContent.add(excelContent);
                }


                //
                createStateSheet(workbook, title, allExcelContent);

//                prepareServlet(request, response, fileName);

                workbook.write(fileOut);
                fileOut.close();
            } catch (Exception e) {
            }
        }

    private void createStateSheet(XSSFWorkbook workbook, List<String> title, List<List<String>> allExcelContent) {
        XSSFSheet sheet = workbook.createSheet("物联网中台");
        XSSFCellStyle textCellStyle = workbook.createCellStyle();
        XSSFDataFormat format = workbook.createDataFormat();
        textCellStyle.setDataFormat(format.getFormat("@"));

        XSSFRow row0 = sheet.createRow(0);
        for (int i = 0; i < title.size(); i++) {
            sheet.setDefaultColumnStyle(i, textCellStyle);
            sheet.setColumnWidth(i, 2560);
            Cell cell = row0.createCell(i);
            cell.setCellValue(title.get(i));
        }

        for (int i = 1; i <= allExcelContent.size(); i++) {
            List<String> excelContent = allExcelContent.get(i-1);
            XSSFRow row1 = sheet.createRow(i);
            for (int j=0; j < excelContent.size(); j++) {
                Cell cell1 = row1.createCell(j);
                cell1.setCellValue(excelContent.get(j));
            }
        }
    }
}
