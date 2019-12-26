package com.yxy.nova.service.base;

import com.yxy.nova.bean.UserStatusEnum;
import com.yxy.nova.dal.mysql.dataobject.UserDO;
import com.yxy.nova.dal.mysql.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @description:
 * @author: shui.ren
 * @date: 2018-04-24 下午7:47
 */
@Service
public class UserBaseServiceImpl implements UserBaseService{

    @Autowired
    private UserMapper userMapper;


    @Override
    public UserDO findUserByLoginId(String loginId) {
        Example example = new Example(UserDO.class);
        example.createCriteria().andEqualTo("loginId", loginId)
                .andNotEqualTo("status", UserStatusEnum.DELETE);
        return userMapper.selectOneByExample(example);
    }

    @Override
    public UserDO findUserByUserUuid(String userUuid) {
        Example example = new Example(UserDO.class);
        example.createCriteria().andEqualTo("userUuid", userUuid)
                .andNotEqualTo("status",UserStatusEnum.DELETE);
        return userMapper.selectOneByExample(example);
    }

    @Override
    public boolean updatePassword(String userUuid, String password) {
        Example example = new Example(UserDO.class);
        example.createCriteria().andEqualTo("userUuid", userUuid);
        UserDO userDO = new UserDO();
        userDO.setPassword(password);
        return userMapper.updateByExampleSelective(userDO, example) > 0;
    }

    @Override
    public UserDO findUserByUserUuidAndStatus(String userUuid, UserStatusEnum status) {
        Example example = new Example(UserDO.class);
        example.createCriteria().andEqualTo("userUuid", userUuid)
                .andEqualTo("status", status);
        return userMapper.selectOneByExample(example);
    }

    @Override
    public UserDO findUserByLoginIdAndStatus(String loginId, UserStatusEnum status) {
        Example example = new Example(UserDO.class);
        example.createCriteria().andEqualTo("loginId", loginId)
                .andEqualTo("status", status);
        return userMapper.selectOneByExample(example);
    }

    @Override
    public List<UserDO> findUserByRealName(String realName) {
        Example example = new Example(UserDO.class);
        example.createCriteria().andEqualTo("realName", realName)
                .andNotEqualTo("status",UserStatusEnum.DELETE);
        return userMapper.selectByExample(example);
    }


}
