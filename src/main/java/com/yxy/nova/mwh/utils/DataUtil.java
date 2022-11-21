/*
 * Copyright 2015 FraudMetrix.cn All right reserved. This software is the
 * confidential and proprietary information of FraudMetrix.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with FraudMetrix.cn.
 */
package com.yxy.nova.mwh.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * 数据非法校验工具类
 *
 * @author chenchanglong 2015年5月21日 下午6:00:54
 */
public final class DataUtil {

    /** 大陆地区地域编码最大值 **/
    public static final int          MAX_MAINLAND_AREACODE     = 659004;
    /** 大陆地区地域编码最小值 **/
    public static final int          MIN_MAINLAND_AREACODE     = 110000;
    /** 香港地域编码值 **/
    public static final int          HONGKONG_AREACODE         = 810000;                                                                                                                                                 // 香港地域编码值
    /** 台湾地域编码值 **/
    public static final int          TAIWAN_AREACODE           = 710000;
    /** 澳门地域编码值 **/
    public static final int          MACAO_AREACODE            = 820000;

    /** 数字正则 **/
    public static final String       regexNum                  = "^[0-9]*$";
    /** 闰年生日正则 **/
    public static final String       regexBirthdayInLeapYear   = "^((19[0-9]{2})|(200[0-9])|(201[0-5]))((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))$";
    /** 平年生日正则 **/
    public static final String       regexBirthdayInCommonYear = "^((19[0-9]{2})|(200[0-9])|(201[0-5]))((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))$";
    /** 邮箱严格正则 **/
    public static final String       regexStrictEmail          = "^[A-Za-z0-9](([_\\.\\-]?[a-zA-Z0-9_\\-]+)*)@([A-Za-z0-9]+)(([\\.\\-]?[a-zA-Z0-9]+)*)\\.([A-Za-z]{2,})$";
    /** 邮箱普通正则 **/
    public static final String       regexCommonEmail          = "^[A-Za-z0-9_\\.\\-]+@[A-Za-z0-9_\\.\\-]+\\.[A-Za-z]{2,}";
    /** 手机号正则 **/
    public static final String       regexMobile               = "^(\\+86[-]?)?1[0-9]{10}$";
    /** 座机号正则 **/
    public static final String       regexPhone                = "^([0-9]{3,4}[-]?)?([0-9]{7,8})$";
    /** QQ正则 **/
    public static final String       regexQQ                   = "^[1-9][0-9]{4,9}$";

    /** 手机号段 **/
    public static final String[]     mobileSegs                = new String[] { "13","141", "145","146", "147", "149",
            "150", "151", "152", "153", "155", "156", "157", "158", "159",
            "165","166",
            "170", "171", "172","173", "174","175", "176", "177", "178",
            "18",
            "19"             };

    /** 座机区号 **/
    public static final String[]     phoneAreaCodes            = new String[] { "010", "02",
            "031", "0335", "0349", "035", "037", "039",
            "041", "0421", "0427", "0429", "043", "0440", "045", "0464", "0467", "0468", "0469", "047", "0482", "0483",
            "051", "0523", "0527", "053", "0543", "0546", "055", "0561", "0562", "0563", "0564", "0565", "0566", "057", "0580", "059",
            "0631", "0632", "0633", "0634", "0635", "0660", "0661", "0662", "0663", "0668", "0691", "0692",
            "0701", "071", "0722", "0724", "0728", "073", "0743", "0744", "0745", "0746", "075", "0760", "0762", "0763", "0765", "0766", "0768", "0769", "077", "079",
            "081", "0825", "0826", "0827", "083", "0840", "085", "087", "0881", "0883", "0886", "0887", "0888", "089",
            "0901", "0902", "0903", "0906", "0908", "0909", "091", "093", "0941", "0943", "0951", "0952", "0953", "0954", "0955", "097", "099",
            "852", "853" };

    private static final Set<String> BLACK_ID_NUMBER_SET       = new HashSet<String>() {

                                                                   private static final long serialVersionUID = 1L;

                                                                   {
                                                                       add("111111111111111");
                                                                   }
                                                               };

    /**
     * IP-正则表达式
     */
    private static final Pattern IP_PATTERN = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");

    /**
     * <p>
     * 身份证格式弱校验，只进行长度校验和基本格式校验(前17位为数字最后一位为数字或X)
     * </p>
     */
    public static final boolean weakValidateIdNumber(String idNumber) {
        if (StringUtils.isBlank(idNumber)) {
            return false;
        }
        idNumber = idNumber.trim();
        if (!checkIdNumberRegex(idNumber)) {
            return false;
        }
        return true;
    }

