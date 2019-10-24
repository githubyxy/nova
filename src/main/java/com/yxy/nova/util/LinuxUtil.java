package com.yxy.nova.util;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author yuxiaoyu
 * @Description:
 * @date 2019-10-24 10:22
 */
public class LinuxUtil {

    public static String fortune() throws Exception {
        String command = "fortune";

        Process proc = new ProcessBuilder(command)
                .redirectErrorStream(true)
                .start();

        ArrayList<String> output = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line;
        while ((line = br.readLine()) != null){
            output.add(line);
        }

        return StringUtils.join(output, System.lineSeparator());
    }

}
