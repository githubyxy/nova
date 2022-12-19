package test.util;

import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;

public class Signature {
    private Signature() {
    }

    public static String sign(String secret, Map<String, String> header, Map<String, String> data) {
        return sign(secret, header, data, (byte[]) null);
    }

    @SneakyThrows
    public static String sign(String secret, Map<String, String> header, Map<String, String> param, byte[] body) {
        Map<String, List<String>> ps = new LinkedHashMap<>();
        param.forEach((key, value) -> ps.put(key, Collections.singletonList(value)));
        return sign(secret, header.get("application"), header.get("timestamp"), ps, body);
    }

    @SneakyThrows
    public static String sign(String secret, String application, String timestamp, Map<String, List<String>> param, byte[] body) {
        if (secret == null) {
            throw new Exception("Secret cannot be null");
        } else if (application == null) {
            throw new Exception("Application cannot be null");
        } else if (timestamp == null) {
            throw new Exception("Timestamp cannot be null");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("application").append(":").append(application).append("\n");
            sb.append("timestamp").append(":").append(timestamp).append("\n");
            if (param != null) {
                for (Entry<String, List<String>> entry : new TreeMap<>(param).entrySet()) {
                    String key = entry.getKey();
                    List<String> ls = entry.getValue();
                    if (ls.isEmpty()) {
                        sb.append(key).append(":\n");
                    } else {
                        for (String val : ls) {
                            sb.append(key).append(":").append(val == null ? "" : val).append("\n");
                        }
                    }
                }
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(sb.toString().getBytes(StandardCharsets.UTF_8));
            if (body != null && body.length > 0) {
                baos.write(body);
                baos.write("\n".getBytes(StandardCharsets.UTF_8));
            }

            return new String(Base64.encodeBase64(encryptHMAC(secret, baos.toByteArray())));
        }
    }

    public static byte[] encryptHMAC(String secret, byte[] data) {
        byte[] bytes = null;

        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSha1");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data);
        } catch (Exception var5) {
            var5.printStackTrace(System.err);
        }

        return bytes;
    }
}