    /**
     * <p>
     * 模糊身份证格式校验，校验地区码，根据模糊类型进行校验，模糊类型为“1”则为模糊后四位，“2”为模糊出生月日四位，“3”为模糊出生年月日八位 若模糊类型为1，则校验出生年月日；若模糊类型为2，则只校验出生年；
     * </p>
     */
    public static final boolean validateFuzzyIdNumber(String fuzzyId, String fuzzyType) {
        if (StringUtils.isAnyBlank(fuzzyId, fuzzyType)) {
            return false;
        }
        fuzzyId = fuzzyId.trim();
        if (!checkFuzzyIdFirstSixRegex(fuzzyId.substring(0, 6))) {
            return false;
        }
        if (!checkIdNumberArea(fuzzyId.substring(0, 6))) {
            return false;
        }
        if (fuzzyType.equals("1")) {
            if (!checkFuzzyIdFirstFourteenRegex(fuzzyId.substring(0, 14)) || !checkBirthday(fuzzyId.substring(6, 14))) {
                return false;
            }
        }
        if (fuzzyType.equals("2")) {
            if (!checkFuzzyIdFirstTenRegex(fuzzyId.substring(0, 10))
                || !checkFuzzyIdLastFourRegex(fuzzyId.substring(14, 18))) {
                return false;
            }
            Integer year = Integer.parseInt(fuzzyId.substring(6, 10));
            Calendar cal = Calendar.getInstance();
            if (year < 1900 || year > cal.get(Calendar.YEAR)) {
                return false;
            }
        }

        if (fuzzyType.equals("3") && !checkFuzzyIdLastFourRegex(fuzzyId.substring(14, 18))) {
            return false;
        }
        return true;
    }

    /**
     * <p>
     * 身份证格式强校验
     * </p>
     * <p>
     * 身份证号码验证 1、号码的结构 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码， 八位数字出生日期码，三位数字顺序码和一位数字校验码。 2、地址码(前六位数）
     * 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。 3、出生日期码（第七位至十四位） 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。
     * 4、顺序码（第十五位至十七位） 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号， 顺序码的奇数分配给男性，偶数分配给女性。 5、校验码（第十八位数） （1）十七位数字本体码加权求和公式 S =
     * Sum(Ai * Wi), i = 0, ... , 16 ，先对前17位数字的权求和 Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5
     * 8 4 2 （2）计算模 Y = mod(S, 11) （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9 8 7 6 5 4 3 2
     * </p>
     */
    public static final boolean strongValidateIdNumber(String idNumber) {
        if (StringUtils.isBlank(idNumber)) {
            return false;
        }
        idNumber = idNumber.trim();

        if (BLACK_ID_NUMBER_SET.contains(idNumber)) {
            return false;
        }
        if (!checkIdNumberRegex(idNumber)) {
            return false;
        }
        if (!checkIdNumberArea(idNumber.substring(0, 6))) {
            return false;
        }
        idNumber = convertFifteenToEighteen(idNumber);
        if (!checkBirthday(idNumber.substring(6, 14))) {
            return false;
        }
        if (!checkIdNumberVerifyCode(idNumber)) {
            return false;
        }
        return true;
    }

    /**
     * 身份证正则校验
     */
    private static boolean checkIdNumberRegex(String idNumber) {
        return Pattern.matches("^([0-9]{17}[0-9Xx])|([0-9]{15})$", idNumber);
    }

    /**
     * 模糊身份证前六位正则校验
     */
    private static boolean checkFuzzyIdFirstSixRegex(String fuzzyId) {
        return Pattern.matches("^([0-9]{6})$", fuzzyId);
    }

    /**
     * 模糊身份证前十位正则校验
     */
    private static boolean checkFuzzyIdFirstTenRegex(String fuzzyId) {
        return Pattern.matches("^([0-9]{10})$", fuzzyId);
    }

    /**
     * 模糊身份证前十四位正则校验
     */
    private static boolean checkFuzzyIdFirstFourteenRegex(String fuzzyId) {
        return Pattern.matches("^([0-9]{14})$", fuzzyId);
    }

    /**
     * 模糊身份证后四位校验
     */
    private static boolean checkFuzzyIdLastFourRegex(String fuzzyId) {
        return Pattern.matches("^([0-9]{3}[0-9Xx])$", fuzzyId);
    }

