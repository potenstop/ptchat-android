package top.potens.ptchat_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


/**
 * Created by wenshao on 2018/8/24.
 */
public class SplashActivity extends Activity {
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = getApplicationContext();


        startActivity(new Intent(mContext, MainActivity.class));

    }
}
