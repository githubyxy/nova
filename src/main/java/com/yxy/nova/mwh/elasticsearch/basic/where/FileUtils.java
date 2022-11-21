package com.yxy.nova.mwh.elasticsearch.basic.where;

import java.io.*;

/**
 * Created by caipeichao on 2015/1/14.
 */
public class FileUtils {

    /**
     * @param clazz 调用方
     */
    public static String loadTextFromClassPath(Class clazz, String filename) {
        try {
            byte[] bytes = loadBytesFromClassPath(clazz, filename);
            return new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Failed load text", e);
        }
    }

    /**
     * @param clazz 调用方
     * @param filename 文件名，斜杠开头表示resource目录下的，没有斜杠开头表示相对与class的文件， 例如：resource/cn/fraudmetrix/.../MyClass/filename
     */
    public static byte[] loadBytesFromClassPath(Class clazz, String filename) {
        filename = expandIfNoLeadingSlash(clazz, filename);
        try {
            InputStream input = clazz.getResourceAsStream(filename);
            if (input == null) {
                throw new FileNotFoundException("Cannot find classpath file: " + filename);
            }
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            while (true) {
                byte[] buffer = new byte[4096];
                int readSize = input.read(buffer);
                if (readSize < 0) {
                    return result.toByteArray();
                }
                result.write(buffer, 0, readSize);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static String expandIfNoLeadingSlash(Class clazz, String filename) {
        if (filename.startsWith("/")) {
            return filename;
        }

        String prefix = clazz.getCanonicalName();
        prefix = prefix.replace('.', File.separatorChar);
        return prefix + File.separatorChar + filename;
    }
}
