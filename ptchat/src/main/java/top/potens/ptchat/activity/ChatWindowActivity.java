package top.potens.ptchat.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import top.potens.ptchat.GlobalApplication;
import top.potens.ptchat.adapter.ChatMessageAdapter;
import top.potens.ptchat.adapter.FaceVPAdapter;
import top.potens.ptchat.adapter.SmallFaceAdapter;
import top.potens.ptchat.bean.MessageBean;
import top.potens.ptchat.bean.UserBean;
import top.potens.ptchat.constant.HandlerCode;
import top.potens.ptchat.engine.Glide4Engine;
import top.potens.ptchat.helper.FaceHelper;
import top.potens.ptchat.network.SendCallback;
import top.potens.ptchat.util.AudioRecordUtil;
import top.potens.ptchat.util.DisplayUtil;
import top.potens.ptchat.util.FileManageUtil;
import top.potens.ptchat.util.PermissionUtil;
import top.potens.ptchat.util.ToastUtil;
import top.potens.ptchat.view.CirclePlayProgress;
import top.potens.ptchat.view.KeyboardRelativeLayout;
import top.potens.ptchat.view.MultiLineEditText;

import top.potens.ptchat.R;


/**
 * Created by wenshao on 2017/4/5.
 * 聊天页面
 */

