package common;

/**
 * @author sowhile
 * @version 1.0
 * <p>
 * 2022/11/22 11:24
 */
public enum MessageType {
    MESSAGE_LOGIN_SUCCESS("1"), MESSAGE_LOGIN_FAIL("2");
    final String type;

    MessageType(String type) {
        this.type = type;
    }
}
