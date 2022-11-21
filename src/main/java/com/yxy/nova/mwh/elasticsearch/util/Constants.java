package com.yxy.nova.mwh.elasticsearch.util;

import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.Consts;
import com.yxy.nova.mwh.elasticsearch.facade.org.apache.http.entity.ContentType;
import com.alibaba.fastjson.parser.Feature;

import java.nio.charset.Charset;

public class Constants {

    public static final Charset UTF8 = Charset.forName("UTF-8");
    public static final ContentType TEXT_PLAIN_UTF8 = ContentType.create("text/plain", Consts.UTF_8);


    public static final int JSON_FEATURE;

    static {
        int features = 0;
        features |= Feature.AutoCloseSource.getMask();
        features |= Feature.InternFieldNames.getMask();
        features |= Feature.AllowUnQuotedFieldNames.getMask();
        features |= Feature.AllowSingleQuotes.getMask();
        features |= Feature.AllowArbitraryCommas.getMask();
        features |= Feature.SortFeidFastMatch.getMask();
        features |= Feature.IgnoreNotMatch.getMask();
        JSON_FEATURE = features;
    }

    /**
     * 条件中是否有ID信息
     */
    public enum IDINCONDITION{
        ONLYID,
        HASID,
        NOID;
    }
}
