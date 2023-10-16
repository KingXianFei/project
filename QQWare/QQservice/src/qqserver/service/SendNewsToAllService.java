package qqserver.service;

import com.king.qqcommon.Message;
import com.king.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Scanner;

/**
 * @version 1.0
 * @Author King
 * 此线程用于服务端向客户端推送消息
 */
public class SendNewsToAllService extends Thread{
    private Scanner scanner = new Scanner(System.in);

    @Override
    public void run() {
        while (true){
            System.out.println("请输入向所有在线用户推送的消息：[输入exit退出推送服务]");
            String news = scanner.next();
            if (news.equals("exit")){
                return;
            }
            //发送消息，就是群发消息
            Message message = new Message();
            message.setMesType(MessageType.MESSAGE_TO_ALL_MES);
            message.setSender("服务器");
            message.setContent(news);
            message.setSendTime(new Date().toString());
            for (String userName : ManageServerConnectClientThread.getHm().keySet()) {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(ManageServerConnectClientThread.
                            getServerConnectClientThread(userName).getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("服务器向所有在线用户推送了消息："+message.getContent());
        }

    }
}
