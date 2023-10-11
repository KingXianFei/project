package qqclient.service;

import com.king.qqcommon.Message;
import com.king.qqcommon.MessageType;
import com.king.qqcommon.User;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author King
 * @version 1.0
 * 该类完成用户登陆验证和注册功能
 */
public class UserClientService {
    private User user = new User(); //把user做成属性，在别的地方有可能要使用user属性
    private Socket socket ;
    //根据userId和pwd到服务器去验证该用户是否合法
    public boolean checkUser(String userId,String pwd){
        boolean b = false;
        user.setUserId(userId);
        user.setPasswd(pwd);
        //连接到服务器，发送对象
        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"),9999);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(user);//发送user对象

            //读取从服务端回复的message对象
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Message message = (Message) objectInputStream.readObject();
            if (message.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)){//登录成功
                //启动线程让客户端和服务器端一直保持通信
                ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket);
                clientConnectServerThread.start();
                MangeClientConnectServerThread.addClientConnectServerThread(userId,clientConnectServerThread);
                b = true;
            }else{//登录失败

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }
}
