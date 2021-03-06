package top.potens.ptchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;

import top.potens.ptchat.activity.ChatWindowActivity;
import top.potens.ptchat.bean.MessageBean;
import top.potens.ptchat.bean.UserBean;
import top.potens.ptchat.engine.PtchatImageEngine;
import top.potens.ptchat.engine.MatisseImageEngine;
import top.potens.ptchat.network.DataInteraction;
import top.potens.ptchat.util.ToastUtil;

/**
 * Created by wenshao on 2018/7/22.
 * 调用入口
 */
public class Ptchat {
    private final WeakReference<Activity> mActivity;
    private final WeakReference<Fragment> mFragment;
    private UserBean userBean;
    private DataInteraction mDataInteraction;
    private MatisseImageEngine mMatisseImageEngine;
    private PtchatImageEngine mPtchatImageEngine;
    private String mImageDir;;
    private String mFileDir;
    private String mToastPrefix;

    private Ptchat(Activity activity) {
        this(activity, null);
    }

    private Ptchat(Fragment fragment) {
        this(fragment.getActivity(), fragment);
    }

    private Ptchat(Activity activity, Fragment fragment) {
        mActivity = new WeakReference<>(activity);
        mFragment = new WeakReference<>(fragment);
        init();
    }

    private void init() {
        mImageDir = mActivity.get().getFilesDir().getPath()+"/image";
        mFileDir = mActivity.get().getFilesDir().getPath()+"/file";
    }

    /**
     * 创建PtChat
     *
     * @param activity activity对象
     * @return Ptchat
     */
    public static Ptchat from(Activity activity) {
        return new Ptchat(activity);
    }

    /**
     * 创建PtChat
     *
     * @param fragment fragment对象
     * @return Ptchat
     */
    public static Ptchat from(Fragment fragment) {
        return new Ptchat(fragment);
    }

    /**
     * 当前用户的user信息
     *
     * @param userBean userBean
     * @return Ptchat
     */
    public Ptchat userInfo(UserBean userBean) {
        this.userBean = userBean;
        return this;
    }

    /**
     * 配置数据交互
     *
     * @param dataInteraction DataInteraction
     * @return Ptchat
     */
    public Ptchat dataStrategy(DataInteraction dataInteraction) {
        this.mDataInteraction = dataInteraction;
        return this;
    }

    /**
     * 配置相册图片渲染
     * @param matisseImageEngine MatisseImageEngine
     * @return Ptchat
     */
    public Ptchat matisseImageEngine(MatisseImageEngine matisseImageEngine) {
        this.mMatisseImageEngine = matisseImageEngine;
        return this;
    }

    /**
     * 配置chat页面的图片渲染
     * @param ptchatImageEngine       PtchatImageEngine
     * @return                      Ptchat
     */
    public Ptchat chatImageEngine(PtchatImageEngine ptchatImageEngine) {
        this.mPtchatImageEngine = ptchatImageEngine;
        return this;
    }

    /**
     * 配置图片保存目录
     * @param imageDir      目录路径
     * @return              Ptchat
     */
    public Ptchat imageDir(String imageDir){
        mImageDir = imageDir;
        return  this;
    }
    /**
     * 配置文件保存目录
     * @param fileDir       目录路径
     * @return              Ptchat
     */
    public Ptchat fileDir(String fileDir){
        mFileDir = fileDir;
        return  this;
    }

    /**
     * 配置文件保存目录
     * @param prefix        前缀
     * @return              Ptchat
     */
    public Ptchat toastPrefix(String prefix){
        mToastPrefix = prefix;
        ToastUtil.prefix = prefix;
        return  this;
    }
    public MatisseImageEngine getMatisseImageEngine() {
        return mMatisseImageEngine;
    }
    public PtchatImageEngine getChatImageEngine() {
        return mPtchatImageEngine;
    }

    public DataInteraction getDataInteraction() {
        return mDataInteraction;
    }
    public String getImageDir() {
        return mImageDir;
    }

    public String getFileDir() {
        return mFileDir;
    }

    public String getToastPrefix() {
        return mToastPrefix;
    }

    /**
     * 跳转至chat页面
     *
     * @param requestCode 请求码
     */
    public Ptchat forResult(int requestCode) {
        Activity activity = mActivity.get();
        if (activity == null) {
            return this;
        }
        Intent intent = new Intent(activity, ChatWindowActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("userInfo", userBean);
        intent.putExtras(bundle);
        Fragment fragment = mFragment.get();
        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
        GlobalStaticVariable.setPtchat(this);
        return this;
    }

    // 添加一条对方发来的消息
    public boolean addOtherMessage(MessageBean messageBean) {
        if (GlobalStaticVariable.getChatWindowActivity() != null) {
            GlobalStaticVariable.getChatWindowActivity().insertPageMessage(messageBean);
            return true;
        }
        return false;
    }
}
