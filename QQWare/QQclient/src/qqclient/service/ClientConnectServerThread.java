package qqclient.service;

import com.king.qqcommon.Message;

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

    @Override
    public void run() {
        //线程需要一直通信，要做成while循环
        while (true){
            try {
                System.out.println("客户端线程，等待读取从服务器端发送消息");//如果服务器没有发消息，会一直阻塞在这里
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Message message = (Message)objectInputStream.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
