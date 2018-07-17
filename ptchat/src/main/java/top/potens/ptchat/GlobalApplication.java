package top.potens.ptchat;

import android.app.Application;
import android.content.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Created by Administrator on 2017/9/19.
 * 程序入口
 * 完成app初始化操作
 */

public class GlobalApplication extends Application {
    private static final Logger logger = LoggerFactory.getLogger(GlobalApplication.class);
    private static Context mContext;

    public static Context getAppContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 获取Context
        mContext = getApplicationContext();
    }


}
