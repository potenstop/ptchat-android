package top.potens.ptchat_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import butterknife.ButterKnife;
import butterknife.OnClick;
import top.potens.ptchat.activity.IndexActivity;

public class MainActivity extends AppCompatActivity {

    @OnClick(R.id.test) void submit() {
        Log.d("111", "submit: ");
        startActivity(new Intent(this, IndexActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }
}
