package client.view;

import client.clientservice.UserClientService;

import java.io.File;

/**
 * @author sowhile
 * @version 1.0
 * <p>
 * 2022/11/22 11:34
 */
public class ClientView {
    //主菜单循环
    private boolean loop = true;
    private final UserClientService userClientService = new UserClientService();

    public static void main(String[] args) {
        new ClientView().mainMenu();
    }

    public void mainMenu() {
        while (loop) {
            //loop2为二级菜单循环
            boolean loop2 = true;
            System.out.println("===========欢迎登录网络通信系统===========");
            System.out.println("\t\t\t\t1.登录");
            System.out.println("\t\t\t\t9.退出系统");

            System.out.print("请选择：");
            switch (Utility.readChar()) {
                case '1':
                    System.out.print("请输入用户名(q返回)：");
                    String userID = Utility.readString(64);
                    if (userID.toLowerCase().charAt(0) == 'q') break;
                    System.out.print("请输入密码(q返回)：");
                    String password = Utility.readString(64);
                    if (password.toLowerCase().charAt(0) == 'q') break;

                    //到服务端验证是否合法
                    if (userClientService.checkUser(userID, password)) {
                        //二级菜单
                        System.out.println("==============登录成功！==============");
                        while (loop2) {
                            System.out.println("===============欢迎, " + userID + "===============");
                            secondMenu();
                            switch (Utility.readChar()) {
                                case '1':
                                    System.out.println("==============当前在线用户列表==============");
                                    userClientService.getOnlineFriendList();
                                    Utility.readChar('r');
                                    break;
                                case '2':
                                    System.out.println("===============群发消息===============");
                                    System.out.println("请输入消息内容(q返回): ");
                                    String contentAll = Utility.readString(512);
                                    if (contentAll.toLowerCase().charAt(0) == 'q') break;
                                    userClientService.sendMessageToAll(contentAll);
                                    break;
                                case '3':
                                    System.out.println("===============私聊消息===============");
                                    userClientService.getOnlineFriendList();
                                    try {
                                        Thread.sleep(200);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                    System.out.print("请输入要聊天的对象(q返回): ");
                                    String receiver = Utility.readString(64);
                                    if (receiver.toLowerCase().charAt(0) == 'q') break;
                                    System.out.println("请输入消息内容(q返回): ");
                                    String content = Utility.readString(512);
                                    if (content.toLowerCase().charAt(0) == 'q') break;
                                    userClientService.sendMessage(receiver, content);
                                    try {
                                        Thread.sleep(200);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                    break;
                                case '4':
                                    System.out.println("===============发送文件===============");
                                    userClientService.getOnlineFriendList();
                                    try {
                                        Thread.sleep(200);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                    System.out.print("请输入要发送的对象(q返回): ");
                                    String fileReceiver = Utility.readString(64);
                                    if (fileReceiver.toLowerCase().charAt(0) == 'q') break;
                                    System.out.println("请输入文件路径[c:\\\\xxx\\\\xxx\\\\xx.txt](q返回): ");
                                    String filePath = Utility.readString(512);
                                    if (filePath.toLowerCase().charAt(0) == 'q') break;
                                    if (!new File(filePath).isFile()) {
                                        System.out.println("文件不存在！");
                                        break;
                                    }
                                    System.out.println("请输入对方文件路径[c:\\\\xxx\\\\xxx\\\\xx.txt](q返回): ");
                                    String filePathDes = Utility.readString(512);
                                    if (filePathDes.toLowerCase().charAt(0) == 'q') break;
                                    userClientService.sendFile(filePath, filePathDes, userID, fileReceiver);
                                    try {
                                        Thread.sleep(200);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                    break;
                                case '5':
                                case 'q':
                                case 'Q':
                                    userClientService.logOut();
                                    loop2 = false;
                                    break;
                                case '6':
                                    userClientService.logOut();
                                    System.exit(0);
                                    break;
                            }
                        }
                    } else System.out.println("用户名或密码错误！");

                    break;
                case '9':
                    loop = false;
                    break;
            }
        }
    }

    public static void secondMenu() {
        System.out.println("\t\t\t1.显示在线用户列表");
        System.out.println("\t\t\t2.群发消息");
        System.out.println("\t\t\t3.私聊消息");
        System.out.println("\t\t\t4.发送文件");
        System.out.println("\t\t\t5.退出用户");
        System.out.println("\t\t\t6.退出系统");
        System.out.print("请输入(q返回): ");
    }
}
