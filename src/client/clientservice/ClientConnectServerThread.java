package client.clientservice;

import common.Message;
import common.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @author sowhile
 * @version 1.0
 * <p>
 * 该线程需要持有socket
 * 2022/11/22 16:22
 */
public class ClientConnectServerThread extends Thread {
    private Socket socket;
    private boolean loop = true;

    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    @Override
    public void run() {
        while (loop) {
            //System.out.println("客户端线程等待服务端发送消息...");
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                //如果服务器没有发送message对象，线程会阻塞在这里
                Message message = (Message) objectInputStream.readObject();
                if (message.getMesType() == MessageType.MESSAGE_RET_ONLINE_FRIEND) {
                    String[] onlineUsers = message.getContent().split(" ");
                    for (int i = 0; i < onlineUsers.length; i++)
                        System.out.println("\t\t\t\t用户" + (i + 1) + ": " + onlineUsers[i]);
                }
                //其他类型暂时不处理
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
