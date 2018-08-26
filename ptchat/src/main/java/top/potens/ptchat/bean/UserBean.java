package top.potens.ptchat.bean;

import java.io.Serializable;

/**
 * Created by wenshao on 2018/7/17.
 * 消息的用户对象
 */

public class UserBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String userHead;
    private String userName;

    public UserBean(String userId, String userHead, String userName) {
        this.userId = userId;
        this.userHead = userHead;
        this.userName = userName;

    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "userId=" + userId +
                ", userHead='" + userHead + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
