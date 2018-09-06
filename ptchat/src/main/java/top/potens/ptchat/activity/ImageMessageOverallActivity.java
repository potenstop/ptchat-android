package top.potens.ptchat.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.view.MotionEvent;
import android.view.View;
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
public class ImageMessageOverallActivity extends Activity implements View.OnClickListener {
    private static final Logger logger = LoggerFactory.getLogger(ImageMessageOverallActivity.class);
    private Context mContext;
    private String imagePath;
    private ImageView message_overall_image;
    private ImageButton message_overall_more;
    private BottomSheetDialog bottomSheetDialog;

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

        View bottom_dialog_image_overall = View.inflate(this, R.layout.bottom_dialog_image_overall, null);
        message_overall_image = findViewById(R.id.message_overall_image);
        message_overall_more = findViewById(R.id.message_overall_more);

        GlobalStaticVariable.getPtchat().getChatImageEngine().loadCommonImage(mContext, imagePath, message_overall_image);
        bottomSheetDialog = new BottomSheetDialog(mContext);

        bottomSheetDialog.setContentView(bottom_dialog_image_overall);

        message_overall_more.setOnClickListener(this);
    }
    private void showButton() {
        bottomSheetDialog.show();
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




    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.message_overall_more) {
            showButton();
        }
    }
}
