package qrCode;

public class MainTest {

    public static void main(String[] args) {
        String imgPath = "/tmp/1.jpg";
        String encoderContent = "曾经\r\n 有人说我帅我不承认\r\n 然后他们就打我\r\n 现在\r\n 那些说我帅的人 \r\n都用手机扫到了这张图\r\n 他们会不会又想打我~\r\n →_→ ~YXY";
//		String encoderContent = "l love three things in this world.\r\n Sun,Moon and You.\r\n Sun for morning,\r\n Moon for night,\r\n and You forever.  \r\n浮世三千，吾爱有三。\r\n 日、月与卿。\r\n 日为朝，月为暮，卿为朝朝暮暮。";
        QRCode handler = new QRCode();
        handler.encoderQRCode(encoderContent, imgPath, "png");
        System.out.println("========encoder success");
        String decoderContent = handler.decoderQRCode(imgPath);
        System.out.println("解析结果如下：");
        System.out.println(decoderContent);
        System.out.println("========decoder success!!!");
    }
}
