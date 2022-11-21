package com.yxy.nova.mwh.utils.mask;

/**
 * @author maming.zhong
 * @date 2019-11-12 11:43
 * @description
 */

import org.apache.commons.lang3.StringUtils;

/**
 *
 * http://oa.tongdun.me/seeyon/bulData.do?method=userView&spaceId=&id=6734778813874980077
 *
 * 字段				脱敏前				    脱敏后
 * ID_NUMBER	    350000199001016666	    350000********6666
 * BANK_CARD_NUMBER	6221386102180111666     622138*********1666
 * MOBILE		    15700000000			    157****0000
 * NAME		        张 张三 张三丰 张三丰收	    * *三 *三丰 *三丰收
 * PASSWORD		    hello_world			    ******
 * ACCOUNT		    15700000000			    157****0000
 * 				    张					    *
 * 			    	张三					    张*
 * 				    张三丰				    张*丰
 * 				    张三丰收				    张**丰
 */
public final class MaskUtil {
    private enum Field{
        VALUE, ID_NUMBER, MOBILE, NAME, BANK_CARD_NUMBER, ACCOUNT, PASSWORD;
    }
    private static final String DEFAULT_MASK = "**";
    private static final String PASSWORD_MASK  = "******";

    /**
     * 按字段脱敏
     * @param field
     * @param value
     * @return
     */
    private static final String mask(Field field, String value) {
        if(StringUtils.isBlank(value)){
            return StringUtils.EMPTY;
        }
        switch (field){
            case ID_NUMBER:
                if(value.length()>10){
                    return value.replaceAll("(?<=\\S{6})\\S(?=\\S{4})", "*");
                }
                break;
            case MOBILE:
                if(value.length()> 7){
                    return value.replaceAll("(?<=\\S{3})\\S(?=\\S{4})", "*");
                }
                break;
            case NAME:
                return value.replaceAll("\\S{1}(\\S*)", "*$1");
            case BANK_CARD_NUMBER:
                if(value.length()>10){
                    return value.replaceAll("(?<=\\S{6})\\S(?=\\S{4})", "*");
                }
                break;
            case ACCOUNT:
                if(value.length() == 1){
                    return "*";
                } else if(value.length() == 2){
                    return value.replaceAll("(\\S{1})\\S{1}","$1*");
                } else if(value.length() == 11){
                    return value.replaceAll("(?<=\\S{3})\\S(?=\\S{4})", "*");
                } else if(value.length()>10){
                    return value.replaceAll("(?<=\\S{6})\\S(?=\\S{4})", "*");
                } else {
                    return value.replaceAll("(?<=\\S{1})\\S(?=\\S{1})", "*");
                }
            case PASSWORD:
                return PASSWORD_MASK;
            default:
                break;
        }
        return DEFAULT_MASK;
    }

    /**
     * 身份证号码脱敏
     * @param value
     * @return
     */
    public static final String maskIdNumber(String value){
        return mask(Field.ID_NUMBER, value);
    }

    /**
     * 手机号码脱敏
     * @param value
     * @return
     */
    public static final String maskMobile(String value){
        return mask(Field.MOBILE, value);
    }

    /**
     * 姓名脱敏
     * @param value
     * @return
     */
    public static final String maskName(String value){
        return mask(Field.NAME, value);
    }

    /**
     * 银行卡号脱敏
     * @param value
     * @return
     */
    public static final String maskBankCardNumber(String value){
        return mask(Field.BANK_CARD_NUMBER, value);
    }

    /**
     * 账号脱敏
     * @param value
     * @return
     */
    public static final String maskAccount(String value){
        return mask(Field.ACCOUNT, value);
    }

    /**
     * 密码脱敏
     * @param value
     * @return
     */
    public static final String maskPassword(String value){
        return mask(Field.PASSWORD, value);
    }

    /**
     * 其他值脱敏,返回**
     * @param value
     * @return **
     */
    public static final String maskValue(String value){
        return mask(Field.VALUE, value);
    }
}