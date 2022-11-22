package client.view;

import client.clientservice.UserClientService;

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
                            System.out.println("==============欢迎," + userID + "==============");
                            System.out.println("\t\t\t1.显示在线用户列表");
                            System.out.println("\t\t\t2.群发消息");
                            System.out.println("\t\t\t3.私聊消息");
                            System.out.println("\t\t\t4.发送文件");
                            System.out.println("\t\t\t5.退出用户");
                            System.out.println("\t\t\t6.退出系统");

                            System.out.print("请输入(q返回)：");
                            switch (Utility.readChar()) {
                                case '1':
                                    System.out.println("==============当前在线用户列表==============");
                                    userClientService.getOnlineFriendList();
                                    Utility.readChar('r');
                                    break;
                                case '2':
                                    System.out.println("群发消息");
                                    break;
                                case '3':
                                    System.out.println("私聊消息");
                                    break;
                                case '4':
                                    System.out.println("发送文件");
                                    break;
                                case '5':
//                                    ManageClientConnectServerThread.getClientServerThread(userID).setLoop(false);
//                                    try {
//                                        ManageClientConnectServerThread.getClientServerThread(userID).getSocket().close();
//                                    } catch (IOException e) {
//                                        throw new RuntimeException(e);
//                                    }
//                                    ManageClientConnectServerThread.removeClientServerThread(userID);
                                case 'q':
                                case 'Q':
                                    loop2 = false;
                                    break;
                                case '6':
                                    loop2 = false;
                                    loop = false;
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
}
