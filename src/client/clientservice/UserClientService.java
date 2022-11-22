package client.clientservice;

import common.Message;
import common.MessageType;
import common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    private User user = new User();
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
        //发送一个MESSAGE_GET_ONLINE_FRIEND
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream
                    (ManageClientConnectServerThread.getClientServerThread(user.getUserID()).getSocket().getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
