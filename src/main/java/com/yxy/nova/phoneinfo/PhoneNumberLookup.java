package com.yxy.nova.phoneinfo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * 电话号码归属信息查询
 *
 * @author xq.h
 * 2019/10/18 21:25
 **/
@Slf4j
@Data
public class PhoneNumberLookup {
    private static final String PHONE_NUMBER_GEO_PHONE_DAT = "phoneinfo/phone.dat";
    /**
     * 数据版本hash值, 版本:202302
     */
    private static final int DATA_HASH = 1990190400;

    private ByteBuffer originalByteBuffer;
    private int indicesStartOffset;
    private int indicesEndOffset;

    private BinarySearchAlgorithmImpl algorithm = new BinarySearchAlgorithmImpl();


    public List<PhoneNumberInfo> getAll() {
        ByteBuffer byteBuffer = originalByteBuffer.asReadOnlyBuffer().order(ByteOrder.LITTLE_ENDIAN);
        int left = indicesStartOffset;
        int right = indicesEndOffset;
        List<PhoneNumberInfo> list = new ArrayList<>();
        try {
            for (int i = left; i<=right; i=i+9) {
                int ps = alignPosition(i);
                Optional<PhoneNumberInfo> extract = extract(ps, byteBuffer);
                System.out.println(JSONObject.toJSONString(extract.get()));
                list.add(extract.get());
            }
        } catch (Exception e) {

        }
        return list;
    }

    private Optional<PhoneNumberInfo> extract(int indexStart, ByteBuffer byteBuffer) {
        byteBuffer.position(indexStart);
        //noinspection unused
        int prefix = byteBuffer.getInt(); // it is necessary
        int infoStartIndex = byteBuffer.getInt();
        byte ispMark = byteBuffer.get();
        ISP isp = ISP.of(ispMark).orElse(ISP.UNKNOWN);

        byte[] bytes = new byte[determineInfoLength(infoStartIndex, byteBuffer)];
        byteBuffer.get(bytes);
        String oriString = new String(bytes);
        Attribution attribution = parse(oriString);

        return Optional.of(new PhoneNumberInfo(String.valueOf(prefix), attribution, isp));
    }

    private int determineInfoLength(int infoStartIndex, ByteBuffer byteBuffer) {
        byteBuffer.position(infoStartIndex);
        //noinspection StatementWithEmptyBody
        while ((byteBuffer.get()) != 0) {
            // just to find index of next '\0'
        }
        int infoEnd = byteBuffer.position() - 1;
        byteBuffer.position(infoStartIndex); //reset to info start index
        return infoEnd - infoStartIndex;
    }

    private Attribution parse(String ori) {
        String[] split = ori.split("\\|");
        if (split.length < 4) {
            throw new IllegalStateException("content format error");
        }
        return Attribution.builder()
                .province(split[0])
                .city(split[1])
                .zipCode(split[2])
                .areaCode(split[3])
                .build();
    }

    /**
     * 对齐
     */
    private int alignPosition(int pos) {
        int remain = (pos - indicesStartOffset) % 9;
        if (pos - indicesStartOffset < 9) {
            return pos - remain;
        } else if (remain != 0) {
            return pos + 9 - remain;
        } else {
            return pos;
        }
    }

    public void loadData(byte[] data) {
        originalByteBuffer = ByteBuffer.wrap(data)
                .asReadOnlyBuffer()
                .order(ByteOrder.LITTLE_ENDIAN);
        //noinspection unused
        int dataVersion = originalByteBuffer.getInt(); // dataVersion not valid, don't know why
        indicesStartOffset = originalByteBuffer.getInt(4);
        indicesEndOffset = originalByteBuffer.capacity();
    }

    public byte[] initAllByte() {
        try {
            byte[] allBytes;
            try (final InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(PHONE_NUMBER_GEO_PHONE_DAT);
                 final ByteArrayOutputStream output = new ByteArrayOutputStream()) {
                int n;
                byte[] buffer = new byte[1024 * 4];
                while (-1 != (n = requireNonNull(inputStream, "PhoneNumberLookup: Failed to get inputStream.").read(buffer))) {
                    output.write(buffer, 0, n);
                }
                allBytes = output.toByteArray();
            }
            int hashCode = Arrays.hashCode(allBytes);
            log.debug("loaded datasource, size: {}, hash: {}", allBytes.length, hashCode);
            if (hashCode != DATA_HASH) {
                throw new IllegalStateException("Hash of data not match, expect: " + DATA_HASH + ", actually: " + hashCode);
            }
            loadData(allBytes);
            return allBytes;
        } catch (Exception e) {
            log.error("failed to init PhoneNumberLookUp", e);
            throw new RuntimeException(e);
        }
    }


}
