package com.yxy.nova.lwm2m.leshan.server;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.leshan.core.link.Link;
import org.eclipse.leshan.core.link.attributes.Attribute;
import org.eclipse.leshan.core.node.LwM2mPath;
import org.eclipse.leshan.core.request.BindingMode;
import org.eclipse.leshan.server.queue.PresenceService;
import org.eclipse.leshan.server.registration.Registration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class JacksonRegistrationSerializer extends StdSerializer<Registration> {

    private static final long serialVersionUID = -2828961931685566265L;

    private final PresenceService presenceService;

    protected JacksonRegistrationSerializer(Class<Registration> t, PresenceService presenceService) {
        super(t);
        this.presenceService = presenceService;
    }

    public JacksonRegistrationSerializer(PresenceService presenceService) {
        this(null, presenceService);
    }

    @Override
    public void serialize(Registration src, JsonGenerator gen, SerializerProvider provider) throws IOException {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put("endpoint", src.getEndpoint());
        map.put("registrationId", src.getId());

        map.put("registrationDate", src.getRegistrationDate());

        map.put("lastUpdate", src.getLastUpdate());

        map.put("address", src.getAddress().getHostAddress() + ":" + src.getPort());
        map.put("smsNumber", src.getSmsNumber());
        map.put("lwM2mVersion", src.getLwM2mVersion().toString());
        map.put("lifetime", src.getLifeTimeInSec());
        map.put("bindingMode", BindingMode.toString(src.getBindingMode()));

        map.put("rootPath", src.getRootPath());
        map.put("objectLinks", serializeLinks(src.getSortedObjectLinks()));
        map.put("secure", src.getIdentity().isSecure());
        map.put("additionalRegistrationAttributes", src.getAdditionalRegistrationAttributes());
        map.put("queuemode", src.usesQueueMode());
        map.put("availableInstances", serializeAvailableInstances(src.getAvailableInstances()));

        if (src.usesQueueMode()) {
            map.put("sleeping", !presenceService.isClientAwake(src));
        }

        gen.writeObject(map);
    }

    private Map<Integer, Set<Integer>> serializeAvailableInstances(Set<LwM2mPath> instances) {
        SortedMap<Integer, Set<Integer>> result = new TreeMap<>();
        for (LwM2mPath path : instances) {
            Set<Integer> instancesList = result.get(path.getObjectId());
            // add list if does not already exist
            if (instancesList == null) {
                instancesList = new TreeSet<Integer>();
                result.put(path.getObjectId(), instancesList);
            }
            // add instance id to the list
            instancesList.add(path.getObjectInstanceId());
        }
        return result;
    }

    private List<Map<String, Object>> serializeLinks(Link[] links) {
        if (links == null)
            return Collections.emptyList();

        List<Map<String, Object>> jlinks = new ArrayList<>(links.length);
        for (Link link : links) {
            Map<String, Object> jlink = new HashMap<>();

            // add url
            jlink.put("url", link.getUriReference());

            // add attributes
            Map<String, String> attributes = new HashMap<>();
            for (Attribute attr : link.getAttributes()) {
                attributes.put(attr.getName(), attr.getCoreLinkValue());
            }
            jlink.put("attributes", attributes);
            jlinks.add(jlink);
        }
        return jlinks;
    }
}
