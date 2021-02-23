package com.yxy.nova.mwh.utils.constant;

import java.util.Arrays;

/**
 * 全国地区枚举类
 * 因山西和陕西两省拼音相同,在这两省拼音后加简称(山西:晋, 陕西:秦)拼音以示区分
 * @author maming.zhong
 * @date 2019-07-08 12:47
 */
public enum ProvinceEnum {
    BEI_JING       ("110000", "北京"),
    TIAN_JIN       ("120000", "天津"),
    HE_BEI         ("130000", "河北"),
    //山西(晋)
    SHAN_XI_JIN    ("140000", "山西"),
    NEI_MENG_GU    ("150000", "内蒙古"),
    LIAO_NING      ("210000", "辽宁"),
    JI_LIN         ("220000", "吉林"),
    HEI_LONG_JIANG ("230000", "黑龙江"),
    SHANG_HAI      ("310000", "上海"),
    JIANG_SU       ("320000", "江苏"),
    ZHE_JIANG      ("330000", "浙江"),
    AN_HUI         ("340000", "安徽"),
    FU_JIAN        ("350000", "福建"),
    JIANG_XI       ("360000", "江西"),
    SHAN_DONG      ("370000", "山东"),
    HE_NAN         ("410000", "河南"),
    HU_BEI         ("420000", "湖北"),
    HU_NAN         ("430000", "湖南"),
    GUANG_DONG     ("440000", "广东"),
    GUANG_XI       ("450000", "广西"),
    HAI_NAN        ("460000", "海南"),
    CHONG_QING     ("500000", "重庆"),
    SI_CHUAN       ("510000", "四川"),
    GUI_ZHOU       ("520000", "贵州"),
    YUN_NAN        ("530000", "云南"),
    XI_ZANG        ("540000", "西藏"),
    //陕西(秦)
    SHAN_XI_QIN    ("610000", "陕西"),
    GAN_SU         ("620000", "甘肃"),
    QING_HAI       ("630000", "青海"),
    NING_XIA       ("640000", "宁夏"),
    XIN_JIANG      ("650000", "新疆"),
    TAI_WANG       ("710000", "台湾"),
    HONG_KONG      ("810000", "香港"),
    MACAU          ("820000", "澳门");

    //编码
    private String code;
    //名称
    private String desc;

    ProvinceEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据地区编码匹配枚举
     * @param code
     * @return 如果匹配不到返回null
     */
    public static ProvinceEnum getByCode(String code) {
        return Arrays.stream(ProvinceEnum.values())
                .filter(entry -> entry.getCode().equals(code))
                .findFirst().orElse(null);
    }

    /**
     * 根据地区名称匹配枚举
     * @param desc
     * @return 如果匹配不到返回null
     */
    public static ProvinceEnum getByDesc(String desc) {
        return Arrays.stream(ProvinceEnum.values())
                .filter(entry -> startsWith(desc , entry.getDesc(), false))
                .findFirst().orElse(null);
    }

    private static boolean startsWith(final String str, final String prefix, final boolean ignoreCase) {
        if (str == null || prefix == null) {
            return str == null && prefix == null;
        }
        if (prefix.length() > str.length()) {
            return false;
        }
        for (int i = 0; i <prefix.length(); i++) {
            char c1 = str.charAt(i);
            char c2 = prefix.charAt(i);
            if (c1 == c2) {
                continue;
            }

            if (!ignoreCase) {
                return false;
            }

            if (Character.toUpperCase(c1) != Character.toUpperCase(c2)
                    && Character.toLowerCase(c1) != Character.toLowerCase(c2)) {
                return false;
            }
        }
        return true;
    }
}
