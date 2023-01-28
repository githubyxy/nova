package com.yxy.nova.lwm2m.leshan.bootstrap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.yxy.nova.lwm2m.leshan.bootstrap.json.ByteArraySerializer;
import com.yxy.nova.lwm2m.leshan.bootstrap.json.EnumSetDeserializer;
import com.yxy.nova.lwm2m.leshan.bootstrap.json.EnumSetSerializer;
import org.apache.commons.lang.Validate;
import org.eclipse.leshan.server.bootstrap.BootstrapConfig;
import org.eclipse.leshan.server.bootstrap.InMemoryBootstrapConfigStore;
import org.eclipse.leshan.server.bootstrap.InvalidConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JSONFileBootstrapStore extends InMemoryBootstrapConfigStore {

    private static final Logger LOG = LoggerFactory.getLogger(JSONFileBootstrapStore.class);

    // lock for the two maps
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    // default location for persistence
    public static final String DEFAULT_FILE = "data/bootstrapStore.json";

    private final String filename;
    private final ObjectMapper mapper;

    public JSONFileBootstrapStore() {
        this(DEFAULT_FILE);
    }

    /**
     * @param filename the file path to persist the registry
     */
    public JSONFileBootstrapStore(String filename) {
        Validate.notEmpty(filename);

        mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(EnumSet.class, new EnumSetDeserializer());

        CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(EnumSet.class, Object.class);
        module.addSerializer(new EnumSetSerializer(collectionType));

        module.addSerializer(new ByteArraySerializer(ByteArraySerializer.ByteMode.SIGNED));
        mapper.registerModule(module);

        this.filename = filename;
        this.loadFromFile();
    }

    @Override
    public Map<String, BootstrapConfig> getAll() {
        readLock.lock();
        try {
            return super.getAll();
        } finally {
            readLock.unlock();
        }
    }

    public void addToStore(String endpoint, BootstrapConfig config) throws InvalidConfigurationException {
        super.add(endpoint, config);
    }

    @Override
    public void add(String endpoint, BootstrapConfig config) throws InvalidConfigurationException {
        writeLock.lock();
        try {
            addToStore(endpoint, config);
            saveToFile();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public BootstrapConfig remove(String enpoint) {
        writeLock.lock();
        try {
            BootstrapConfig res = super.remove(enpoint);
            saveToFile();
            return res;
        } finally {
            writeLock.unlock();
        }
    }

    // /////// File persistence
    private void loadFromFile() {
        try {
            File file = new File(filename);
            if (file.exists()) {
                try (InputStreamReader in = new InputStreamReader(new FileInputStream(file))) {
                    TypeReference<Map<String, BootstrapConfig>> bootstrapConfigTypeRef = new TypeReference<Map<String, BootstrapConfig>>() {
                    };
                    Map<String, BootstrapConfig> configs = mapper.readValue(in, bootstrapConfigTypeRef);
                    for (Map.Entry<String, BootstrapConfig> config : configs.entrySet()) {
                        addToStore(config.getKey(), config.getValue());
                    }

                }
            }
        } catch (Exception e) {
            LOG.error("Could not load bootstrap infos from file", e);
        }
    }

    private void saveToFile() {
        try {
            // Create file if it does not exists.
            File file = new File(filename);
            if (!file.exists()) {
                File parent = file.getParentFile();
                if (parent != null) {
                    parent.mkdirs();
                }
                file.createNewFile();
            }

            // Write file
            try (OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filename))) {
                out.write(mapper.writeValueAsString(getAll()));
            }
        } catch (Exception e) {
            LOG.error("Could not save bootstrap infos to file", e);
        }
    }
}
