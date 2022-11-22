package client.clientservice;

import java.util.HashMap;

/**
 * @author sowhile
 * @version 1.0
 * <p>
 * 2022/11/22 17:00
 */
public class ManageClientConnectServerThread {
    //key是userID，value是ClientConnectServerThread线程
    private static HashMap<String, ClientConnectServerThread> hashMap = new HashMap<>();

    public static void addClientServerThread(String userID, ClientConnectServerThread clientConnectServerThread) {
        hashMap.put(userID, clientConnectServerThread);
    }

    public static ClientConnectServerThread getClientServerThread(String userID) {
        return hashMap.get(userID);
    }

    public static void removeClientServerThread(String userID) {
        hashMap.remove(userID);
    }
}
