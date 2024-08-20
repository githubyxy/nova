//package test.util;
//
//import com.itextpdf.io.font.PdfEncodings;
//import com.itextpdf.kernel.font.PdfFont;
//import com.itextpdf.kernel.font.PdfFontFactory;
//import com.itextpdf.kernel.pdf.PdfDocument;
//import com.itextpdf.kernel.pdf.PdfReader;
//import com.itextpdf.kernel.pdf.PdfWriter;
//import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
//import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
//import com.itextpdf.layout.element.Paragraph;
//import com.itextpdf.layout.Document;
//import com.itextpdf.layout.property.TextAlignment;
//import com.itextpdf.kernel.colors.ColorConstants;
//import com.itextpdf.kernel.geom.AffineTransform;
//import com.itextpdf.kernel.geom.Rectangle;
//import com.itextpdf.kernel.geom.Matrix;
//import java.io.IOException;
//
//import com.itextpdf.layout.property.VerticalAlignment;
//import org.junit.Test;
//
//public class ItextpdfTest {
//
//    @Test
//    public void a() {
//        String src = "/Users/yuxiaoyu/Downloads/1.pdf";
//        String dest = "/Users/yuxiaoyu/Downloads/2.pdf";
//
//        try {
//            // 读取PDF
//            PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
//            Document document = new Document(pdfDoc);
//            int numberOfPages = pdfDoc.getNumberOfPages();
//
//            String fontPath = "/Users/yuxiaoyu/code/githubyxy/nova/src/test/java/test/util/NotoSansCJKsc-VF.otf"; // 设置为支持中文的字体文件路径
//            PdfFont chineseFont = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H, true);
//
//            // 遍历每一页并添加水印
//            for (int i = 1; i <= numberOfPages; i++) {
//                PdfCanvas pdfCanvas = new PdfCanvas(pdfDoc.getPage(i).newContentStreamBefore(), pdfDoc.getPage(i).getResources(), pdfDoc);
//                Rectangle pageSize = pdfDoc.getPage(i).getPageSize();
//
//                // 设置水印内容
//                Paragraph paragraph = new Paragraph("仅供业委会申请使用")
//                        .setFont(chineseFont)
//                        .setFontSize(40)
//                        .setFontColor(ColorConstants.GRAY)
////                        .setOpacity(15f)
//                        .setRotationAngle(Math.toRadians(0))
//                        .setTextAlignment(TextAlignment.CENTER)
//                        ;
//
//                float x = pageSize.getWidth() / 2;
//                float y = pageSize.getHeight() / 2;
//
//                // 添加水印到页面
////                document.showTextAligned(paragraph, x, y, TextAlignment.JUSTIFIED);
//
//                document.showTextAligned(paragraph,
//                        pdfDoc.getPage(i).getPageSize().getWidth() / 2,
//                        pdfDoc.getPage(i).getPageSize().getHeight() / 2,
//                        i,
//                        TextAlignment.CENTER,
//                        VerticalAlignment.MIDDLE,
//                        -90   // 水印选择弧度
////                        (float) -Math.toDegrees(Math.atan(pdfDoc.getPage(i).getPageSize().getHeight() / pdfDoc.getPage(i).getPageSize().getWidth()))
//                        );
//
//
//            }
//
//            // 关闭文档
//            document.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
