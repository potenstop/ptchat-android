package top.potens.ptchat_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import top.potens.ptchat.PtChat;
import top.potens.ptchat.activity.ChatWindowActivity;
import top.potens.ptchat.bean.UserBean;

public class MainActivity extends AppCompatActivity {

    @OnClick(R.id.test) void submit() {
        UserBean userBean = new UserBean(1, "http://img.zcool.cn/community/01d881579dc3620000018c1b430c4b.JPG@3000w_1l_2o_100sh.jpg", "abc");
        PtChat ptChat = PtChat.from(this)
                .userInfo(userBean)
                .forResult(1);


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }
}
