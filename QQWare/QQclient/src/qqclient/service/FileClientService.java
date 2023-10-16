package qqclient.service;

import com.king.qqcommon.Message;
import com.king.qqcommon.MessageType;

import java.io.*;

/**
 * @author King
 * @version 1.0
 * 该类完成文件的传输
 */
public class FileClientService {
    /**
     *
     * @param src 源文件
     * @param dest 把文件传递到对方哪个目录
     * @param sender 发送者
     * @param getter 文件接收者
     */
    public void sendFileToOne(String src,String dest,String sender,String getter){
        //读取src文件，将其封装到message文件中
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_FILE_MES);
        message.setSender(sender);
        message.setGetter(getter);
        message.setSrc(src);
        message.setDest(dest);
        //需要将文件进行读取
        FileInputStream fileInputStream = null;
        byte[] fileBytes = new byte[(int)new File(src).length()];
        try {
            fileInputStream = new FileInputStream(src);
            fileInputStream.read(fileBytes);//将本地文件读取到fileBytes
            message.setFileBytes(fileBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //将message发送到服务端
        try {
            ObjectOutputStream oos = new ObjectOutputStream(MangeClientConnectServerThread.
                    getClientConnectServerThread(sender).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\n你"+"给"+getter+"发送"+src+"到对方目录："+dest);
    }
}