    /**
     * 身份证地区码检查
     */
    private static boolean checkIdNumberArea(String idNumberArea) {
        int areaCode = Integer.parseInt(idNumberArea);
        if (areaCode == HONGKONG_AREACODE || areaCode == MACAO_AREACODE || areaCode == TAIWAN_AREACODE) {
            return true;
        }
        if (areaCode <= MAX_MAINLAND_AREACODE && areaCode >= MIN_MAINLAND_AREACODE) {
            return true;
        }
        return false;
    }

    /**
     * 将15位身份证转换为18位
     */
    private static String convertFifteenToEighteen(String idNumber) {
        if (15 != idNumber.length()) {
            return idNumber;
        }
        idNumber = idNumber.substring(0, 6) + "19" + idNumber.substring(6, 15);
        idNumber = idNumber + getVerifyCode(idNumber);
        return idNumber;
    }

    /**
     * 根据身份证前17位计算身份证校验码
     */
    private static String getVerifyCode(String idNumber) {
        if (!Pattern.matches(regexNum, idNumber.substring(0, 17))) {
            return null;
        }
        String[] ValCodeArr = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
        String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2" };

        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum = sum + Integer.parseInt(String.valueOf(idNumber.charAt(i))) * Integer.parseInt(Wi[i]);
        }
        return ValCodeArr[sum % 11];
    }

    /**
     * 身份证出生日期嘛检查
     */
    private static boolean checkBirthday(String idNumberBirthdayStr) {
        Integer year = null;
        try {
            year = Integer.valueOf(idNumberBirthdayStr.substring(0, 4));
        } catch (Exception e) {
        }
        if (null == year) {
            return false;
        }
        if (isLeapYear(year)) {
            return Pattern.matches(regexBirthdayInLeapYear, idNumberBirthdayStr);
        } else {
            return Pattern.matches(regexBirthdayInCommonYear, idNumberBirthdayStr);
        }
    }

    /**
     * 判断是否为闰年
     */
    private static boolean isLeapYear(int year) {
        return (year % 400 == 0) || (year % 100 != 0 && year % 4 == 0);
    }

    /**
     * 身份证校验码检查
     */
    private static boolean checkIdNumberVerifyCode(String idNumber) {
        return getVerifyCode(idNumber).equalsIgnoreCase(idNumber.substring(17));
    }

    /**
     * 银行卡号弱校验
     */
    public static boolean weakValidateCardNumber(String cardNumber) {
        if (StringUtils.isBlank(cardNumber)) {
            return false;
        }
        cardNumber = cardNumber.trim();
        if (!checkCardNumberLength(cardNumber)) {
            return false;
        }
        if (!checkCardNumberRegex(cardNumber)) {
            return false;
        }
        return true;
    }

    /**
     * 银行卡号强校验，包括长度和luhn算法校验
     */
    public static boolean strongValidateCardNumber(String cardNumber) {
        if (StringUtils.isBlank(cardNumber)) {
            return false;
        }
        cardNumber = cardNumber.trim();
        if (!checkCardNumberLength(cardNumber)) {
            return false;
        }
        if (!checkCardNumberRegex(cardNumber)) {
            return false;
        }
        if (!checkByLuhn(cardNumber)) {
            return false;
        }
        return true;
    }

    /**
     * 检查银行卡号正则
     */
    private static boolean checkCardNumberRegex(String cardNumber) {
        return Pattern.matches(regexNum, cardNumber);
    }

    /**
     * 检查银行卡号长度
     */
    private static boolean checkCardNumberLength(String cardNumber) {
        int len = cardNumber.length();
        return len >= 13 && len <= 19;
    }

    /**
     * <p>
     * luhn算法校验
     * </p>
     * <ol>
     * <li>从卡号最后一位数字开始,偶数位乘以2,如果乘以2的结果是两位数，将两个位上数字相加保存。</li>
     * <li>把所有数字相加,得到总和。</li>
     * <li>如果银行卡号码是合法的，总和可以被10整除。</li>
     * </ol>
     */
    private static boolean checkByLuhn(String cardNumber) {
        int len = cardNumber.length();
        int sum = 0;
        for (int i = 0; i < len; i++) {
            int c = cardNumber.charAt(len - 1 - i) - '0';
            if (1 == i % 2) {
                sum += (2 * c) / 10 + (2 * c) % 10;
            } else {
                sum += c;
            }
        }
        return 0 == sum % 10;
    }

    /**
     * 手机号强校验
     */
    public static boolean strongValidateMobile(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return false;
        }
        mobile = mobile.trim();
        if (!checkMobileRegex(mobile)) {
            return false;
        }
        if (!checkMobileSegment(mobile)) {
            return false;
        }
        return true;
    }

    /**
     * 手机号正则校验
     */
    private static boolean checkMobileRegex(String mobile) {
        return Pattern.matches(regexMobile, mobile);
    }

    /**
     * 手机号段校验
     */
    private static boolean checkMobileSegment(String mobile) {
        int index = mobile.indexOf("1");
        String segment = mobile.substring(index, index + 4);
        for (String mobileSeg : mobileSegs) {
            if (segment.startsWith(mobileSeg)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 手机号弱校验
     */
    public static boolean weakValidateMobile(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return false;
        }
        mobile = mobile.trim();
        if (!checkMobilePrefix(mobile)) {
            return false;
        }
        if (!checkMobileLength(mobile)) {
            return false;
        }
        return true;
    }

    /**
     * 检查手机号长度
     */
    private static boolean checkMobileLength(String mobile) {
        int index = mobile.indexOf("1");
        if (index < 0) {
            return false;
        }
        return 11 == mobile.substring(index).length();
    }

    /**
     * 检查手机号前缀
     */
    private static boolean checkMobilePrefix(String mobile) {
        return mobile.startsWith("1") || mobile.startsWith("86") || mobile.startsWith("+86");
    }

    /**
     * 邮箱格式强校验
     */
    public static boolean strongValidateEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        email = email.trim();
        if (!Pattern.matches(regexStrictEmail, email)) {
            return false;
        }
        return true;
    }

    /**
     * 邮箱格式弱校验
     */
    public static boolean weakValidateEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        email = email.trim();
        if (!Pattern.matches(regexCommonEmail, email)) {
            return false;
        }
        return true;
    }

    /**
     * 座机格式强校验
     */
    public static boolean strongValidatePhone(String phone) {
        if (StringUtils.isBlank(phone)) {
            return false;
        }
        phone = phone.trim();
        if (!checkPhoneRegex(phone)) {
            return false;
        }
        if (!checkPhoneAreaCode(phone)) {
            return false;
        }
        return true;
    }

    /**
     * 获取座机区号
     * @param phone
     * @return
     */
    public static String getPhoneAreaCode(String phone) {
        return Stream.of(phoneAreaCodes)
                .filter(areaCode -> phone.startsWith(areaCode))
                .findFirst()
                .orElse(null);
    }

    /**
     * 校验是否是手机号或者座机
     * @param phone
     * @return
     */
    public static boolean strongValidateMobileOrPhone(String phone) {
        return strongValidateMobile(phone)
                || strongValidatePhone(phone);
    }

    /**
     * 检查座机区号
     */
    private static boolean checkPhoneAreaCode(String phone) {
        if (phone.length() > 8) {
            for (String areaCode : phoneAreaCodes) {
                if (phone.startsWith(areaCode)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * 座机格式弱校验
     */
    public static boolean weakValidatePhone(String phone) {
        if (StringUtils.isBlank(phone)) {
            return false;
        }
        phone = phone.trim();
        if (!checkPhoneRegex(phone)) {
            return false;
        }
        return true;
    }

    /**
     * 检查座机号正则
     */
    private static boolean checkPhoneRegex(String phone) {
        return Pattern.matches(regexPhone, phone);
    }

    /**
     * QQ格式强校验
     */
    public static boolean strongValidateQQ(String qq) {
        if (StringUtils.isBlank(qq)) {
            return false;
        }
        qq = qq.trim();
        if (!checkQqRegex(qq)) {
            return false;
        }
        return true;
    }

    /**
     * 检查QQ正则
     */
    private static boolean checkQqRegex(String qq) {
        return Pattern.matches(regexQQ, qq);
    }

    /**
     * QQ格式弱校验
     */
    public static boolean weakValidateQQ(String qq) {
        if (StringUtils.isBlank(qq)) {
            return false;
        }
        qq = qq.trim();
        if (!checkQQLength(qq)) {
            return false;
        }
        if (!Pattern.matches(regexNum, qq)) {
            return false;
        }
        return true;
    }

    /**
     * 检查QQ长度
     */
    private static boolean checkQQLength(String qq) {
        return qq.length() >= 5 && qq.length() <= 10;
    }

    /**
     * 校验ip地址的格式正确性
     * @param ip
     * @return
     */
    public static boolean checkIP(String ip) {
        return StringUtils.isNotBlank(ip)
                && IP_PATTERN.matcher(ip).matches();
    }

    /**
     * 校验端口正确性
     * @param portStr
     * @return
     */
    public static boolean checkPort(String portStr) {
        try {
            int port = Integer.parseInt(portStr);
            return port >= 1 && port <= 65535;
        } catch (Throwable thr) {
            return false;
        }

    }
}
