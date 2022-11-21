package com.yxy.nova.mwh.utils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by liulongli on 2017/12/5.
 */
public class CompressUtil {

    private static final Logger logger = LoggerFactory.getLogger(CompressUtil.class);


    // 压缩
    public static String compressWithGzip(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes("utf-8"));
            gzip.close();
            return new sun.misc.BASE64Encoder().encode(out.toByteArray());
        } catch (IOException e) {
            logger.error("压缩失败", e);

            //如果压缩失败，则直接存入原始数据
            return str;
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    // 解压缩
    public static String deCompressWithGzip(String str){
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPInputStream gunzip = null;
        ByteArrayInputStream in = null;
        try {
            byte [] bytes = new sun.misc.BASE64Decoder().decodeBuffer(str);
            in = new ByteArrayInputStream(bytes);
            gunzip = new GZIPInputStream(in);

            byte[] buffer = new byte[256];
            int n = 0;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            logger.error("解压缩失败", e);
        } finally {
            IOUtils.closeQuietly(out);
            if (in != null) {
                IOUtils.closeQuietly(in);
            }
            if (gunzip != null) {
                IOUtils.closeQuietly(gunzip);
            }
        }

        try {
            return out.toString("utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("解压缩失败", e);
        }

        return null;
    }
}
