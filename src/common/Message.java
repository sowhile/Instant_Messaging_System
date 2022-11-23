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
    private MessageType mesType;

    //要发送文件，对Message进行扩展
    private byte[] bytes;
    private int fileLen = 0;
    //源路径
    private String src;
    //目的路径
    private String des;

    public Message() {
    }

    public Message(String sender, String receiver, String content, MessageType mesType) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.mesType = mesType;
    }

    public Message(String sender, String receiver, String content, String sendTime, MessageType mesType) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.sendTime = sendTime;
        this.mesType = mesType;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public int getFileLen() {
        return fileLen;
    }

    public void setFileLen(int fileLen) {
        this.fileLen = fileLen;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
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

    public MessageType getMesType() {
        return mesType;
    }

    public void setMesType(MessageType mesType) {
        this.mesType = mesType;
    }
}
