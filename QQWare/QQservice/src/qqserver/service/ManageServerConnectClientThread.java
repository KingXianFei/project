package qqserver.service;

import java.util.HashMap;

/**
 * @version 1.0
 * @Author King
 */
public class ManageServerConnectClientThread {
    private static HashMap<String,ServerConnectClientThread> hm = new HashMap<>();

    /**
     * @param userId
     * @param serverConnectClientThread
     * 将和客户端连接的线程放入hashmap中进行管理
     */
    public static void addServerConnectClientThread(String userId,ServerConnectClientThread serverConnectClientThread){
        hm.put(userId,serverConnectClientThread);
    }
    /**
     * 根据用户userId查找服务端和其连接的线程
     */
    public static ServerConnectClientThread getServerConnectClientThread(String userId){
        return hm.get(userId);
    }
    /**
     * 编写返回在线用户列表方法
     */
    public static String getOnlineUser(){
        String st = "";
        for(String userId : hm.keySet()){
            st += userId+" ";
        }
        return st;
    }
    /**
     * 将线程移除集合
     */
    public static void deleteServerConnectClientThread(String userId){
         hm.remove(userId);
    }

    /**
     * 判断某个用户是否在线
     * @return
     */
    public static boolean checkUserOnline(String userId){
        for (String userName : hm.keySet()) {
            if (userId.equals(userName)){
                return true;
            }
        }
        return false;
    }

    public static HashMap<String, ServerConnectClientThread> getHm() {
        return hm;
    }

    public static void setHm(HashMap<String, ServerConnectClientThread> hm) {
        ManageServerConnectClientThread.hm = hm;
    }
}
