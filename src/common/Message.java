package common;

import java.io.Serializable;

/**
 * @author sowhile
 * @version 1.0
 * <p>
 * 表示客户端与服务器端通信时的一个消息对象
 * 2022/11/22 11:13
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 2L;
    private String sender;
    private String receiver;
    private String content;
    private String sendTime;
    private String mesType;

    public Message() {
    }

    public Message(String sender, String receiver, String content, String sendTime) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.sendTime = sendTime;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMesType() {
        return mesType;
    }

    public void setMesType(String mesType) {
        this.mesType = mesType;
    }
}
