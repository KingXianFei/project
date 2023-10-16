package qqserver.service;

import com.king.qqcommon.Message;
import com.king.qqcommon.MessageType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @version 1.0
 * @Author King
 * 该类对应对象和客户端保持通信
 */
public class ServerConnectClientThread extends Thread{
    private Socket socket;
    private String userId;//服务端连接的用户id
    private boolean exit = false;//控制线程的结束

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
        System.out.println("服务端线程，等待从客户端("+userId+")处接收消息...");
        while (!exit){//会一直循环和客户端保持通信,可以接收和发送消息
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Message message = (Message)objectInputStream.readObject();
                //根据message的类型，做相应的业务处理
                if (message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)){
                    //客户端需要在线用户列表
                    System.out.println(message.getSender() + "拉取在线用户列表");
                    String onlineUserList = ManageServerConnectClientThread.getOnlineUser();
                    //构建发送给用户的信息
                    Message message2 = new Message();
                    message2.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message2.setContent(onlineUserList);
                    message2.setGetter(message.getSender());
                    //将信息发送给用户
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message2);
                }else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)){
                    //私发消息
                    //获取私发对象的输出流,直接把接收到的message发送过去
                    ObjectOutputStream oos = new ObjectOutputStream(ManageServerConnectClientThread.
                            getServerConnectClientThread(message.getGetter()).getSocket().getOutputStream());
                    oos.writeObject(message);//如果客户不在线，可以储存到数据库，实现离线留言
                    System.out.println(message.getSender()+"对"+message.getGetter()+"说："+message.getContent());
                } else if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {
                    //客户端退出
                    exit = true;//关闭此线程
                    socket.close();//关闭socket
                    //将此线程从集合中移除
                    ManageServerConnectClientThread.deleteServerConnectClientThread(message.getSender());
                    System.out.println(message.getSender()+"退出连接~");
                }else if (message.getMesType().equals(MessageType.MESSAGE_TO_ALL_MES)){
                    //群发消息
                    for (String userName : ManageServerConnectClientThread.getHm().keySet()) {
                        if (!(userName.equals(message.getSender()))){
                            ObjectOutputStream oos = new ObjectOutputStream(ManageServerConnectClientThread.
                                    getServerConnectClientThread(userName).getSocket().getOutputStream());
                            oos.writeObject(message);
                        }
                    }
                    System.out.println(message.getSender()+"对所有在线用户说："+message.getContent());
                }else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)){
                    //发送文件
                    //获取文件发送对象的输出流,直接把接收到的message发送过去
                    ObjectOutputStream oos = new ObjectOutputStream(ManageServerConnectClientThread.
                            getServerConnectClientThread(message.getGetter()).getSocket().getOutputStream());
                    oos.writeObject(message);//如果客户不在线，可以储存到数据库，实现离线留言
                    System.out.println(message.getSender()+"给"+message.getGetter()+"发送了文件："+message.getSrc());
                }
                else{
                    System.out.println("其他类型的业务暂时不处理");
                }
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

    public boolean isExit() {
        return exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }
}
