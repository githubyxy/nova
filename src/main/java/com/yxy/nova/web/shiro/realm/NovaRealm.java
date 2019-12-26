package com.yxy.nova.web.shiro.realm;


import com.yxy.nova.dal.mysql.dataobject.UserDO;
import com.yxy.nova.service.base.UserBaseService;
import com.yxy.nova.web.shiro.sesssion.NovaPrincipal;
import com.yxy.nova.web.util.SecurityUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 认证
 *
 * @author 于晓宇
 * @date 2019-10-28 10:09
 */
@Component
public class NovaRealm extends AuthorizingRealm {

    @Autowired
    private UserBaseService userBaseService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }


    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        String loginId = (String) principals.getPrimaryPrincipal();

        NovaPrincipal principal = SecurityUtil.getPrincipalByloginId(loginId);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        info.setStringPermissions(principal.getPrivilegeCodeSet());
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authcToken) throws AuthenticationException {

        UsernamePasswordToken token = (UsernamePasswordToken)authcToken;

        String loginId = token.getUsername();

        UserDO userDO = userBaseService.findUserByLoginId(loginId);

        if (userDO == null) {
            throw new UnknownAccountException();
        }

        return new SimpleAuthenticationInfo(loginId, userDO.getPassword(),
                ByteSource.Util.bytes(userDO.getSalt()), getName());
    }


    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        hashedCredentialsMatcher.setHashIterations(2);
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        super.setCredentialsMatcher(hashedCredentialsMatcher);
    }
}
