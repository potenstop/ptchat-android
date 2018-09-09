package top.potens.ptchat.network;

import top.potens.ptchat.bean.MessageBean;

/**
 * Created by wenshao on 2018/7/22.
 * 发送数据回调
 */
public interface SendCallback {
    public void success(MessageBean message);
    public void error(MessageBean message);
}
