package com.yxy.nova.lwm2m.leshan.client;

import com.alibaba.fastjson.JSON;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.client.servers.ServerIdentity;
import org.eclipse.leshan.core.node.LwM2mObjectInstance;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.request.argument.Arguments;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.core.response.WriteResponse;

import java.util.Timer;
import java.util.TimerTask;

public class ConnectivityStatistics extends BaseInstanceEnabler {

    private final Timer timer;

    public ConnectivityStatistics() {
        // notify new date each 5 second
        this.timer = new Timer("Device-Current Time");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("fireResourceChange 4");
                fireResourceChange(4);
            }
        }, 5000, 5000);
    }


    @Override
    public ReadResponse read(ServerIdentity identity, int resourceid) {
        System.out.println("client read:" + JSON.toJSONString(resourceid));
        return ReadResponse.success(resourceid, resourceid);
//        switch (resourceid) {
//            case 0:
//                return ReadResponse.success(resourceid, resourceid);
//        }
//        return ReadResponse.notFound();
    }

    @Override
    public WriteResponse write(ServerIdentity identity, boolean replace, LwM2mObjectInstance value) {
        System.out.println("client write:" + JSON.toJSONString(value.getResources()));
        return WriteResponse.success();
    }

    @Override
    public WriteResponse write(ServerIdentity identity, boolean replace, int resourceid, LwM2mResource value) {
        System.out.println("client write: value:" + JSON.toJSONString(value.getValue()));
        return WriteResponse.success();
    }

//    @Override
//    public WriteResponse write(ServerIdentity identity, int resourceid, LwM2mResource value) {
//        switch (resourceid) {
//            case 15:
////                setCollectionPeriod((Long) value.getValue());
//                return WriteResponse.success();
//        }
//        return WriteResponse.notFound();
//    }
//
    @Override
    public ExecuteResponse execute(ServerIdentity identity, int resourceid, Arguments arguments) {
        System.out.println("client execute resourceid :" + resourceid + ":" + JSON.toJSONString(arguments));
        switch (resourceid) {
            case 12:
//                start();
                return ExecuteResponse.success();
        }
        return ExecuteResponse.success();
    }
}
