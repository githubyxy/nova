package com.yxy.nova.nio;

import lombok.Data;

import java.io.Serializable;

@Data
public class UDPMessage implements Serializable {
    private String key;
    private String content;
}
