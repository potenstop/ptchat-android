package top.potens.ptchat.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import pub.devrel.easypermissions.EasyPermissions;
import top.potens.ptchat.GlobalStaticVariable;
import top.potens.ptchat.R;
import top.potens.ptchat.constant.PermissionConstant;
import top.potens.ptchat.util.FileManageUtil;
import top.potens.ptchat.util.ToastUtil;
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

        Button imo_save = bottom_dialog_image_overall.findViewById(R.id.imo_save);
        Button imo_send = bottom_dialog_image_overall.findViewById(R.id.imo_send);
        Button imo_cancel = bottom_dialog_image_overall.findViewById(R.id.imo_cancel);

        imo_save.setOnClickListener(this);
        imo_send.setOnClickListener(this);
        imo_cancel.setOnClickListener(this);

        message_overall_image.setOnClickListener(this);


        GlobalStaticVariable.getPtchat().getChatImageEngine().loadCommonImage(mContext, imagePath, message_overall_image);
        bottomSheetDialog = new BottomSheetDialog(mContext);

        bottomSheetDialog.setContentView(bottom_dialog_image_overall);

        message_overall_more.setOnClickListener(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        /*switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!ViewUtil.isTouchView(message_overall_more, ev)) finish();
                break;
        }*/
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.message_overall_more) {
            bottomSheetDialog.show();
        } else if (id == R.id.imo_cancel) {
            bottomSheetDialog.cancel();
        } else if (id == R.id.imo_send) {

        } else if (id == R.id.imo_save) {
            boolean b = EasyPermissions.hasPermissions(mContext, PermissionConstant.getExternalStoragePermissions(1).getPermissions());
            if (!b) ToastUtil.showShortToastTop(mContext, "没有权限");
            else {
                String saveFilePath = GlobalStaticVariable.getPtchat().getImageDir() + "/" + new File(imagePath).getName();
                FileManageUtil.copyFile(imagePath, saveFilePath);
                ToastUtil.showLongToastTop(mContext, getString(R.string.save_succeed) + saveFilePath);
                bottomSheetDialog.cancel();
            }
        } else if (id == R.id.message_overall_image) {
            finish();
        }
    }
}
