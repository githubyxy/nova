package com.yxy.nova.lwm2m.leshan.client;

import com.alibaba.fastjson.JSON;
import com.yxy.nova.MainLog;
import org.eclipse.leshan.client.LwM2mClient;
import org.eclipse.leshan.client.californium.LeshanClient;
import org.eclipse.leshan.client.californium.LeshanClientBuilder;
import org.eclipse.leshan.client.object.Device;
import org.eclipse.leshan.client.object.Security;
import org.eclipse.leshan.client.object.Server;
import org.eclipse.leshan.client.resource.ObjectsInitializer;
import org.eclipse.leshan.client.resource.listener.ResourceListener;
import org.eclipse.leshan.client.send.DataSender;
import org.eclipse.leshan.client.send.ManualDataSender;
import org.eclipse.leshan.client.send.SendService;
import org.eclipse.leshan.client.servers.ServerIdentity;
import org.eclipse.leshan.core.LwM2mId;
import org.eclipse.leshan.core.model.*;
import org.eclipse.leshan.core.node.LwM2mNode;
import org.eclipse.leshan.core.node.LwM2mPath;
import org.eclipse.leshan.core.request.BindingMode;
import org.eclipse.leshan.core.request.ContentFormat;
import org.eclipse.leshan.core.request.Identity;
import org.eclipse.leshan.core.request.ReadRequest;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.core.response.SendResponse;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

public class LeshanClientDemo {


    public static void main(String[] args) throws InvalidModelException, InvalidDDFFileException, IOException, InterruptedException {
        MainLog.initLog();

        // Load model
        List<ObjectModel> models = ObjectLoader.loadDefault();
        String[] modelPaths = new String[] {"3303.xml"};
        models.addAll(ObjectLoader.loadDdfResources("/models", modelPaths));

        // create objects
        ObjectsInitializer initializer = new ObjectsInitializer(new StaticModel(models));
        initializer.setInstancesForObject(LwM2mId.SECURITY, Security.noSec("coap://localhost:5683", 12345));
//        initializer.setInstancesForObject(LwM2mId.SECURITY, Security.noSec("coap://leshan.eclipseprojects.io:5683", 12345));
        Server server = new Server(12345, 5 * 60L);
        initializer.setInstancesForObject(LwM2mId.SERVER, server);
//        initializer.setInstancesForObject(LwM2mId.SERVER, new Server(12345, 5 * 60L, BindingMode.U, false));
        initializer.setInstancesForObject(LwM2mId.DEVICE, new Device("Eclipse Leshan yxy", "model12345", "12345", EnumSet.of(BindingMode.U)));
//        initializer.setInstancesForObject(LwM2mId.CONNECTIVITY_STATISTICS, new ConnectivityStatistics());

        RandomTemperatureSensor randomTemperatureSensor = new RandomTemperatureSensor();
        initializer.setInstancesForObject(3303, randomTemperatureSensor);



        String endpoint = "yxy_client" ; // choose an endpoint name
        LeshanClientBuilder builder = new LeshanClientBuilder(endpoint);
        // add it to the client
// DO NOT FORGET TO ASK TO CREATE THE NEW OBJECT "7"
//        builder.setObjects(initializer.create(3303));
        // add it to the client
        builder.setObjects(initializer.createAll());
        builder.setDataSenders(new ManualDataSender());
//        builder.setLocalAddress("127.0.0.1", 5683);
        LeshanClient client = builder.build();

        client.start();


//        Map<String, ServerIdentity> registeredServers = client.getRegisteredServers();
//        System.out.println(registeredServers.toString());
//        registeredServers.forEach((key, serverIdentity) -> {
//
//            randomTemperatureSensor.fireResourceChange(5700);
//        });

//        randomTemperatureSensor.adjustTemperature(10f);
//        randomTemperatureSensor.adjustTemperature(30f);

//        randomTemperatureSensor.addResourceListener(new ResourceListener() {
//            @Override
//            public void resourceChanged(LwM2mPath... lwM2mPaths) {
//                System.out.println("randomTemperatureSensor.addResourceListener = " + Arrays.toString(lwM2mPaths));
//            }
//        });

//        new Thread(() -> {
//            while (true) {
//                try {
//                    Thread.sleep(5000L);
//                    randomTemperatureSensor.fireResourceChange(5700);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

//        ServerIdentity serverIdentity = client.getRegisteredServers().values().iterator().next();
//        client.getSendService().sendData(serverIdentity, ContentFormat.JSON,
//                Arrays.asList("/3/0/1", "/3/0/2"), 1234L); // 关联自定义的发送服务


//        ServerIdentity serverIdentity = client.getRegisteredServers().values().iterator().next();
//        List<String> list = Arrays.asList("3303/0/5700");
//
//        SendService sendService = client.getSendService();
//
//        ManualDataSender manualSender = (ManualDataSender) sendService.getDataSender("MANUAL_SENDER");
//        List<LwM2mPath> paths = new ArrayList<>();
//        paths.add(new LwM2mPath("3303/0/5700"));
//        manualSender.collectData(paths);
//        manualSender.sendCollectedData(serverIdentity, ContentFormat.SENML_CBOR, 5000L,true);
//
//        SendResponse sendResponse = sendService.sendData(serverIdentity, ContentFormat.SENML_CBOR, list, 5000L);
//        System.out.println("sendResponse = " + JSON.toJSONString(sendResponse));
//        sendService.getDataSender()


    }



}
