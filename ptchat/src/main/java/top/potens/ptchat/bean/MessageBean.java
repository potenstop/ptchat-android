package top.potens.ptchat.bean;

import java.util.UUID;

import top.potens.ptchat.R;

/**
 * Created by wenshao on 2017/4/9.
 * 消息对象
 */

public class MessageBean implements Cloneable {
    public static final String TYPE_TEXT = "text";
    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_AUDIO = "audio";
    public static final int LOCATION_LEFT = 1;
    public static final int LOCATION_RIGHT = 2;
    public final static int STATUS_SEND_START = 1;
    public final static int STATUS_SEND_SUCCESS = 2;
    public final static int STATUS_SEND_FAIL = 3;
    public final static int STATUS_READ = 4;
    public final static int STATUS_UNREAD = 5;

    private String messageId;
    private String type;
    private String content;
    private long createTime;
    private int location;
    private UserBean userBean;
    private String sendCode;
    private int duration;  // 音频或视频的时长
    private String receiveId;
    private int status = STATUS_SEND_START;

    public MessageBean() {
        UUID uuid = UUID.randomUUID();
        this.sendCode = uuid.toString();
    }

    public MessageBean clone() {
        MessageBean o = null;
        try {
            // Object中的clone()识别出你要复制的是哪一个对象。
            o = (MessageBean) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println(e.toString());
        }
        return o;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public UserBean getFriendUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public String getSendCode() {
        return sendCode;
    }

    public void setSendCode(String sendCode) {
        this.sendCode = sendCode;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * 获取状态对应的描述资源id
     * @param status    状态
     * @return          资源id
     */
    public static int getStatusResourceId(int status) {
        switch (status) {
            case STATUS_SEND_START:
                return R.string.send_start;
            case STATUS_SEND_SUCCESS:
                return R.string.send_success;
            case STATUS_SEND_FAIL:
                return R.string.send_fail;
            case STATUS_READ:
                return R.string.read;
            case STATUS_UNREAD:
                return R.string.unread;
            default:
                return R.string.send_success;
        }

    }

}
