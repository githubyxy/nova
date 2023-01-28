package com.yxy.nova.lwm2m.leshan.client;

import com.yxy.nova.MainLog;
import org.eclipse.leshan.client.californium.LeshanClient;
import org.eclipse.leshan.client.californium.LeshanClientBuilder;
import org.eclipse.leshan.client.object.Device;
import org.eclipse.leshan.client.object.Security;
import org.eclipse.leshan.client.object.Server;
import org.eclipse.leshan.client.resource.ObjectsInitializer;
import org.eclipse.leshan.core.LwM2mId;
import org.eclipse.leshan.core.model.*;
import org.eclipse.leshan.core.request.BindingMode;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;

public class LeshanClientDemo {


    public static void main(String[] args) throws InvalidModelException, InvalidDDFFileException, IOException {
        MainLog.initLog();

        // Load model
        List<ObjectModel> models = ObjectLoader.loadDefault();
        String[] modelPaths = new String[] {"10242.xml"};
        models.addAll(ObjectLoader.loadDdfResources("/models", modelPaths));


        // create objects
        ObjectsInitializer initializer = new ObjectsInitializer(new StaticModel(models));
        initializer.setInstancesForObject(LwM2mId.SECURITY, Security.noSec("coap://127.0.0.1:5683", 12345));
        initializer.setInstancesForObject(LwM2mId.SERVER, new Server(12345, 5 * 60L));
//        initializer.setInstancesForObject(LwM2mId.SERVER, new Server(12345, 5 * 60L, BindingMode.U, false));
        initializer.setInstancesForObject(LwM2mId.DEVICE, new Device("Eclipse Leshan", "model12345", "12345", EnumSet.of(BindingMode.U)));
        initializer.setInstancesForObject(7, new ConnectivityStatistics());



        String endpoint = "yxy_client" ; // choose an endpoint name
        LeshanClientBuilder builder = new LeshanClientBuilder(endpoint);
        // add it to the client
// DO NOT FORGET TO ASK TO CREATE THE NEW OBJECT "7"
        builder.setObjects(initializer.create(7, LwM2mId.SECURITY,LwM2mId.SERVER, LwM2mId.DEVICE));
        // add it to the client
//        builder.setObjects(initializer.createAll());
        LeshanClient client = builder.build();
        client.start();
    }



}
