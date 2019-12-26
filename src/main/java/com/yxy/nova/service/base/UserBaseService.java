package com.yxy.nova.service.base;


import com.yxy.nova.bean.UserStatusEnum;
import com.yxy.nova.dal.mysql.dataobject.UserDO;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @description: 基础的用户服务
 * @author: shui.ren
 * @date: 2018-04-24 下午7:44
 */

public interface UserBaseService {

    /**
     * 根据loginId查询用户(不包含已删除用户)
     * @param loginId
     * @return
     */
    UserDO findUserByLoginId(String loginId);

    /**
     * 根据userUuid查询用户
     * @param userUuid
     * @return
     */
    UserDO findUserByUserUuid(String userUuid);

    boolean updatePassword(String userUuid, String password);

    /**
     * 根据loginId和用户状态查询用户
     * @param userUuid
     * @param status
     * @return
     */
    UserDO findUserByUserUuidAndStatus(String userUuid, UserStatusEnum status);

    /**
     * 根据loginId和用户状态查询用户
     * @param loginId
     * @param status
     * @return
     */
    UserDO findUserByLoginIdAndStatus(String loginId, UserStatusEnum status);
    /**
     *真实姓名查询用户
     * @param realName
     * @return
     */
    List<UserDO> findUserByRealName(String realName);
}
