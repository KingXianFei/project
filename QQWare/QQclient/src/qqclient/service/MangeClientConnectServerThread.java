package qqclient.service;

import java.util.HashMap;

/**
 * @author King
 * @version 1.0
 * 管理客户端连接到服务器端线程的类
 */
public class MangeClientConnectServerThread {
    //把多个线程放入到hashmap的集合中
    private static HashMap<String,ClientConnectServerThread> hm = new HashMap<>() ;
    //将客户端连接到服务器端线程放入集合中
    public static void addClientConnectServerThread(String userId,ClientConnectServerThread clientConnectServerThread){
        hm.put(userId,clientConnectServerThread);
    }
    //通过用户名查询此用户和客户端连接的所有线程
    public static ClientConnectServerThread getClientConnectServerThread(String userId){
        return hm.get(userId);
    }
}
