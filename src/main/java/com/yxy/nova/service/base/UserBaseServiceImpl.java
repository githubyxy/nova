package com.yxy.nova.service.base;

import com.yxy.nova.dal.mysql.dataobject.UserDO;
import com.yxy.nova.dal.mysql.mapper.UserMapper;
import com.yxy.nova.util.Md5Encrypt;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @author yuxiaoyu
 * @Description:
 * @date 2019-10-28 14:06
 */
@Service
public class UserBaseServiceImpl implements UserBaseService {
    @Resource
    private UserMapper userMapper;

    @Override
    public UserDO queryUserByUserName(String userName) {
        Example example = new Example(UserDO.class);
        example.createCriteria().andEqualTo("name", userName);
        return userMapper.selectOneByExample(example);
    }

    @Override
    public UserDO queryUserByUserNameAndPwd(String userName, String password) {
        String pwd = Md5Encrypt.encrypt(userName, password);

        Example example = new Example(UserDO.class);
        example.createCriteria().andEqualTo("name", userName).andEqualTo("password", pwd);
        return userMapper.selectOneByExample(example);
    }
}
