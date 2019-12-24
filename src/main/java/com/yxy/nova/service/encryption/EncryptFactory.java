package com.yxy.nova.service.encryption;

import com.yxy.nova.bean.BizException;
import com.yxy.nova.bean.EncryModeEnum;
import org.springframework.stereotype.Component;

/**
 * @author yuxiaoyu
 * @Description:
 * @date 2019-07-11 14:44
 */
@Component
public class EncryptFactory {

    public AbstractEncrypt createInstance(EncryModeEnum modeEnum){
        switch (modeEnum) {
            case MD5:
                return Md5Encrypt.getInstance();
            case SHA256:
                return SHA256Encrypt.getInstance();
            default:
                throw BizException.instance("没有相应的加密方式!");
        }
    }

}
