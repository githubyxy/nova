package com.yxy.nova.mwh.elasticsearch.facade.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by caipeichao on 17/3/29.
 */
public class ESClassLoader extends ClassLoader {
    private static final Charset UTF8 = Charset.forName("UTF8");
    private static final Logger logger = LoggerFactory.getLogger(ESClassLoader.class);
    public static final ESClassLoader INSTANCE = new ESClassLoader();

    //private final List<String> libFileList;

    public ESClassLoader() {
        //this.libFileList = loadFileList();
    }

    private List<String> loadFileList() {
        String manifest = readManifest();
        String[] result = manifest.split("\\n");
        return Collections.unmodifiableList(Arrays.asList(result));
    }

    private String readManifest() {
        byte[] result = loadResource("/JAR-INF/lib/es-lib-manifest");
        if (result == null) {
            throw new RuntimeException("Cannot load es-lib-manifest");
        }
        return new String(result, UTF8);
    }

    private byte[] loadResource(String filename) {
        logger.info("Load resource: " + filename);
        try {
            InputStream input = this.getClass().getResourceAsStream(filename);
            if (input == null) return null;
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

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classData = loadClassBytes(name);
        if (classData != null) {
            return defineClass(name, classData, 0, classData.length);
        }
        return this.getClass().getClassLoader().loadClass(name);
    }

    @Override
    protected URL findResource(String name) {
        URL result = this.getClass().getClassLoader().getResource("JAR-INF/lib/" + name);
        logger.info("Find resource {} get {}", name, result);
        return result;
    }

    private byte[] loadClassBytes(String className) {
        String path = toPath(className);
        /*if (!libFileList.contains(path)) {
            return null;
        }*/
        return loadResource("/JAR-INF/lib/" + path);
    }

    private String toPath(String className) {
        return className.replace(".", "/") + ".class";
    }
}
