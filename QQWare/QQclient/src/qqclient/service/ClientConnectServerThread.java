package qqclient.service;

import com.king.qqcommon.Message;
import com.king.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @author King
 * @version 1.0
 * 客户端和服务器端连接的线程
 */
public class ClientConnectServerThread extends Thread{
    private Socket socket;
    private boolean exit = false; //用于控制此线程的结束
    //该线程需要持有socket
    public ClientConnectServerThread(Socket socket){
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public boolean isExit() {
        return exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    @Override
    public void run() {
        //线程需要一直通信，要做成while循环
        while (!exit){
            try {
                System.out.println("客户端线程，等待读取从服务器端发送消息");//如果服务器没有发消息，会一直阻塞在这里
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Message message = (Message)objectInputStream.readObject();
                if (message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)){//返回的是在线用户列表信息
                    String[] onlineUsers = message.getContent().split(" ");//规定形式，采用空格进行字符串拆分
                    System.out.println("\n======当前在线用户列表======");
                    for (int i = 0; i < onlineUsers.length; i++) {
                        System.out.println("用户"+i+"："+ onlineUsers[i]);
                    }
                }else{
                    System.out.println("是其他类型信息，暂时不处理");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
