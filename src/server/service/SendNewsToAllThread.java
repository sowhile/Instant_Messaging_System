package server.service;

import common.Message;
import common.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * @author sowhile
 * @version 1.0
 * <p>
 * 推送消息
 * 2022/11/23 17:11
 */
public class SendNewsToAllThread implements Runnable {
    private Scanner scanner = new Scanner(System.in);

    @Override
    public void run() {
        while (true) {
            System.out.println("请输入要推送的消息(q退出推送)：");
            String news = scanner.nextLine();
            if (news.equals("q")) break;
            System.out.println("服务器对所有人推送：" + news);
            Set<Map.Entry<String, ServerConnectClientThread>> entries = ManageServerConnectClientThread.getAllThread().entrySet();
            for (Map.Entry<String, ServerConnectClientThread> entry : entries) {
                Message message = new Message("server", entry.getKey(), news, MessageType.MESSAGE_COMM_MES);
                try {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(entry.getValue().getSocket().getOutputStream());
                    objectOutputStream.writeObject(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
