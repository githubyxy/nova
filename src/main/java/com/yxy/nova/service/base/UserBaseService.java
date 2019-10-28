package com.yxy.nova.service.base;

import com.yxy.nova.dal.mysql.dataobject.UserDO;

/**
 * @author yuxiaoyu
 * @Description:
 * @date 2019-10-28 14:06
 */
public interface UserBaseService {
    UserDO queryUserByUserName(String userName);

    UserDO queryUserByUserNameAndPwd(String userName, String password);
}
