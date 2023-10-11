package qqclient.service;

import com.king.qqcommon.User;

/**
 * @author King
 * @version 1.0
 * 该类完成用户登陆验证和注册功能
 */
public class UserClientService {
    private User user = new User(); //把user做成属性，在别的地方有可能要使用user属性
    //根据userId和pwd到服务器去验证该用户是否合法
//    public boolean checkUser(String userId,String pwd){
//        user.setUserId(userId);
//        user.setPasswd(pwd);
//
//        //连接到服务器，发送对象
//    }
}
