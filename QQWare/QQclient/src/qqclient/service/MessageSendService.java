package qqclient.service;

import com.king.qqcommon.Message;
import com.king.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

/**
 * @author King
 * @version 1.0
 * 该类提供和消息相关的服务方法
 */
public class MessageSendService {
    /**
     * 私聊实现
     */
    public void sendMessageToOne(String userId,String content,String getter){
        //设置message
        Message message = new Message();
        message.setSender(userId);
        message.setContent(content);
        message.setGetter(getter);
        message.setMesType(MessageType.MESSAGE_COMM_MES);
        message.setSendTime(new Date().toString());
        System.out.println("你对"+getter+"说："+'"'+ content +'"');
        //发送给服务端
        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    MangeClientConnectServerThread.getClientConnectServerThread(userId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 群发消息实现
     */
    public void sendMessageToAll(String userId,String content){
        //设置message
        Message message = new Message();
        message.setSender(userId);
        message.setContent(content);
        message.setMesType(MessageType.MESSAGE_TO_ALL_MES);
        message.setSendTime(new Date().toString());
        System.out.println("你在群里说："+'"'+ content +'"');
        //发送给服务端
        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    MangeClientConnectServerThread.getClientConnectServerThread(userId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
