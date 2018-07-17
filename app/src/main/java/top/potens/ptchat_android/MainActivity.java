package top.potens.ptchat_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import top.potens.ptchat.activity.ChatWindowActivity;
import top.potens.ptchat.bean.UserBean;

public class MainActivity extends AppCompatActivity {

    @OnClick(R.id.test) void submit() {
        Intent intent = new Intent(this, ChatWindowActivity.class);
        UserBean userBean = new UserBean(1, "http://img.zcool.cn/community/01d881579dc3620000018c1b430c4b.JPG@3000w_1l_2o_100sh.jpg", "abc");
        Bundle bundle=new Bundle();
        bundle.putSerializable("userInfo", userBean);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }
}
