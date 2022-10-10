package com.yxy.nova.udp;

import lombok.Data;

import java.io.Serializable;

@Data
public class UDPMessage implements Serializable {
    private String key;
    private String content;
}
