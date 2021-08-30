package com.msj.config;

import com.msj.pojo.User;
import com.msj.service.UserServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

//自定义的UserRealm
public class UserRealm extends AuthorizingRealm{
    @Autowired
    UserServiceImpl userService;
    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了=>授权doGetAuthorizationInfo");
        //授权需要使用SimpleAuthorizationInfo类
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //添加授权 及只要经过该方法就会添加user:add权限（现在无论什么用户都会添加此权限）
        //info.addStringPermission("user:add");
        //拿到当前登录的这个对象
        Subject subject = SecurityUtils.getSubject();
        User currentUser = (User)subject.getPrincipal(); //拿到user对象
        //设置权限
        info.addStringPermission(currentUser.getPerms());
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行了=>认证doGetAuthenticationInfo");

        //用户名 数据库中取 这里测试就是用模拟数据
        /*String name = "root";
        String password = "123456";*/
        //把AuthenticationToken authenticationToken转换成UsernamePasswordToken
        UsernamePasswordToken userToken = (UsernamePasswordToken)authenticationToken;
        //连接真实数据库
        User user = userService.queryUserByName(userToken.getUsername());
        if(user==null){
            //没有这个人
            return null;
        }
        //登录成功，设置session，以便让前端控制显示登录按钮
        Subject currentSubject = SecurityUtils.getSubject();
        //获得shiro的session
        Session session = currentSubject.getSession();
        //设置session
        session.setAttribute("loginUser",user);

        //System.out.println(userToken);
        //如果密码不一致，则return null;
        //if(!userToken.getUsername().equals(name)){
            //return null后就会抛出UnknownAccountException异常（用户名错误）
            //return null;
        //}

        //密码认证：shiro自己做（不用我们做，防止密码泄露）
        /*返回AuthenticationInfo，AuthenticationInfo是一个接口,他有两个实现类，我们返回SimpleAuthenticationInfo实现类即可
        * SimpleAuthenticationInfo实现类需要三个参数:
        * 参数一：Object principal:获取当前用户的认证
        * 参数二：Object credentials：要传递的密码对象
        * 参数三：Object realmName：认证名
        * 参数一和参数三我们都可以省去，只传递参数二，使用password即可，shiro会自动去帮我们匹配
        *
        * 加密：md5,md5盐值加密等*/
        return new SimpleAuthenticationInfo(user,user.getPwd(),"");
        //return new SimpleAccount();
    }
}
