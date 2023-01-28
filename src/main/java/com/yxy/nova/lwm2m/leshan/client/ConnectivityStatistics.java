package com.yxy.nova.lwm2m.leshan.client;

import com.alibaba.fastjson.JSON;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.client.servers.ServerIdentity;
import org.eclipse.leshan.core.node.LwM2mObjectInstance;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.core.response.WriteResponse;

public class ConnectivityStatistics extends BaseInstanceEnabler {

    @Override
    public ReadResponse read(ServerIdentity identity, int resourceid) {
        System.out.println("client read:" + JSON.toJSONString(resourceid));
        switch (resourceid) {
            case 0:
                return ReadResponse.success(resourceid, "getSmsTxCounter()");
        }
        return ReadResponse.notFound();
    }

    public WriteResponse write(ServerIdentity identity, boolean replace, LwM2mObjectInstance value) {
        System.out.println("client write:" + JSON.toJSONString(value.getResources()));
        return WriteResponse.notFound();
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
//    @Override
//    public ExecuteResponse execute(ServerIdentity identity, int resourceid, String params) {
//        switch (resourceid) {
//            case 12:
////                start();
//                return ExecuteResponse.success();
//        }
//        return ExecuteResponse.notFound();
//    }
}
