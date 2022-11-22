package server.service;

import common.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @author sowhile
 * @version 1.0
 * <p>
 * 该类的一个对象和某个客户端保持通信
 * 2022/11/22 17:37
 */
public class ServerConnectClientThread extends Thread {

    private String userID;
    private Socket socket;
    private boolean loop = true;

    public ServerConnectClientThread(String userID, Socket socket) {
        this.userID = userID;
        this.socket = socket;
    }

    @Override
    public void run() {
        while (loop) {
            System.out.println("服务器和客户端 " + userID + " 保持通信，读取数据...");
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                //阻塞
                Message message = (Message) objectInputStream.readObject();
                //
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
