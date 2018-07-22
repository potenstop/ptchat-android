package top.potens.ptchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.zhihu.matisse.ui.MatisseActivity;

import java.lang.ref.WeakReference;

import top.potens.ptchat.activity.ChatWindowActivity;
import top.potens.ptchat.bean.MessageBean;
import top.potens.ptchat.bean.UserBean;

/**
 * Created by wenshao on 2018/7/22.
 * 调用入口
 */
public class PtChat {
    private final WeakReference<Activity> mActivity;
    private final WeakReference<Fragment> mFragment;
    private UserBean userBean;


    private PtChat(Activity activity) {
        this(activity, null);
    }

    private PtChat(Fragment fragment) {
        this(fragment.getActivity(), fragment);
    }

    private PtChat(Activity activity, Fragment fragment) {
        mActivity = new WeakReference<>(activity);
        mFragment = new WeakReference<>(fragment);
    }

    /**
     * 创建PtChat
     *
     * @param activity activity对象
     * @return PtChat
     */
    public static PtChat from(Activity activity) {
        return new PtChat(activity);
    }

    /**
     * 创建PtChat
     *
     * @param fragment fragment对象
     * @return PtChat
     */
    public static PtChat from(Fragment fragment) {
        return new PtChat(fragment);
    }

    /**
     * 当前用户的user信息
     * @param userBean      userBean
     * @return              PtChat
     */
    public PtChat userInfo(UserBean userBean) {
        this.userBean = userBean;
        return this;
    }



    /**
     * 跳转至chat页面
     * @param requestCode   请求码
     */
    public PtChat forResult(int requestCode) {
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
        return this;
    }

    
    public void addMessage(MessageBean messageBean) {


    }
}
