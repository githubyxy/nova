package com.yxy.nova.service.impl;

import com.yxy.nova.bean.BizException;
import com.yxy.nova.bean.UserLoginDTO;
import com.yxy.nova.dal.mysql.dataobject.UserDO;
import com.yxy.nova.service.UserService;
import com.yxy.nova.service.base.UserBaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yuxiaoyu
 * @Description:
 * @date 2019-10-28 14:00
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserBaseService userBaseService;

    @Override
    public UserDO login(UserLoginDTO userLoginDTO) {

        if (StringUtils.isBlank(userLoginDTO.getUsername()) || StringUtils.isBlank(userLoginDTO.getPassword())) {
            throw BizException.instance("用户名和密码不能为空");
        }

        UserDO userDO = userBaseService.queryUserByUserName(userLoginDTO.getUsername());
        if (userDO == null) {
            throw BizException.instance("不存在该用户");
        }
        UserDO userDO2 = userBaseService.queryUserByUserNameAndPwd(userLoginDTO.getUsername(), userLoginDTO.getPassword());
        if (null == userDO2) {
            throw BizException.instance("用户名或密码错误");
        }
        return userDO2;
    }
}
