package top.potens.ptchat.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import top.potens.ptchat.R;

/**
 * Created by wenshao on 2018/7/16.
 */

public class IndexActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("222", "onCreate: ");
        setContentView(R.layout.activity_main1);

    }
}
