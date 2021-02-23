package com.yxy.nova.mwh.elasticsearch.util;

import com.yxy.nova.mwh.elasticsearch.dto.SearchRequest;

import java.util.Map;
import java.util.Random;

/**
 * 生成查询url
 */
public class GenerateURL {


    private static final Random random = new Random();

    public static String requestId() {
        return "ES" + random8() + random8();
    }

    private static String random8() {
        return String.format("%08d", random.nextInt(100000000));
    }


    /**
     * 创建查询url
     * @param request
     * @return
     */
    public static String buildUrl(SearchRequest request) {
        Map<String, String> params = request.getQueryString();
        String indexEncoded = AssistantUtil.urlEncode(request.getIndex());
        String paramsString = encodeParams(params);
        if (request.getType() == null) {
            return String.format("/%s/_search%s", indexEncoded, paramsString);
        }
        String typeEncoded = AssistantUtil.urlEncode(request.getType());
        return String.format("/%s/%s/_search%s", indexEncoded, typeEncoded, paramsString);
    }

    private static String encodeParams(Map<String, String> params) {
        if (params.isEmpty()) return "";
        StringBuilder result = new StringBuilder();
        result.append("?");
        for (String key : params.keySet()) {
            if (result.length() > 1) {
                result.append("&");
            }
            result.append(AssistantUtil.urlEncode(key));
            String value = params.get(key);
            if (value == null) continue;
            result.append("=");
            result.append(AssistantUtil.urlEncode(value));
        }
        return result.toString();
    }
}
