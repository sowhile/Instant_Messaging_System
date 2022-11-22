package server.service;

import common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author sowhile
 * @version 1.0
 * <p>
 * 监听9999，等待客户端连接并保持通讯
 * 2022/11/22 17:16
 */
public class Server {
    private ServerSocket serverSocket;

    public Server() {
        System.out.println("服务端在9999端口监听...");
        try {
            this.serverSocket = new ServerSocket(9999);
            //一直监听
            while (true) {
                Socket socket = serverSocket.accept();
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                User user = (User) objectInputStream.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
