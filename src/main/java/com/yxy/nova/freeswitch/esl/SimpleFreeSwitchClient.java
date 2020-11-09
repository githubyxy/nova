package com.yxy.nova.freeswitch.esl;

import com.yxy.nova.freeswitch.cmd.FSCmd;
import org.apache.commons.lang3.StringUtils;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.inbound.InboundConnectionFailure;
import org.freeswitch.esl.client.transport.message.EslMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class SimpleFreeSwitchClient {
    protected Logger LOGGER = LoggerFactory.getLogger(getClass());

    private boolean connectSuccess = false;

    private volatile boolean isRetrying = false;

    @Value("${freeswitch.host}")
    private String host;

    @Value("${freeswitch.port}")
    private int port;

    @Value("${freeswitch.password}")
    private String password;

    protected Client client;

    private boolean disabled = false;


    @PostConstruct
    public void init() {
        if (StringUtils.isAnyBlank(host, password)) {
            this.disabled = true;
            return;
        }
        client = new Client();
        LOGGER.info("Start connect to freeswitch, host: {}, port: {}", host, port);
        try {
            client.connect(host, port, password, 2);
            connectSuccess = true;
        } catch (InboundConnectionFailure e) {
            LOGGER.error("Client failed", e);
            return;
        }
        LOGGER.info("Client connected to {}:{} with password.", host, port);
    }

    public synchronized void watch() {
        if (!this.client.canSend()) {
            LOGGER.error("freeswitch连接断开，准备尝试恢复连接。");
            try {
                this.reConnect();
            } catch (Exception ignore) {
                LOGGER.error("尝试恢复freeswitch连接失败");
            }
        }
    }

    protected void reConnect() {
        LOGGER.info("开始恢复freeswitch连接,正在连接freeswitch");
        this.init();
        Boolean isConnected = this.client.canSend();
        LOGGER.info("freeswitch 恢复操作执行完毕, 当前连接状态: {}", isConnected ? "正常" : "断开");

        if (isConnected) {
            LOGGER.info("呼叫节点的freeswitch重启完成");

        }
    }

    public synchronized String execute(FSCmd cmd) {
        EslMessage eslMessage = client.sendSyncApiCommand(cmd.cmd(), cmd.args());
        LOGGER.info("Command run {} {}", cmd.cmd(), cmd.args());
        StringBuilder sb = new StringBuilder();
        List<String> lines = eslMessage.getBodyLines();
        int size = lines.size();
        for (int i = 0; i < size; i++) {
            sb.append(lines.get(i));
            if (i != size - 1) {
                sb.append('\n');
            }
        }
        return sb.toString();
    }

    public String executeAsync(FSCmd cmd) {
        // freeswitch job-uuid不可信，不能根据job uuid去找session
        try {
            String jobId = client.sendAsyncApiCommand(cmd.cmd(), cmd.args());
            LOGGER.info("Command run async {} {}, Job-UUID {}", cmd.cmd(), cmd.args(), jobId);
            return jobId;
        } catch (IllegalStateException e) {
            connectSuccess = false;
            // 连接超时
            // reConnect();
        }
        return null;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
