package com.yxy.nova.lwm2m.leshan.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.eclipse.leshan.core.node.LwM2mNode;
import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.node.LwM2mSingleResource;
import org.eclipse.leshan.core.node.TimestampedLwM2mNodes;
import org.eclipse.leshan.core.observation.CompositeObservation;
import org.eclipse.leshan.core.observation.Observation;
import org.eclipse.leshan.core.observation.SingleObservation;
import org.eclipse.leshan.core.request.ObserveRequest;
import org.eclipse.leshan.core.request.ReadRequest;
import org.eclipse.leshan.core.request.SendRequest;
import org.eclipse.leshan.core.request.WriteRequest;
import org.eclipse.leshan.core.response.*;
import org.eclipse.leshan.core.util.Hex;
import org.eclipse.leshan.server.californium.LeshanServer;
import org.eclipse.leshan.server.californium.LeshanServerBuilder;
import org.eclipse.leshan.server.observation.ObservationListener;
import org.eclipse.leshan.server.registration.Registration;
import org.eclipse.leshan.server.registration.RegistrationListener;
import org.eclipse.leshan.server.registration.RegistrationService;
import org.eclipse.leshan.server.registration.RegistrationUpdate;
import org.eclipse.leshan.server.send.SendListener;

import java.util.Base64;
import java.util.Collection;

public class LeshanServerDemo {
    public static void main(String[] args) {
        LeshanServerBuilder builder = new LeshanServerBuilder();
        LeshanServer server = builder.build();
        server.start();

        server.getRegistrationService().addListener(new RegistrationListener() {

            public void registered(Registration registration, Registration previousReg,
                                   Collection<Observation> previousObsersations) {
                System.out.println("LeshanServerDemo new device: " + registration.getEndpoint());
//                try {
////                    WriteResponse response = server.send(registration, new WriteRequest( 3303,0,5700, 23));
//                    ReadResponse response = server.send(registration, new ReadRequest(3303,0,5700));
//                    if (response.isSuccess()) {
////                        LwM2mSingleResource content = (LwM2mSingleResource)response.getContent();
//                        ObjectMapper mapper = new ObjectMapper();
//                        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//                        SimpleModule module = new SimpleModule();
//                        module.addSerializer(Registration.class, new JacksonRegistrationSerializer(server.getPresenceService()));
//                        module.addSerializer(LwM2mResponse.class, new JacksonResponseSerializer());
//                        module.addSerializer(LwM2mNode.class, new JacksonLwM2mNodeSerializer());
//                        module.addDeserializer(LwM2mNode.class, new JacksonLwM2mNodeDeserializer());
//                        mapper.registerModule(module);
//                        String s = mapper.writeValueAsString(response);
//
//                        System.out.println("LeshanServerDemo value:" + s);
//                        System.out.println("LeshanServerDemo value:" + JSONObject.parseObject(s).getJSONObject("content").getString("value"));
//                        System.out.println("LeshanServerDemo value:" + Double.parseDouble(JSONObject.parseObject(s).getJSONObject("content").getString("value")));
//                        System.out.println("LeshanServerDemo value:" + new String(Hex.decodeHex(JSONObject.parseObject(s).getJSONObject("content").getString("value").toCharArray())));
//                        System.out.println("LeshanServerDemo Device time:" + response.toString());
//                    }else {
//                        System.out.println("LeshanServerDemo Failed to read:" + response.toString());
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (JsonProcessingException e) {
//                    throw new RuntimeException(e);
//                }

                //

                ObserveRequest observeRequest = new ObserveRequest(3303, 0, 5700);
                server.send(registration, observeRequest, new ResponseCallback<ObserveResponse>() {
                    @Override
                    public void onResponse(ObserveResponse observeResponse) {
                        System.out.println("LeshanServerDemo observeResponse:" + observeResponse.toString());
                    }
                }, new ErrorCallback() {
                    @Override
                    public void onError(Exception e) {
                        System.out.println("LeshanServerDemo observeResponse:" + e.getMessage());
                    }
                });
            }

            public void updated(RegistrationUpdate update, Registration updatedReg, Registration previousReg) {
                System.out.println("LeshanServerDemo device is still here: " + updatedReg.getEndpoint());
            }

            public void unregistered(Registration registration, Collection<Observation> observations, boolean expired,
                                     Registration newReg) {
                System.out.println("LeshanServerDemo device left: " + registration.getEndpoint());
            }
        });

        server.getObservationService().addListener(new ObservationListener() {

            @Override
            public void newObservation(Observation observation, Registration registration) {
                System.out.println("LeshanServerDemo newObservation: " + JSON.toJSONString(observation));
            }

            @Override
            public void cancelled(Observation observation) {
                System.out.println("LeshanServerDemo cancelled: " + JSON.toJSONString(observation));
            }

            @Override
            public void onResponse(SingleObservation observation, Registration registration, ObserveResponse response) {
                System.out.println("LeshanServerDemo SingleObservation onResponse: " + JSON.toJSONString(observation));
                try {
                    ReadResponse readResponse = server.send(registration, new ReadRequest(3303, 0, 5700));
                    System.out.println("LeshanServerDemo SingleObservation ReadResponse: " + JSON.toJSONString(readResponse.isSuccess()));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onResponse(CompositeObservation observation, Registration registration, ObserveCompositeResponse response) {
                System.out.println("LeshanServerDemo CompositeObservation onResponse: " + JSON.toJSONString(observation));
            }

            @Override
            public void onError(Observation observation, Registration registration, Exception error) {
                System.out.println("LeshanServerDemo onError: " + JSON.toJSONString(observation));
            }
        });

        server.getSendService().addListener(new SendListener() {
            @Override
            public void dataReceived(Registration registration, TimestampedLwM2mNodes data, SendRequest request) {
                System.out.println("LeshanServerDemo SendListener dataReceived: " + JSON.toJSONString(data));
            }

            @Override
            public void onError(Registration registration, Exception error) {

            }
        });

    }


}
