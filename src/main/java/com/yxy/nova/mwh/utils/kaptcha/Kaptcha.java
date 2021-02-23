//package com.yxy.nova.mwh.utils.kaptcha;
//
//import com.google.code.kaptcha.impl.DefaultKaptcha;
//import com.google.code.kaptcha.util.Config;
//
//import java.awt.image.BufferedImage;
//import java.util.Properties;
//
///**
// * @author yuxiaoyu
// * @date 2021/2/5 下午2:18
// * @Description
// */
//public class Kaptcha {
//
//    private static DefaultKaptcha generator;
//
//    static {
//        Properties ppt = new Properties();
//        ppt.put("kaptcha.border", "no");
//        ppt.put("kaptcha.textproducer.font.color", "88,105,147");
//        ppt.put("kaptcha.textproducer.char.length", "4");
//        ppt.put("kaptcha.background.clear.from", "white");
//        ppt.put("kaptcha.background.clear.to", "white");
//        Config config = new Config(ppt);
//        generator = new DefaultKaptcha();
//        generator.setConfig(config);
//    }
//
//    public String getCaptcha() {
//        return generator.createText();
//    }
//
//    public BufferedImage createImage(String text) {
//        return generator.createImage(text);
//    }
//}
