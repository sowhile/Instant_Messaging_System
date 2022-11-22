package server.service;

import common.Message;
import common.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author sowhile
 * @version 1.0
 * <p>
 * 该类的一个对象和某个客户端保持通信
 * 2022/11/22 17:37
 */
public class ServerConnectClientThread extends Thread {

    private final String userID;
    private final Socket socket;
    private final boolean loop = true;

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
                //发送在线用户列表
                if (message.getMesType() == MessageType.MESSAGE_GET_ONLINE_FRIEND) {
                    System.out.println(message.getSender() + "获取了在线用户列表");
                    Message messageR = new Message();
                    messageR.setContent(ManageServerConnectClientThread.getOnlineUser());
                    messageR.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    messageR.setReceiver(message.getSender());
                    messageR.setSender(message.getReceiver());
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(messageR);
                }
                //其他类型暂时不处理
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
