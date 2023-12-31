package qqserver.service;

import com.king.qqcommon.Message;
import com.king.qqcommon.MessageType;
import com.king.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * @version 1.0
 * @Author King
 * 这是服务器，在监听9999，等待客户端的连接，并保持通信
 */
public class QQServer {
    private ServerSocket serverSocket = null;
    //创建一个集合，储存合法用户信息进行认证
    private static HashMap<String,User> validUsers = new HashMap<>();
    static {//静态代码块，初始化validUsers
        validUsers.put("100",new User("100","123456"));
        validUsers.put("200",new User("200","123456"));
        validUsers.put("至尊宝",new User("至尊宝","123456"));
        validUsers.put("紫霞仙子",new User("紫霞仙子","123456"));
        validUsers.put("猪八戒",new User("猪八戒","123456"));
    }
    public QQServer() {
        try {
            System.out.println("服务端在9999端口监听...");
            new SendNewsToAllService().start();//启动服务端的推送服务线程
            serverSocket = new ServerSocket(9999);
            while (true){//当和某个客户连接后，会继续监听
                Socket socket = serverSocket.accept();
                //得到socket关联对象的输入流
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                User user = (User) objectInputStream.readObject();//客户端传过来的user对象
                //得到socket关联对象的输入流
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                Message message = new Message();
                if (checkUser(user)){//合法用户
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    objectOutputStream.writeObject(message);
                    //创建一个线程和客户端保持通信
                    ServerConnectClientThread serverConnectClientThread =
                            new ServerConnectClientThread(socket, user.getUserId());
                    serverConnectClientThread.start();//线程启动
                    //将线程添加进一个集合进行管理
                    ManageServerConnectClientThread.addServerConnectClientThread(user.getUserId(), serverConnectClientThread);
                }else{//非法用户
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    objectOutputStream.writeObject(message);
                    socket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {//如果退出while循环，说明服务退出，不在监听
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 验证客户端user是否在合法的集合中
     * @param user
     * @return
     */
    public boolean checkUser(User user){
        User validUser = validUsers.get(user.getUserId());
        if (validUser == null){//用户不存在
            return false;
        }
        if (!user.getPasswd().equals(validUser.getPasswd())){//密码错误
            return false;
        }
        if (!(ManageServerConnectClientThread.getServerConnectClientThread(user.getUserId()) == null)){
            return false;//验证单点登录，此用户已经和服务端进行了连接
        }
        return true;
    }
}
