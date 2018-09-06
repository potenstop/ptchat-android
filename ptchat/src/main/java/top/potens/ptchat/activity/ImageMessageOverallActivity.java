package top.potens.ptchat.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import top.potens.ptchat.GlobalStaticVariable;
import top.potens.ptchat.R;
import top.potens.ptchat.util.ViewUtil;

/**
 * Created by wenshao on 2018/9/2.
 * 图片消息全屏页面
 */
public class ImageMessageOverallActivity extends Activity {
    private static final Logger logger = LoggerFactory.getLogger(ImageMessageOverallActivity.class);
    private Context mContext;
    private String imagePath;
    private ImageView message_overall_image;
    private ImageButton message_overall_more;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_image_message_overall);

        checkParams();
        initUi();
    }

    // 检查数据正确性
    private void checkParams() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) finish();
        assert extras != null;
        imagePath = extras.getString("imagePath");
        if (imagePath == null) finish();
    }

    // 初始化ui
    private void initUi() {
        message_overall_image = findViewById(R.id.message_overall_image);
        message_overall_more = findViewById(R.id.message_overall_more);

        GlobalStaticVariable.getPtchat().getChatImageEngine().loadCommonImage(mContext, imagePath, message_overall_image);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!ViewUtil.isTouchView(message_overall_more, ev)) finish();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
    void showBottomSheetDialog(){
        BottomSheetDialog bottomSheet = new BottomSheetDialog(this);//实例化
        BottomSheetDialog bottomSheet.setCancelable(true);//设置点击外部是否可以取消
        bottomSheet.setContentView(R.layout.dialog_layout);//设置对框框中的布局
        bottomSheet.show();//显示弹窗
    }

}
