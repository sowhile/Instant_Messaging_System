package server.service;

import common.Message;
import common.MessageType;
import common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sowhile
 * @version 1.0
 * <p>
 * 监听9999，等待客户端连接并保持通讯
 * 2022/11/22 17:16
 */
public class Server {
    public static void main(String[] args) {
        new Server();
    }

    private ServerSocket serverSocket;

    /**
     * 创建一个集合，存放多个用户
     * HashMap是线程不安全的
     */
    private static final ConcurrentHashMap<String, User> validUsers = new ConcurrentHashMap<>();

    static {
        validUsers.put("wangda", new User("wangda", "wangda"));
        validUsers.put("100", new User("100", "123456"));
        validUsers.put("1", new User("1", "1"));
    }

    public Server() {
        System.out.println("服务端在9999端口监听...");
        try {
            this.serverSocket = new ServerSocket(9999);
            //一直监听
            while (true) {
                //阻塞
                Socket socket = serverSocket.accept();
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                User user = (User) objectInputStream.readObject();
                //验证
                Message message = new Message();
                if (checkUser(user.getUserID(), user.getPassword())) {
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCESS);
                    objectOutputStream.writeObject(message);
                    //创建一个线程，和客户端保持通讯
                    ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(user.getUserID(), socket);
                    serverConnectClientThread.setName("serverConnectClientThread");
                    serverConnectClientThread.start();

                    //将线程放入集合中
                    ManageServerConnectClientThread.addServerClientThread(user.getUserID(), serverConnectClientThread);
                } else {
                    //登录失败
                    System.out.println("用户:" + user.getUserID() + "  密码:" + user.getPassword() + "  验证失败");
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    objectOutputStream.writeObject(message);
                    //关闭socket
                    socket.close();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean checkUser(String userID, String pwd) {
        User user = validUsers.get(userID);
        //用户名是否存在
        if (user == null) return false;
        //密码是否正确
        return user.getPassword().equals(pwd);
    }
}
