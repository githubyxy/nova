package com.yxy.nova.lwm2m.leshan.bootstrap;

import org.eclipse.leshan.core.SecurityMode;
import org.eclipse.leshan.server.bootstrap.*;
import org.eclipse.leshan.server.californium.bootstrap.LeshanBootstrapServer;
import org.eclipse.leshan.server.californium.bootstrap.LeshanBootstrapServerBuilder;

import java.util.Arrays;

public class LeshanBootstrapDemo {


    public static void main(String[] args) throws InvalidConfigurationException {
        // Create Bootstrap Server
        LeshanBootstrapServerBuilder builder = new LeshanBootstrapServerBuilder();
        builder.setLocalAddress(null, 5685);
        JSONFileBootstrapStore configStore = new JSONFileBootstrapStore();
        builder.setConfigStore(configStore);

        LeshanBootstrapServer bootstrapServer = builder.build();

// Create a Bootstrap config.
        BootstrapConfig config = new BootstrapConfig();

// delete object /0 and /1
        config.toDelete = Arrays.asList("/0","/1");

// write a security instance for LWM2M server.
// here we will use the leshan sandbox at : https://leshan.eclipseprojects.io/
        BootstrapConfig.ServerSecurity dmSecurity = new BootstrapConfig.ServerSecurity();
        dmSecurity.uri = "coap://leshan.eclipseprojects.io";
//        dmSecurity.uri = "coap://localhost:5683";
        dmSecurity.serverId = 12345;
        dmSecurity.securityMode = SecurityMode.NO_SEC;
        config.security.put(1, dmSecurity); // O is reserved for bootstrap server

// write a server object for LWM2M server
        BootstrapConfig.ServerConfig dmConfig = new BootstrapConfig.ServerConfig();
        dmConfig.shortId = dmSecurity.serverId;
        dmConfig.lifetime = 5*60;
        config.servers.put(0, dmConfig);

// Add the config to the store for your device.
//        JSONFileBootstrapStore configStore = new JSONFileBootstrapStore();
        configStore.add("yxy_client", config);

// Start server
        bootstrapServer.start();
    }
}
