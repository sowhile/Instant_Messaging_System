package client.clientservice;

import client.view.ClientView;
import common.Message;
import common.MessageType;

import java.io.FileOutputStream;
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
    private final Socket socket;
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
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                //如果服务器没有发送message对象，线程会阻塞在这里
                Message message = (Message) objectInputStream.readObject();
                if (message.getMesType() == MessageType.MESSAGE_RET_ONLINE_FRIEND) {
                    String[] onlineUsers = message.getContent().split(" ");
                    for (int i = 0; i < onlineUsers.length; i++)
                        System.out.println("\t\t\t\t用户" + (i + 1) + ": " + onlineUsers[i]);
                }
                //如果是普通消息，直接展示
                else if (message.getMesType() == MessageType.MESSAGE_COMM_MES) {
                    System.out.println("\n===============消息窗口===============");
                    System.out.println("[" + message.getSender() + "] 对你说：" + message.getContent());
                    if (!(message.getContent().equals("发送成功") || message.getContent().equals("没有该用户!") || message.getContent().equals("该用户不在线，已发送离线消息") || message.getContent().equals("该用户不在线，已发送离线文件"))) {
                        System.out.println("===============欢迎, " + message.getReceiver() + "===============");
                        ClientView.secondMenu();
                    }
                }
                //接收文件
                else if (message.getMesType() == MessageType.MESSAGE_FILE) {
                    System.out.println("\n[" + message.getSender() + "] 发送 " + message.getSrc() + " 到本地 " + message.getDes());
                    FileOutputStream fileOutputStream = new FileOutputStream(message.getDes());
                    fileOutputStream.write(message.getBytes());
                    fileOutputStream.close();
                    System.out.println("文件保存成功！");
                    System.out.println("===============欢迎, " + message.getReceiver() + "===============");
                    ClientView.secondMenu();
                }
                //退出
                else if (message.getMesType() == MessageType.MESSAGE_CLIENT_EXIT) {
                    loop = false;
                    break;
                }
                //其他类型暂时不处理
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
