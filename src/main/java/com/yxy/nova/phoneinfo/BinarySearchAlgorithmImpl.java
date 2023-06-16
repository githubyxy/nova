package com.yxy.nova.phoneinfo;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Optional;

/**
 * 二分查找算子
 *
 * @author xq.h
 * 2019/10/19 00:12
 **/
@Slf4j
@SuppressWarnings("DuplicatedCode")
public class BinarySearchAlgorithmImpl {
    private ByteBuffer originalByteBuffer;
    private int indicesStartOffset;
    private int indicesEndOffset;

    public void loadData(byte[] data) {
        originalByteBuffer = ByteBuffer.wrap(data)
                .asReadOnlyBuffer()
                .order(ByteOrder.LITTLE_ENDIAN);
        //noinspection unused
        int dataVersion = originalByteBuffer.getInt(); // dataVersion not valid, don't know why
        indicesStartOffset = originalByteBuffer.getInt(4);
        indicesEndOffset = originalByteBuffer.capacity();
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

    private boolean isInvalidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            log.debug("phone number is null");
            return true;
        }
        int phoneNumberLength = phoneNumber.length();
        if (phoneNumberLength < 7 || phoneNumberLength > 11) {
            log.debug("phone number {} is not acceptable, length invalid, length should be 11 or 7(for left 7 numbers), actual: {}",
                    phoneNumber, phoneNumberLength);
            return true;
        }
        return false;
    }

    // 获取all号段信息
    public Optional<PhoneNumberInfo> lookup() {
        ByteBuffer byteBuffer = originalByteBuffer.asReadOnlyBuffer().order(ByteOrder.LITTLE_ENDIAN);
//        if (isInvalidPhoneNumber(phoneNumber)) {
//            return Optional.empty();
//        }
//        int attributionIdentity;
//        try {
//            attributionIdentity = Integer.parseInt(phoneNumber.substring(0, 7));
//        } catch (NumberFormatException e) {
//            log.debug("phone number {} is invalid, is it numeric?", phoneNumber);
//            return Optional.empty();
//        }
        int left = indicesStartOffset;
        int right = indicesEndOffset;
        int total = 0;
        System.out.println("left=" + left);
        System.out.println("right=" + right);
        for (int i = left; i<=right; i=i+9) {
          int ps = alignPosition(i);
//            byteBuffer.position(ps);
//            int phonePrefix = byteBuffer.getInt();
            Optional<PhoneNumberInfo> extract = extract(ps, byteBuffer);
            System.out.println(JSONObject.toJSONString(extract.get()));
            total ++;
            System.out.println("total=" + total);
        }


//        while (mid >= left && mid <= right) {
//            if (mid == right) {
//                return Optional.empty();
//            }
//            int compare = compare(mid, attributionIdentity, byteBuffer);
//            if (compare == 0) {
//                return extract(phoneNumber, mid, byteBuffer);
//            } else if (mid == left) {
//                return Optional.empty();
//            } else if (compare > 0) {
//                int tempMid = (mid + left) / 2;
//                right = mid;
//                mid = alignPosition(tempMid);
//            } else {
//                int tempMid = (mid + right) / 2;
//                left = mid;
//                mid = alignPosition(tempMid);
//            }
//        }
        return Optional.empty();
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

    private int compare(int position, int key, ByteBuffer byteBuffer) {
        byteBuffer.position(position);
        int phonePrefix = byteBuffer.getInt();
        return Integer.compare(phonePrefix, key);
    }
}
