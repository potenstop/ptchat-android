package top.potens.ptchat.network;

import java.util.List;

import top.potens.ptchat.bean.MessageBean;

/**
 * Created by wenshao on 2018/7/22.
 * 数据交互接口
 */
public interface DataInteraction {

    // 初始数据
    public List<MessageBean> initData();
    // 发送数据
    public void sendData(MessageBean messageBean, SendCallback sendCallback);


}
