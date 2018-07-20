package top.potens.ptchat.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;


import com.tbruyelle.rxpermissions2.RxPermissions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by wenshao on 2017/4/18.
 * 权限检查
 */

public class PermissionUtil {
    private static final Logger logger = LoggerFactory.getLogger(PermissionUtil.class);

    public static void requestPermission(Activity activity, String permission) {
        if (ContextCompat.checkSelfPermission(activity, permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, 0);
        }
    }
    public static void rxRequestPermission(FragmentActivity activity, String[] permissions, final PermissionCallback callback) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        Disposable subscribe = rxPermissions
                .request(permissions)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) { // 在android 6.0之前会默认返回true
                            // 已经获取权限
                            callback.succeed();
                        } else {
                            // 未获取权限

                        }
                    }
                },new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        logger.error("Consumer error", throwable);
                    }
                }, new Action() {
                    @Override
                    public void run() {
                        logger.debug("run");
                    }
                });
    }

    public interface PermissionCallback {
        public void succeed();
    }
}
