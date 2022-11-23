package client.clientservice;

import common.Message;
import common.MessageType;
import common.User;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author sowhile
 * @version 1.0
 * <p>
 * 完成用户登录验证和注册等
 * 2022/11/22 16:09
 */
public class UserClientService {

    private final User user = new User();
    private Socket socket;

    //与服务器通信。检查用户名和密码
    public boolean checkUser(String userID, String pwd) {
        boolean check = false;
        user.setUserID(userID);
        user.setPassword(pwd);
        Message message;
        try {
            //连接到服务端，发送user对象
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(user);

            //读取从服务端回送的message对象
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            message = (Message) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (message.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCESS)) {
            //创建一个和服务器端保持通讯的线程
            ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket);
            clientConnectServerThread.setName("clientConnectServerThread");
            clientConnectServerThread.start();
            ManageClientConnectServerThread.addClientServerThread(userID, clientConnectServerThread);
            check = true;
        } else {
            //登录失败，关闭socket
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return check;
    }

    //向服务器端请求在线用户列表
    public void getOnlineFriendList() {
        //发送一个MESSAGE_GET_ONLINE_FRIEND包
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(user.getUserID());
        message.setReceiver("server");
        try {
            //此处是否可以直接用socket? 答案是可以
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream
//                    (ManageClientConnectServerThread.getClientServerThread(user.getUserID()).getSocket().getOutputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //给服务端发送MESSAGE_CLIENT_EXIT
    public void logOut() {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(user.getUserID());
        message.setReceiver("server");

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(message);
            System.out.println("用户 [" + user.getUserID() + "] 已退出系统");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //私聊发送消息
    public void sendMessage(String receiver, String content) {
        Message message = new Message(user.getUserID(), receiver, content, MessageType.MESSAGE_COMM_MES);
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessageToAll(String contentAll) {
        Message message = new Message(user.getUserID(), "all", contentAll, MessageType.MESSAGE_COMM_MES);
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendFile(String src, String des, String sender, String receiver) {
        Message message = new Message(sender, receiver, "send a file", MessageType.MESSAGE_FILE);
        message.setSrc(src);
        message.setDes(des);
        //读取文件
        byte[] bytes = new byte[(int) new File(src).length()];
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(src);
            fileInputStream.read(bytes);
            message.setBytes(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println("[" + sender + "] 给 [" + receiver + "] 发送文件：" + src + " 到对方 " + des);
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
