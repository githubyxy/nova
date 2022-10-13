package test;

import com.yxy.nova.mwh.utils.text.TextUtil;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPath;
import org.junit.Test;
import org.xml.sax.InputSource;

import java.io.*;
import java.util.List;

public class Sqlmap2TableTest {
    // 默认所有的varchar都是512，可以保证满足绝大多数的字段
    private static final String DEFAULT_VARCHAR_LENGTH = "VARCHAR(256)";


    @Test
    public void test() throws IOException, JDOMException {
        String sqlMapPath = "/Users/yuxiaoyu/code/githubyxy/nova/src/main/resources/mybatis/mapper";//这里指定你的sqlmap配置文件所在路径
        analysis(sqlMapPath);
    }

    /* 根据指定的目录进行遍历分析
     *
     * @param path
     * @throws IOException
     * @throws JDOMException
     */
    private static void analysis(String path) throws IOException, JDOMException {
        File filePath = new File(path);
        if (filePath.isDirectory() && !filePath.getName().equals(".svn")) {
            File[] fileList = filePath.listFiles();
            for (File file : fileList) {
                if (file.isDirectory()) {
                    analysis(file.getAbsolutePath());
                } else {
                    analysisSqlMap(file.getAbsolutePath());
                }
            }
        }
    }

    /**
     * 分析单个的sqlmap配置文件
     *  当然xml文件中必须设置字段类型的属性，否则无法判断字段属性
     * @param sqlMapFile
     * @throws IOException
     * @throws JDOMException
     */
    @SuppressWarnings("unchecked")
    private static void analysisSqlMap(String sqlMapFile) throws IOException, JDOMException {
        // System.out.println("************"+sqlMapFile);
        boolean isNull=false;
        /**
         * 这里要把sqlmap文件中的这一行去掉：<br>
         * <!DOCTYPE sqlMap PUBLIC "-//iBATIS.com//DTD SQL Map 2.0//EN" "http://www.ibatis.com/dtd/sql-map-2.dtd"><br>
         * 否则JDom根据文件创建Document对象时，会报找不到www.ibatis.com这个异常，导致渲染不成功。
         */
        String xmlString = filterRead(sqlMapFile, "<!DOCTYPE");
//       XmlDocument doc = new XmlDocument();

        Document doc = getDocument(xmlString);
        Element rootElement = doc.getRootElement();
        List<Element> resultMap = rootElement.getChildren("resultMap");
        for (Element e : resultMap) {
            String alias = e.getAttributeValue("type");
            String tableName = getTableName(doc, alias);
            List<Element> children = e.getChildren();
            StringBuilder createTableString = new StringBuilder("create table " + tableName + "(\n\t");
            int size = 0;
            for (Element child : children) {
                String jdbcType = child.getAttributeValue("jdbcType");  //获取属性类型
                if(StringUtils.isEmpty(jdbcType)){
                    isNull=true;
                    break;
                }
                if (jdbcType.toUpperCase().equals("VARCHAR")) {
                    jdbcType = DEFAULT_VARCHAR_LENGTH;
                }
                else if (jdbcType.toUpperCase().equals("CHAR")) {
                    jdbcType = "char(10)";
                }
                else if (jdbcType.toUpperCase().equals("BIGINT")) {
                    jdbcType = "bigint(20)";
                }
                if (jdbcType.toUpperCase().equals("INTEGER")) {
                    jdbcType = "int(11)";
                }
                else if (jdbcType.toUpperCase().equals("DECIMAL")) {
                    jdbcType = "decimal(10,2)";
                }
                else if (jdbcType.toUpperCase().equals("NUMERIC")) {
                    jdbcType = "decimal(10,2)";
                }
                if (jdbcType.toUpperCase().equals("DOUBLE")) {
                    jdbcType = "double";
                }
                if (jdbcType.toUpperCase().equals("REAL")) {
                    jdbcType = "double";
                }

                if (jdbcType.toUpperCase().equals("BOOLEAN")) {
                    jdbcType = "tinyint(1)";
                }
                if (jdbcType.toUpperCase().equals("FLOAT")) {
                    jdbcType = "float";
                }
                if (jdbcType.toUpperCase().equals("LONGVARCHAR")) {
                    jdbcType = "text";
                }
                String column = child.getAttributeValue("column");
                createTableString.append(column).append(" ").append(jdbcType);  //加入属性和类型
                if (column.equals("id")) {
                    createTableString.append(" unsigned NOT NULL AUTO_INCREMENT COMMENT '主键(自增ID)'");
                }
                if (size < children.size() - 1) {
                    createTableString.append(",\n\t");
                } else {
                    createTableString.append(",\n\t");
                }
                size++;
            }
            if(isNull){
                break;
            }
            // 索引
//            createTableString.append(",\n\t");
            createTableString.append("PRIMARY KEY (`id`)\n\t");

            createTableString.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");
            System.out.println(createTableString.toString().toLowerCase());
        }
    }


