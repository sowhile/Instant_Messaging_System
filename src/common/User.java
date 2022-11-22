package common;

import java.io.Serializable;

/**
 * @author sowhile
 * @version 1.0
 * <p>
 * 表示一个用户信息
 * 2022/11/22 11:13
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userID;
    private String password;

    public User() {
    }

    public User(String userID, String password) {
        this.userID = userID;
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
