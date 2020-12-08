package com.yxy.nova.util;

import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;

/**
 * @author yuxiaoyu
 * @date 2020/12/1 下午3:26
 * @Description
 */
public class PDFUtil {

    private static final Logger logger = LoggerFactory.getLogger(PDFUtil.class);

    public static byte[] toPDF(byte[] b, String sourceFileName) throws Exception {

        File tempDir = null;
        try {
            tempDir = Files.createTempDir();

            String canonicalPath = tempDir.getCanonicalPath();

            File file = new File(canonicalPath + "/" + sourceFileName);

            OutputStream os = new FileOutputStream(file);

            BufferedOutputStream bufferedOutput = new BufferedOutputStream(os);

            bufferedOutput.write(b);

            String command = "libreoffice";

            Process proc = new ProcessBuilder(command, "--headless", "--convert-to", "pdf:writer_pdf_Export", "--outdir", canonicalPath, canonicalPath + "/" + sourceFileName)
                    .redirectErrorStream(true)
                    .start();

            ArrayList<String> output = new ArrayList<String>();
            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                output.add(line);
            }

            logger.info("执行pdf转换命令的输出：" + StringUtils.join(output, System.lineSeparator()));

            if (0 != proc.waitFor()) {
                throw new Exception("转换失败");
            }

            File[] files = tempDir.listFiles();
            for (File file2 : files) {
                if (file2.getPath().endsWith(".pdf")) {
                    return IOUtils.toByteArray(new FileInputStream(file2));
                }
            }
            return null;
        } finally {
            if (tempDir != null) {
                FileUtils.deleteDirectory(tempDir);
            }
        }
    }
}
