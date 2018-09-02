package top.potens.ptchat.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import top.potens.ptchat.R;

/**
 * Created by wenshao on 2018/9/2.
 * 图片消息全屏页面
 */
public class ImageMessageOverallActivity extends Activity {
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_image_message_overall);
    }


}
