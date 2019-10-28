package com.yxy.nova.service;

import com.yxy.nova.bean.UserLoginDTO;
import com.yxy.nova.dal.mysql.dataobject.UserDO;

/**
 * @author yuxiaoyu
 * @Description:
 * @date 2019-10-28 14:00
 */
public interface UserService {
    UserDO login(UserLoginDTO userLoginDTO);
}
