package server.service;

import common.Message;
import common.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    public String getUserID() {
        return userID;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        //用户一上线就发送离线消息
        ConcurrentHashMap<String, ArrayList<Message>> offLine = Server.getOffLine();
        ConcurrentHashMap.KeySetView<String, ArrayList<Message>> strings = offLine.keySet();
        for (String string : strings) {
            if (string.equals(userID)) {
                ArrayList<Message> messages = offLine.get(string);
                for (Message message : messages) {
                    try {
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        objectOutputStream.writeObject(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        while (loop) {
            System.out.println("服务器和客户端 [" + userID + "] 保持通信，读取数据...");
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                //阻塞
                Message message = (Message) objectInputStream.readObject();
                //发送在线用户列表
                if (message.getMesType() == MessageType.MESSAGE_GET_ONLINE_FRIEND) {
                    System.out.println("[" + message.getSender() + "] 获取了在线用户列表");
                    Message messageR = new Message();
                    messageR.setContent(ManageServerConnectClientThread.getOnlineUser());
                    messageR.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    messageR.setReceiver(message.getSender());
                    messageR.setSender(message.getReceiver());
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(messageR);
                } else if (message.getMesType() == MessageType.MESSAGE_CLIENT_EXIT) {
                    System.out.println("[" + message.getSender() + "] 客户端退出");
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(ManageServerConnectClientThread.getServerClientThread(message.getSender()).socket.getOutputStream());
                    objectOutputStream.writeObject(new Message("server", message.getSender(), "exit", MessageType.MESSAGE_CLIENT_EXIT));
                    //从集合中移除该线程
                    ManageServerConnectClientThread.removeServerClientThread(userID);
                    //关闭连接
                    socket.close();
                    //退出线程
                    break;
                }
                //收到普通消息MESSAGE_COMM_MES
                else if (message.getMesType() == MessageType.MESSAGE_COMM_MES) {
                    //群发
                    if (message.getReceiver().equals("all")) {
                        System.out.println("[" + message.getSender() + "] 群发了一条消息");
                        HashMap<String, ServerConnectClientThread> allThread = ManageServerConnectClientThread.getAllThread();
                        for (Map.Entry<String, ServerConnectClientThread> next : allThread.entrySet()) {
                            if (!next.getKey().equals(message.getSender())) {
                                ObjectOutputStream objectOutputStream = new ObjectOutputStream(next.getValue().socket.getOutputStream());
                                objectOutputStream.writeObject(new Message(message.getSender() + "(群发)", next.getKey(), message.getContent(), MessageType.MESSAGE_COMM_MES));
                            }
                        }
                        Message messageR = new Message("server", message.getSender(), "群发成功", MessageType.MESSAGE_COMM_MES);
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(ManageServerConnectClientThread.getServerClientThread(message.getSender()).socket.getOutputStream());
                        objectOutputStream.writeObject(messageR);
                    }
                    //没有在线的用户，离线发消息
                    else if (ManageServerConnectClientThread.getServerClientThread(message.getReceiver()) == null && Server.getValidUsers().get(message.getReceiver()) != null) {
                        System.out.println("[" + message.getSender() + "] 给 [" + message.getReceiver() + "] 发送了一条离线消息");
                        //将消息保存到数据库
                        Server.addOfflineMessage(message.getReceiver(), message);

                        Message messageR = new Message("server", message.getSender(), "该用户不在线，已发送离线消息", MessageType.MESSAGE_COMM_MES);
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        objectOutputStream.writeObject(messageR);
                    }
                    //没有该用户
                    else if (ManageServerConnectClientThread.getServerClientThread(message.getReceiver()) == null) {
                        Message messageR = new Message("server", message.getSender(), "没有该用户!", MessageType.MESSAGE_COMM_MES);
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        objectOutputStream.writeObject(messageR);
                    }
                    //私聊
                    else {
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(ManageServerConnectClientThread.getServerClientThread(message.getReceiver()).socket.getOutputStream());
                        Message messageT = new Message(message.getSender(), message.getReceiver(), message.getContent(), MessageType.MESSAGE_COMM_MES);
                        objectOutputStream.writeObject(messageT);

                        System.out.println("[" + message.getSender() + "] 给 [" + message.getReceiver() + "] 发送了一条消息");
                        Message messageR = new Message("server", message.getSender(), "发送成功", MessageType.MESSAGE_COMM_MES);
                        ObjectOutputStream objectOutputStreamR = new ObjectOutputStream(socket.getOutputStream());
                        objectOutputStreamR.writeObject(messageR);
                    }
                }
                //文件
                else if (message.getMesType() == MessageType.MESSAGE_FILE) {
                    //没有在线用户，离线发文件
                    if (ManageServerConnectClientThread.getServerClientThread(message.getReceiver()) == null && Server.getValidUsers().get(message.getReceiver()) != null) {
                        System.out.println("[" + message.getSender() + "] 给 [" + message.getReceiver() + "] 发送了一个离线文件");
                        //将消息保存到数据库
                        Server.addOfflineMessage(message.getReceiver(), message);

                        Message messageR = new Message("server", message.getSender(), "该用户不在线，已发送离线文件", MessageType.MESSAGE_COMM_MES);
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        objectOutputStream.writeObject(messageR);
                    }
                    //没有该用户
                    else if (ManageServerConnectClientThread.getServerClientThread(message.getReceiver()) == null) {
                        Message messageR = new Message("server", message.getSender(), "没有该用户!", MessageType.MESSAGE_COMM_MES);
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        objectOutputStream.writeObject(messageR);
                    }
                    //私聊
                    else {
                        System.out.println("[" + message.getSender() + "] 给 [" + message.getReceiver() + "] 从 " + message.getSrc() + " 发送到 " + message.getDes());
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(ManageServerConnectClientThread.getServerClientThread(message.getReceiver()).socket.getOutputStream());
                        objectOutputStream.writeObject(message);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
