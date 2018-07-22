package top.potens.ptchat_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import top.potens.ptchat.Ptchat;
import top.potens.ptchat.bean.MessageBean;
import top.potens.ptchat.bean.UserBean;
import top.potens.ptchat.network.DataInteraction;
import top.potens.ptchat.network.SendCallback;

public class MainActivity extends AppCompatActivity {

    @OnClick(R.id.test) void submit() {
        final UserBean userBean = new UserBean(1, "http://img.zcool.cn/community/01d881579dc3620000018c1b430c4b.JPG@3000w_1l_2o_100sh.jpg", "abc");
        Ptchat ptchat = Ptchat.from(this)
                .userInfo(userBean)
                .dataStrategy(new DataInteraction() {
                    @Override
                    public List<MessageBean> initData() {
                        ArrayList<MessageBean> messageBeans = new ArrayList<>();
                        MessageBean messageBean = new MessageBean();
                        messageBean.setLocation(MessageBean.LOCATION_RIGHT);
                        messageBean.setType(MessageBean.TYPE_TEXT);
                        messageBean.setContent("111111");
                        messageBean.setUserBean(userBean);
                        messageBean.setSendId("111");
                        messageBean.setReceiveId("111");
                        messageBean.setCreateTime(new Date().getTime());

                        messageBeans.add(messageBean);
                        return  messageBeans;
                    }

                    @Override
                    public void sendData(MessageBean messageBean, SendCallback sendCallback) {
                        sendCallback.success();
                    }
                })
                .forResult(1);


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }
}
