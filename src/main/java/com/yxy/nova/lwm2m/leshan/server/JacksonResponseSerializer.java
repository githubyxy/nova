package com.yxy.nova.lwm2m.leshan.server;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.eclipse.leshan.core.response.*;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class JacksonResponseSerializer extends StdSerializer<LwM2mResponse> {

    private static final long serialVersionUID = -1249267471664578631L;

    protected JacksonResponseSerializer(Class<LwM2mResponse> t) {
        super(t);
    }

    public JacksonResponseSerializer() {
        this(null);
    }

    @Override
    public void serialize(LwM2mResponse src, JsonGenerator gen, SerializerProvider provider) throws IOException {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put("status", src.getCode().toString());
        map.put("valid", src.isValid());
        map.put("success", src.isSuccess());
        map.put("failure", src.isFailure());

        if (src instanceof ReadResponse) {
            map.put("content", ((ReadResponse) src).getContent());
        } else if (src instanceof DiscoverResponse) {
            map.put("objectLinks", ((DiscoverResponse) src).getObjectLinks());
        } else if (src instanceof CreateResponse) {
            map.put("location", ((CreateResponse) src).getLocation());
        } else if (src instanceof ReadCompositeResponse) {
            map.put("content", ((ReadCompositeResponse) src).getContent());
        }

        if (src.isFailure() && src.getErrorMessage() != null && !src.getErrorMessage().isEmpty()) {
            map.put("errormessage", src.getErrorMessage());
        }

        gen.writeObject(map);
    }
}
