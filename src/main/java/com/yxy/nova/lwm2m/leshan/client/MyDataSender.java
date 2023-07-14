//package com.yxy.nova.lwm2m.leshan.client;
//
//import org.eclipse.leshan.client.send.DataSenderManager;
//import org.eclipse.leshan.client.send.ManualDataSender;
//import org.eclipse.leshan.client.send.NoDataException;
//import org.eclipse.leshan.client.servers.ServerIdentity;
//import org.eclipse.leshan.core.node.TimestampedLwM2mNodes;
//import org.eclipse.leshan.core.request.ContentFormat;
//
//public class MyDataSender extends ManualDataSender {
//
//    private DataSenderManager dataSenderManager;
//
//
//    @Override
//    public void sendCollectedData(ServerIdentity server, ContentFormat format, long timeoutInMs, boolean noFlush) throws NoDataException {
//        TimestampedLwM2mNodes data = null;
//
//        this.dataSenderManager.sendData(server, format, (TimestampedLwM2mNodes) null, (response) -> {
//            if (response.isFailure()) {
//                this.restoreData(data);
//            }
//
//        }, (error) -> {
//            this.restoreData(data);
//        }, timeoutInMs);
//    }
//}