public class ChatWindowActivity extends ToolBarActivity implements TextView.OnEditorActionListener, View.OnClickListener,
        MultiLineEditText.OnBackspacePressListener, ChatMessageAdapter.OnMessageItemClickListener {
    private static final Logger logger = LoggerFactory.getLogger(ChatWindowActivity.class);

    private Context mContext;
    private FaceHelper mFaceHelper;

    private UserBean mUserBean;
    private MultiLineEditText et_message;
    private List<MessageBean> messageBeanList;
    private ChatMessageAdapter mChatMessageAdapter;
    private RecyclerView rv_message_list;
    private int chatListViewLastOffset;
    private int chatListViewLastPosition;
    private LinearLayout face_container;
    private LinearLayout record_container;
    private Dialog mRecordDialog;
    private CirclePlayProgress cpp_record_play;

    private List<View> faceViews = new ArrayList<View>();
    private ViewPager mViewPager;

    private int mLastPosition = 0;


    private boolean isKeyboardShow = true;
    private AudioRecordUtil mAudioRecord;


    private Set<View> mutexView;  // 互斥的view

    private final ActivityHandler mHandler = new ActivityHandler(this);

    private File cameraPhotoFile;
    /**
     * 耗时操作
     */
    private final Runnable sRunnable = new Runnable() {
        @Override
        public void run() {

        }
    };

    /**
     * 更新ui
     * 声明一个静态的Handler内部类，并持有外部类的弱引用
     */
    private static class ActivityHandler extends Handler {

        private final WeakReference<ChatWindowActivity> mActivity;

        private ActivityHandler(ChatWindowActivity activity) {
            this.mActivity = new WeakReference<ChatWindowActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ChatWindowActivity activity = mActivity.get();
            if (activity != null) {

            }
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        mContext = this;
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) supportActionBar.setDisplayHomeAsUpEnabled(false);  //不显示返回按钮


        initUi();
        InitFaceViewPager();
        initSocket();
        initReceiver();
        // initData();

        initDialog();
        // 清除未读的消息
        cleanUnread();


    }


    private void initUi() {
        et_message = findViewById(R.id.et_message);
        et_message.setOnEditorActionListener(this);
        et_message.setOnBackspacePressListener(this);

        ImageButton ib_voice = findViewById(R.id.ib_voice);
        ImageButton ib_image = findViewById(R.id.ib_image);
        ImageButton ib_camera = findViewById(R.id.ib_camera);
        ImageButton ib_emo = findViewById(R.id.ib_face);
        ImageButton ib_more = findViewById(R.id.ib_more);
        ImageButton ib_calculator = findViewById(R.id.ib_calculator);
        Button message_send = findViewById(R.id.message_send);


        ib_voice.setOnClickListener(this);
        ib_image.setOnClickListener(this);
        ib_camera.setOnClickListener(this);
        ib_emo.setOnClickListener(this);
        ib_more.setOnClickListener(this);
        ib_calculator.setOnClickListener(this);
        message_send.setOnClickListener(this);


        face_container = findViewById(R.id.face_container);
        record_container = findViewById(R.id.record_container);
        //  添加互斥的view
        mutexView = new HashSet<>();
        mutexView.add(face_container);
        mutexView.add(record_container);


        final ImageButton ib_start_intercom = findViewById(R.id.ib_start_intercom);
        ib_start_intercom.setOnClickListener(this);


        //表情下小圆点
        mViewPager = findViewById(R.id.face_viewpager);


        rv_message_list = findViewById(R.id.rv_message_list);
        //  在onCreate需要指定RecyclerView的Adapter  否则会报No adapter attached; skipping layout
        mChatMessageAdapter = new ChatMessageAdapter(mContext);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        //linearLayoutManager.setReverseLayout(true);  // 顺序反转
        //linearLayoutManager.setStackFromEnd(true);
        rv_message_list.setLayoutManager(linearLayoutManager);
        rv_message_list.setAdapter(mChatMessageAdapter);
        rv_message_list.setHasFixedSize(true);

        mChatMessageAdapter.setOnItemClickListener(this);


        //监听RecyclerView滚动状态
        rv_message_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                setScrollLocation(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

        });
        KeyboardRelativeLayout rl_chat_main = findViewById(R.id.rl_chat_main);
        rl_chat_main.setOnSizeChangedListener(new KeyboardRelativeLayout.OnSizeChangedListener() {
            @Override
            public void onSizeChanged(int w, int h, int oldWidth, int oldHeight) {
                if (h > oldHeight) { // 键盘隐藏
                    hideKeyboard(et_message);
                    isKeyboardShow = false;
                } else { // 键盘显示
                    isKeyboardShow = true;

                    // 平滑滚动到指定的位置
                    if (rv_message_list.getLayoutManager() != null && chatListViewLastPosition >= 0) {
                        ((LinearLayoutManager) rv_message_list.getLayoutManager()).scrollToPositionWithOffset(chatListViewLastPosition, chatListViewLastOffset);
                    }

                }

            }
        });

        et_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKeyboard(v);  //显示键盘
                hiddenOutSelf(v);   // 隐藏快捷栏所有的显示
            }
        });


    }

    /**
     * 初始化表情窗口
     */
    private void InitFaceViewPager() {
        mFaceHelper = new FaceHelper(7);
        faceViews.add(smallPagerItem());
        FaceVPAdapter mVpAdapter = new FaceVPAdapter(faceViews);
        mViewPager.setAdapter(mVpAdapter);

    }

    // 加载默认小表情
    private View smallPagerItem() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.face_gridview, null);//表情布局
        GridView gridview = layout.findViewById(R.id.chart_face_gv);

        List<String> subList = mFaceHelper.getAllSmallFace();
        SmallFaceAdapter smallFaceAdapter = new SmallFaceAdapter(mContext, subList);
        gridview.setAdapter(smallFaceAdapter);
        gridview.setNumColumns(mFaceHelper.getColumns());
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取表格chart_face_gv中点击所属的LinearLayout(face_vertical)下的第二个节点的text
                String png = ((TextView) ((LinearLayout) view).getChildAt(1)).getText().toString();
                mFaceHelper.insertFace(et_message.getText(), png);
            }
        });
        return gridview;
    }


    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private void initSocket() {

    }

    // 初始化广播接听事件
    private void initReceiver() {


    }


    /**
     * 初始化录音弹出窗口
     */
    private void initDialog() {
        final String audioPath = FileManageUtil.getAudioPath() + "audio.amr";
        View view = View.inflate(this, R.layout.record_dialog, null);
        mRecordDialog = new Dialog(this, R.style.DialogStyle);
        mRecordDialog.setContentView(view);


        Window window = mRecordDialog.getWindow();
        assert window != null;
        window.setGravity(Gravity.BOTTOM); //可设置dialog的位置
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = DisplayUtil.dip2px(mContext, 250);

        window.setAttributes(lp);

        Button btn_record_cancel = view.findViewById(R.id.btn_record_cancel);
        Button btn_record_send = view.findViewById(R.id.btn_record_send);
        TextView tv_timer = view.findViewById(R.id.tv_timer);

        cpp_record_play = view.findViewById(R.id.cpp_record_play);
        // 录音取消
        btn_record_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecordDialog.dismiss();
                mAudioRecord.reset();
                cpp_record_play.setImageResource(R.drawable.ic_action_playback_pause_big);

            }
        });
        // 录音上传
        btn_record_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildAudio(audioPath);
                mAudioRecord.reset();
                mRecordDialog.dismiss();
                cpp_record_play.setImageResource(R.drawable.ic_action_playback_pause_big);


            }
        });
        cpp_record_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecord(v);

            }
        });
        mAudioRecord = new AudioRecordUtil(audioPath, tv_timer, cpp_record_play);


        mAudioRecord.setOnPlayPressListener(new AudioRecordUtil.OnPlayEventListener() {
            @Override
            public void onPlayAutoEnd() {
                cpp_record_play.setImageResource(R.drawable.ic_action_playback_play_big);

            }
        });


    }

    private void cleanUnread() {
        /*DaoSession ds = GlobalApplication.getDaoInstant();
        List<RecentContactBean> list = ds.getRecentContactBeanDao()
                .queryBuilder()
                .where(RecentContactBeanDao.Properties.User_id.eq(mReceiveUserBean.getUser_id()))
                .limit(1)
                .list();
        if (list.size()>0){
            RecentContactBean recentContactBean = list.get(0);
            GlobalApplication.clearUnreadNumber(recentContactBean);
        }*/

    }

    @Override
    public void onCreateCustomToolBar(Toolbar toolbar) {
        mUserBean = (UserBean) getIntent().getSerializableExtra("userInfo");

        super.onCreateCustomToolBar(toolbar);
        getLayoutInflater().inflate(R.layout.toobar_chat_window, toolbar);

        TextView tv_back = toolbar.findViewById(R.id.tv_back);
        TextView tv_user_name = toolbar.findViewById(R.id.tv_user_name);
        tv_user_name.setText(mUserBean.getUserName());
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_SEND == actionId) {  //发送按钮
            sendMessage();
        }
        return true;
    }

    /**
     * 构建消息 JSON
     */
    private void sendMessage() {
        String text = et_message.getText().toString().trim();

        if (TextUtils.isEmpty(text)) {
            ToastUtil.showShortToast("文本内容不能为空");
            return;
        }
        MessageBean messageBean = new MessageBean();

        et_message.setText("");  // 清空输入框


        // 添加到mChatMessageAdapter中
        messageBean.setLocation(MessageBean.LOCATION_RIGHT);
        messageBean.setType(MessageBean.TYPE_TEXT);
        messageBean.setContent(text);
        messageBean.setUserBean(this.mUserBean);
        messageBean.setSendId("11111");
        messageBean.setReceiveId("111");
        messageBean.setCreateTime(new Date().getTime());


        sendMsg(messageBean);
        mChatMessageAdapter.add(messageBean);
        mLastPosition = mChatMessageAdapter.getItemCount();
        // 平滑的滚动  在键盘打开的情况下必须用smoothScrollToPosition模拟滚动 使用scrollToPosition无效
        rv_message_list.smoothScrollToPosition(mLastPosition);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unregisterReceiver(mNewMessageReceiver);
        mAudioRecord.reset();

    }


    /**
     * 隐藏键盘
     *
     * @param v 点击的控件
     */
    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isKeyboardShow) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            v.setFocusable(false);
        }

    }

    /**
     * 显示键盘 并设置焦点
     *
     * @param v 点击的控件
     */
    private void showKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!isKeyboardShow) {
            v.setFocusable(true);
            v.setFocusableInTouchMode(true);
            v.requestFocus();
            v.findFocus();
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 设置滚动的item位置
     */
    private void setScrollLocation(RecyclerView recyclerView, int newState) {
        //当前状态为停止滑动状态SCROLL_STATE_IDLE时
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            /*//et_message.setFocusable(true);
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                //通过LayoutManager找到当前显示的最后的item的position
                mLastPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof LinearLayoutManager) {
                mLastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                //因为StaggeredGridLayoutManager的特殊性可能导致最后显示的item存在多个，所以这里取到的是一个数组
                //得到这个数组后再取到数组中position值最大的那个就是最后显示的position值了
                int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastPositions);
                mLastPosition = findMax(lastPositions);
            }*/

            //时判断界面显示的最后item的position是否等于itemCount总数-1也就是最后一个item的position
            //如果相等则说明已经滑动到最后了
                    /*if (mLastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
                        Toast.makeText(ChatWindowActivity.this, "滑动到底了", Toast.LENGTH_SHORT).show();
                    }*/
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            //获取可视的第一个view
            View topView = layoutManager.getChildAt(0);
            if (topView != null) {
                // 获取与该view的顶部的偏移量
                chatListViewLastOffset = topView.getTop();
                // 得到该View的数组位置
                chatListViewLastPosition = layoutManager.getPosition(topView);
            }

        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (!isEditText(v, ev)) {
                hideKeyboard(v);
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }


    /**
     * 判断当前点击是否为输入框
     *
     * @param v     点击的控件
     * @param event 点击事件
     * @return true:点击的为输入框 false其他控件
     */
    private boolean isEditText(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            // 点击的是输入框区域，保留点击EditText的事件
            return event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.ib_voice) {
            PermissionUtil.rxRequestPermission(this, new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, new PermissionUtil.PermissionCallback() {
                @Override
                public void succeed() {
                    ChatWindowActivity.this.openVoice();
                }
            });
        } else if (i == R.id.ib_image) {
            PermissionUtil.rxRequestPermission(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionUtil.PermissionCallback() {
                @Override
                public void succeed() {
                    ChatWindowActivity.this.openImage();
                }
            });
        } else if (i == R.id.ib_camera) {
            PermissionUtil.rxRequestPermission(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionUtil.PermissionCallback() {
                @Override
                public void succeed() {
                    ChatWindowActivity.this.openCamera();
                }
            });
        } else if (i == R.id.ib_face) {
            if (face_container.getVisibility() == View.GONE) {
                face_container.setVisibility(View.VISIBLE);
                hiddenOutSelf(face_container);
            } else {
                face_container.setVisibility(View.GONE);
            }

        } else if (i == R.id.ib_more) {

        } else if (i == R.id.ib_calculator) {//startActivity(new Intent(mContext,CalculatorActivity.class));

        } else if (i == R.id.ib_start_intercom) {
            startRecord(view);
        } else if (i == R.id.message_send) {
            sendMessage();
        } else {

        }
    }

    // 隐藏除自己之外的控件
    private void hiddenOutSelf(View self) {
        for (View view : mutexView) {
            if (self != view) {
                view.setVisibility(View.GONE);
            }
        }

    }

    private void openVoice() {
        if (record_container.getVisibility() == View.GONE) {
            record_container.setVisibility(View.VISIBLE);
            hiddenOutSelf(record_container);
        } else {
            record_container.setVisibility(View.GONE);
        }
    }

    private void openImage() {
        Matisse.from(this)
                .choose(MimeType.ofAll(), false)    // //照片视频全部显示
                .countable(true)    // 有序图片
                .maxSelectable(9)   // 最大选择数量
                .capture(true)  // 打开相机
                .captureStrategy(new CaptureStrategy(true, "com.zhihu.matisse.sample.fileprovider")) // 拍照相关，媒体处理authority
                //.gridExpectedSize(120)   // 图片显示表格的大小
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)   //图像选择和预览活动所需的方向。
                .thumbnailScale(0.85f)  // 缩放比例
                .imageEngine(new Glide4Engine())    // 图片加载引擎
                .forResult(HandlerCode.REQUEST_IMAGE);  //请求码
    }

    private void openCamera() {

        //调用getExternalCacheDir()可以得到应用关联缓存目录，
        // Android6.0之后读写SD卡被列为了危险权限，而这个目录不需要申请权限，
        cameraPhotoFile = new File(this.getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
        try {
            if (cameraPhotoFile.exists()) {
                boolean delete = cameraPhotoFile.delete();
                if (delete) {
                    boolean isFile = cameraPhotoFile.createNewFile();
                }
            }
        } catch (IOException e) {
            logger.error("openCamera error", e);
        }
        Uri imageFileUri;
        if (Build.VERSION.SDK_INT >= 24) {
            //如果是7.0 以上的版本就必须使用这个方法，
            // 第一个参数是context,第二个参数可以是任意唯一的字符窜，
            // 第三参数是我们刚刚建立的file对象。
            //因为7.0以后直接使用本地真实路径的Uri本认为是不安全的，会抛出FileUriExposedException异常的
            //而FileProvider这是一种特殊的内容提供器，它使用了和内容提供器类似的机制来对数据进行保护，
            // 可以选择性的将封装过的Uri共享给外部，从而提高了应用的安全性。
            imageFileUri = FileProvider.getUriForFile(this, "com.zhihu.matisse.sample.fileprovider", cameraPhotoFile);
        } else {
            imageFileUri = Uri.fromFile(cameraPhotoFile);//获取文件的Uri
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);// 更改系统默认存储路径
        startActivityForResult(intent, HandlerCode.REQUEST_CAMERA);
    }


    private MessageBean buildAudio(String filepath) {

        MessageBean messageBean = new MessageBean();
        messageBean.setLocation(MessageBean.LOCATION_RIGHT);
        messageBean.setType(MessageBean.TYPE_AUDIO);
        messageBean.setContent("file://" + filepath);
        messageBean.setUserBean(mUserBean);
        messageBean.setSendId("111");
        messageBean.setSendCode("222");
        messageBean.setDuration(mAudioRecord.getDuration());
        messageBean.setReceiveId("11");
        messageBean.setCreateTime(new Date().getTime());
        mChatMessageAdapter.add(messageBean);
        mLastPosition = mChatMessageAdapter.getItemCount();
        rv_message_list.smoothScrollToPosition(mLastPosition);
        return messageBean;
    }


    private MessageBean buildImage(String filepath) {
        MessageBean messageBean = new MessageBean();
        messageBean.setLocation(MessageBean.LOCATION_RIGHT);
        messageBean.setType(MessageBean.TYPE_IMAGE);
        messageBean.setContent("file://" + filepath);
        messageBean.setUserBean(mUserBean);
        messageBean.setSendId("111");
        messageBean.setSendCode("222");
        mChatMessageAdapter.add(messageBean);
        mLastPosition = mChatMessageAdapter.getItemCount();
        // 添加到mChatMessageAdapter中
        // 平滑的滚动  在键盘打开的情况下必须用smoothScrollToPosition模拟滚动 使用scrollToPosition无效
        rv_message_list.smoothScrollToPosition(mLastPosition);
        return messageBean;
    }

    private void sendMsg(MessageBean messageBean) {

        GlobalApplication.getPtchat().getDataInteraction().sendData(messageBean, new SendCallback() {
            @Override
            public void success() {
                logger.debug("success");
            }

            @Override
            public void error() {
                logger.debug("error");

            }
        });

    }


    private void startRecord(View view) {

        if (mAudioRecord.getStatus() == AudioRecordUtil.STATUS_RECORD_FREE) {
            mRecordDialog.show();
            mAudioRecord.startRecord();

        } else if (mAudioRecord.getStatus() == AudioRecordUtil.STATUS_RECORDING) {
            mAudioRecord.endRecord();
            cpp_record_play.setImageResource(R.drawable.ic_action_playback_play_big);

        } else if (mAudioRecord.getStatus() == AudioRecordUtil.STATUS_PLAY_FREE) {
            mAudioRecord.startPlay();
            cpp_record_play.setImageResource(R.drawable.ic_action_playback_pause_big);

        } else if (mAudioRecord.getStatus() == AudioRecordUtil.STATUS_PLAYING) {
            mAudioRecord.endPlay();
            cpp_record_play.setImageResource(R.drawable.ic_action_playback_play_big);

        }

    }

    @Override
    public void onBackspacePressed() {
        mFaceHelper.delete(et_message.getText());
    }


    @Override
    public void onMessageItemClick(View view, int position) {
        List<MessageBean> dataList = mChatMessageAdapter.getDataList();
        MessageBean messageBean = dataList.get(position);
        if (MessageBean.TYPE_AUDIO.equals(messageBean.getType())) {  // 点击了语音类型的消息
        } else if (MessageBean.TYPE_TEXT.equals(messageBean.getType())) {

        } else if (MessageBean.TYPE_IMAGE.equals(messageBean.getType())) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == HandlerCode.REQUEST_IMAGE && resultCode == RESULT_OK) {
            List<String> strings = Matisse.obtainPathResult(data);
            for (String filepath : strings) {
                buildImage(filepath);
            }
        } else if (requestCode == HandlerCode.REQUEST_CAMERA) {
            buildImage(cameraPhotoFile.getAbsolutePath());

        }
    }
}
