package test;

import cn.hutool.core.thread.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * @author yxy
 * @description: 短信提交延迟的通道，单独处理
 * @date 2023/10/19 4:14 PM
 */
public class SmsTaskItemSendProcessor {
    private static Logger logger = LoggerFactory.getLogger(SmsTaskItemSendProcessor.class);

    private static final long INTERVAL = 1000;

    private final String DELIMITER = "::";

    private static String name;

    public SmsTaskItemSendProcessor(String name) {
        this.name = name;
    }

    public void start() {
        Thread thread = ThreadUtil.newThread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                long start = System.currentTimeMillis();
                try {
                    execute();
                } catch (Exception e) {
                    logger.error("sms短信发送异常", e);
                }
                long end = System.currentTimeMillis();
                long cost = end - start;
                if (cost < INTERVAL) {
                    // 休眠
                    ThreadUtil.sleep(INTERVAL - cost);
                }
            }
        }, "sms_batchSend", true);

        // 启动线程
        thread.start();
    }



    private static void execute() {
        System.out.println("execute:" + name);
    }

}
