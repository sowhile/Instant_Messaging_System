package common;

/**
 * @author sowhile
 * @version 1.0
 * <p>
 * 2022/11/22 11:24
 */
public enum MessageType {
    MESSAGE_LOGIN_SUCCESS("1"),
    MESSAGE_LOGIN_FAIL("2"),
    //普通信息包
    MESSAGE_COMM_MES("3"),
    //要求返回在线用户列表
    MESSAGE_GET_ONLINE_FRIEND("4"),
    //返回在线用户列表
    MESSAGE_RET_ONLINE_FRIEND("5"),
    //客户端请求退出
    MESSAGE_CLIENT_EXIT("6"),

    MESSAGE_FILE("7");


    final String type;

    MessageType(String type) {
        this.type = type;
    }

    public String getValue() {
        return type;
    }
}
