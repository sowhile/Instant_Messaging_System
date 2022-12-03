package server.service;

import common.Message;
import common.MessageType;
import common.User;
import server.dataservice.UserService;
import server.utils.BCrypt;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
    private static UserService userService = new UserService();

    /**
     * 创建一个集合，存放多个用户
     * HashMap是线程不安全的
     */

    //离线发消息和文件的缓存
    private static final ConcurrentHashMap<String, ArrayList<Message>> offLine = new ConcurrentHashMap<>();

    //    public static ConcurrentHashMap<String, User> getValidUsers() {
//        return validUsers;
//    }
    public static boolean isValidUsers(String userName) {
        return userService.getUser(userName) == null;
    }

    public static void addOfflineMessage(String receiver, Message message) {
        if (offLine.get(receiver) == null) {
            ArrayList<Message> messages = new ArrayList<>();
            messages.add(message);
            offLine.put(receiver, messages);
        } else {
            ArrayList<Message> messages = offLine.get(receiver);
            messages.add(message);
        }
    }

    public static ConcurrentHashMap<String, ArrayList<Message>> getOffLine() {
        return offLine;
    }

    public Server() {
        System.out.println("服务端在9999端口监听...");
        //启动消息推送服务
        new Thread(new SendNewsToAllThread()).start();
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
                //如果用户已登录
                if (ManageServerConnectClientThread.getServerClientThread(user.getUserID()) != null) {
                    System.out.println("用户: [" + user.getUserID() + "] 重复登录");
                    message.setMesType(MessageType.MESSAGE_COMM_MES);
                    message.setContent("该用户已登录，登录失败");
                    message.setSender("server");
                    message.setReceiver(user.getUserID());
                    objectOutputStream.writeObject(message);
                    //关闭socket
                    socket.close();
                } else if (checkUser(user.getUserID(), user.getPassword())) {
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCESS);
                    message.setSender("server");
                    message.setReceiver(user.getUserID());
                    objectOutputStream.writeObject(message);
                    //创建一个线程，和客户端保持通讯
                    ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(user.getUserID(), socket);
                    serverConnectClientThread.setName("serverConnectClientThread");
                    serverConnectClientThread.start();
                    //将线程放入集合中
                    ManageServerConnectClientThread.addServerClientThread(user.getUserID(), serverConnectClientThread);
                } else {
                    //登录失败
                    System.out.println("用户: [" + user.getUserID() + "]  密码: [" + user.getPassword() + "] 验证失败");
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
        String password = userService.getPassword(userID);
        if (password == null)
            return false;
        //密码是否正确
        return BCrypt.checkpw(pwd, userService.getPassword(userID));
    }
}
