package qqclient.service;

import com.king.qqcommon.Message;
import com.king.qqcommon.MessageType;

import java.io.FileOutputStream;
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
                }else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)){//发消息业务
                    System.out.println("\n你接收到消息："+message.getSender()+"对你说:"+message.getContent());
                }else if (message.getMesType().equals(MessageType.MESSAGE_TO_ALL_MES)){//接收群发消息
                    System.out.println("\n你接收到消息："+message.getSender()+"对所有在线用户说:"+message.getContent());
                }else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)){//接收发送的文件
                    FileOutputStream fileOutputStream = new FileOutputStream(message.getDest());
                    fileOutputStream.write(message.getFileBytes());
                    fileOutputStream.close();//关闭流，并刷新
                    System.out.println("\n你接收到文件："+message.getSender()+"给你发送文件:"
                            +message.getSrc()+", 储存到本地目录"+message.getDest());
                }
                else{
                    System.out.println("是其他类型信息，暂时不处理");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
