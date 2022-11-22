package server.service;

import java.util.HashMap;

/**
 * @author sowhile
 * @version 1.0
 * <p>
 * 用于管理服务器和客户通信的线程
 * 2022/11/22 21:07
 */
public class ManageServerConnectClientThread {
    private static HashMap<String, ServerConnectClientThread> hashMap = new HashMap<>();

    //添加线程对象到hashmap
    public static void addServerClientThread(String userID, ServerConnectClientThread serverConnectClientThread) {
        hashMap.put(userID, serverConnectClientThread);
    }

    //根据userID返回线程
    public static ServerConnectClientThread getServerClientThread(String userID) {
        return hashMap.get(userID);
    }
}
