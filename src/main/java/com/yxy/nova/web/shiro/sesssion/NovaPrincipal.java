package com.yxy.nova.web.shiro.sesssion;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @description: 保存当前登录用户的会话信息
 * @author: yuxiaoyu
 * @date: 2019-04-24 下午3:40
 */
@Data
public class NovaPrincipal implements Serializable {

    private static final long serialVersionUID = 8176143135291216335L;
    /**
     * 当前用户登录使用的loginId
     */
    private String loginId;

    /**
     * 当前登录用户在user表中的uuid
     */
    private String userUuid;

    /**
     * 当前登录用户的真实姓名
     */
    private String realName;

    /**
     * 当前登录用户所属角色名称
     */
    private String roleName;

    /**
     * 当前登录用户拥有的权限列表
     */
    private Set<String> privilegeCodeSet;

    /**
     * 当前登录的会话id
     */
    private String sessionId;

}
