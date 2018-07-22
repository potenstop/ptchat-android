package top.potens.ptchat;

import android.app.Application;
import android.content.Context;


import java.lang.ref.WeakReference;


/**
 * Created by Administrator on 2017/9/19.
 * 程序入口
 * 完成app初始化操作
 */

public class GlobalApplication extends Application {
    private static Context mContext;
    private static Ptchat mPtchat;

    public static Context getAppContext() {
        return mContext;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        // 获取Context
        mContext = getApplicationContext();
    }

    public static void setPtchat(Ptchat ptchat) {
        mPtchat = ptchat;
    }
    public static Ptchat getPtchat() {
        return mPtchat;
    }
}
