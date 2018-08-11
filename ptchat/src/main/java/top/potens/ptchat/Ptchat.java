package top.potens.ptchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;

import top.potens.ptchat.activity.ChatWindowActivity;
import top.potens.ptchat.bean.MessageBean;
import top.potens.ptchat.bean.UserBean;
import top.potens.ptchat.network.DataInteraction;

/**
 * Created by wenshao on 2018/7/22.
 * 调用入口
 */
public class Ptchat {
    private final WeakReference<Activity> mActivity;
    private final WeakReference<Fragment> mFragment;
    private UserBean userBean;
    private DataInteraction mDataInteraction;


    private Ptchat(Activity activity) {
        this(activity, null);
    }

    private Ptchat(Fragment fragment) {
        this(fragment.getActivity(), fragment);
    }

    private Ptchat(Activity activity, Fragment fragment) {
        mActivity = new WeakReference<>(activity);
        mFragment = new WeakReference<>(fragment);
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
     * @param userBean      userBean
     * @return              Ptchat
     */
    public Ptchat userInfo(UserBean userBean) {
        this.userBean = userBean;
        return this;
    }

    /**
     * 配置数据交互
     * @param dataInteraction       DataInteraction
     * @return                      Ptchat
     */
    public Ptchat dataStrategy(DataInteraction dataInteraction) {
        this.mDataInteraction = dataInteraction;
        return this;
    }

    public DataInteraction getDataInteraction() {
        return mDataInteraction;
    }

    /**
     * 跳转至chat页面
     * @param requestCode   请求码
     */
    public Ptchat forResult(int requestCode) {
        Activity activity = mActivity.get();
        if (activity == null) {
            return this;
        }
        Intent intent = new Intent(activity, ChatWindowActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("userInfo", userBean);
        intent.putExtras(bundle);
        Fragment fragment = mFragment.get();
        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
        GlobalApplication.setPtchat(this);
        return this;
    }

    // 添加一条对方发来的消息
    public boolean addOtherMessage(MessageBean messageBean) {
        if (GlobalApplication.mChatWindowActivity!=null){
            GlobalApplication.mChatWindowActivity.insertPageMessage(messageBean);
            return true;
        }
        return false;
    }
}
