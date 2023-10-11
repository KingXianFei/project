package qqserver.service;

import com.king.qqcommon.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @version 1.0
 * @Author King
 * 该类对应对象和客户端保持通信
 */
public class ServerConnectClientThread extends Thread{
    private Socket socket;
    private String userId;//服务端连接的用户id

    /**
     * 创建线程的时候，需要传入的参数
     * @param socket
     * @param userId
     */
    public ServerConnectClientThread(Socket socket,String userId){
        this.socket = socket;
        this.userId = userId;
    }

    @Override
    public void run() {
        while (true){//会一直循环和客户端保持通信,可以接收和发送消息
            try {
                System.out.println("服务端线程，等待从客户端("+userId+")处接收消息...");
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Message message = (Message)objectInputStream.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
