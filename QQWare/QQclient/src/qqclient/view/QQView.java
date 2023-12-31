package qqclient.view;

import qqclient.service.FileClientService;
import qqclient.service.MessageSendService;
import qqclient.service.UserClientService;
import qqclient.utils.Utility;


/**
 * @version 1.0
 * @Author King
 */
public class QQView {
    private boolean loop = true; //控制是否显示主菜单
    private String key = ""; //接收用户的键盘输入
    private UserClientService userClientService = new UserClientService();//用于登录服务器和注册用户
    private MessageSendService messageSendService = new MessageSendService();//用于发送消息服务
    private FileClientService fileClientService = new FileClientService();//用于文件发送

    public static void main(String[] args) {
        new QQView().mainMenu();
        System.out.println("客户端退出系统~");
    }

    //显示主菜单
    private void mainMenu(){
        while (loop){
            System.out.println("=============欢迎登录网络通信系统==============");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 9 退出系统");
            System.out.print("请输入你的选择：");
            key = Utility.readString();
            //根据用户的输入处理不同的逻辑
            switch (key){
                case "1":
                    System.out.print("请输入用户名：");
                    String userId = Utility.readString();
                    System.out.print("请输入密  码：");
                    String pwd = Utility.readString();
                    //后续需要构建user，到服务端看是否合法编写一个UserClientService类进行用户的登陆和注册
                    if (userClientService.checkUser(userId,pwd)){ //验证通过
                        System.out.println("=============用户" + userId + "登录成功" + "==============");
                        //进入二级菜单
                        while (loop){
                            System.out.println("====="+"网络通信系统（用户：" + userId + "）登录成功" + "=====");
                            System.out.println("\t\t 1 在线用户列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 退出系统");
                            System.out.print("请选择功能：");
                            key = Utility.readString();
                            switch (key){
                                case "1":
                                    userClientService.getOnlineFriendList();
                                    break;
                                case "2":
                                    System.out.print("请输入想对大家说的话：");
                                    String content1 = Utility.readString();
                                    messageSendService.sendMessageToAll(userId,content1);
                                    break;
                                case "3":
                                    System.out.print("请输入想聊天的在线用户：");
                                    String getter = Utility.readString();
                                    System.out.print("请输入想说的话：");
                                    String content = Utility.readString();
                                    messageSendService.sendMessageToOne(userId,content,getter);
                                    break;
                                case "4":
                                    System.out.print("请输入想发送文件的在线用户：");
                                    String getterId = Utility.readString();
                                    fileClientService.sendFileToOne("D:\\test\\123.png","D:\\test\\456.png",userId,getterId);
                                    break;
                                case "9":
                                    userClientService.exitClient();//关闭客户端线程并向服务端发送关闭线程的消息
                                    loop = false;//关闭主界面的菜单，结束main线程
                                    break;
                            }
                        }
                    }else{ //验证失败
                        System.out.println("=============用户" + userId + "登录失败" + "==============");
                    }
                    break;
                case "9":
                    loop = false;
                    break;
            }

        }
    }
}