    private static String getTableName(Document doc, String alias) throws JDOMException {
        String tableName = "";
        String classPath = null;
        // 这里的alias可能是一个别名，也可能是一个java类路径，这里我通过该alias是否有点"."这个符号来区别
        if (alias.indexOf(".") > 0) {// 是JAVA类
            classPath = alias;
        } else {// 是别名，就到配置的别名中去找
            Element aliasElement = (Element) XPath.selectSingleNode(doc, "//typeAlias[@alias=\"" + alias + "\"]");
//           classPath = aliasElement.getAttributeValue("type");
        }
//       String[] classPathArray = classPath.split("\\.");
        // 取到DO的名称
//       classPath = classPathArray[classPathArray.length - 1];

        classPath = alias;

         int i = classPath.lastIndexOf("DO");
        // 取到根据表名生成的DO名称，无“DO”两个字符
        classPath = classPath.substring(0, i);
        List<String> split = TextUtil.split(classPath, "\\.");
        // classPath = 表名
        classPath = split.get(split.size() - 1);

        char[] chars = classPath.toCharArray();
        boolean isFirst = Boolean.TRUE;
        // 生成真实的表名
        for (char c : chars) {
            if (!isFirst && c >= 65 && c <= 90) {
                tableName += "_";
            }
            if (isFirst) {
                isFirst = Boolean.FALSE;
            }
            tableName += c;
        }
        // 表名转换为大写返回
        return tableName;

    }


    /**
     * 根据XML字符串 建立JDom的Document对象
     *
     * @param xmlString XML格式的字符串
     * @return Document 返回建立的JDom的Document对象，建立不成功将抛出异常。
     * @throws IOException
     * @throws JDOMException
     */
    private static Document getDocument(String xmlString) throws JDOMException, IOException {
//       SAXBuilder builder = new SAXBuilder();
//       Document anotherDocument =  builder.build( new StringReader(xmlString));

//       try {
//        Document doc = (Document)DocumentHelper.parseText(xmlString);
//         return doc;
//
//       } catch (DocumentException e) {
//        e.printStackTrace();
//    }

        StringReader st = new StringReader(xmlString);
        InputSource is = new InputSource(st);
        Document doc = (new SAXBuilder()).build(is);
        return doc;
    }

    /**
     * 过滤性阅读
     *
     * @param filePath 文件路径
     * @param notIncludeLineStartWith 不包括的字符，即某行的开头是这样的字符串，则在读取的时候该行忽略
     * @return
     * @throws IOException
     */
    private static String filterRead(String filePath, String notIncludeLineStartWith) throws IOException {
        String result = "";
        FileReader fr = new FileReader(filePath);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        while (line != null) {
            if (!line.startsWith(notIncludeLineStartWith)) {
                result += line;
            }
            line = br.readLine();
            if (line != null && !line.startsWith(notIncludeLineStartWith)) {
                result += "\n";
            }
        }
        br.close();
        fr.close();
        return result;
    }
}
