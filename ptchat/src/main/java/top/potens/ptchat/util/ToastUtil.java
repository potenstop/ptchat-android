package top.potens.ptchat.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by wenshao on 2018/7/2.
 * toast封装类
 */

public class ToastUtil {
    private static Toast toast;//实现不管我们触发多少次Toast调用，都只会持续一次Toast显示的时长
    public static String prefix = "";

    /**
     * 短时间显示Toast【居下】
     *
     * @param msg 显示的内容-字符串
     */
    public static void showShortToast(Context context, String msg) {
        if (context != null) {
            if (toast == null) {
                toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
            }
            toast.setText(prefix + msg);

            //1、setGravity方法必须放到这里，否则会出现toast始终按照第一次显示的位置进行显示（比如第一次是在底部显示，那么即使设置setGravity在中间，也不管用）
            //2、虽然默认是在底部显示，但是，因为这个工具类实现了中间显示，所以需要还原，还原方式如下：
            toast.setGravity(Gravity.BOTTOM, 0, dip2px(context, 64));
            toast.show();
        }
    }

    /**
     * 短时间显示Toast【居中】
     *
     * @param msg 显示的内容-字符串
     */
    public static void showShortToastCenter(Context context, String msg) {
        if (context != null) {
            if (toast == null) {
                toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
            }
            toast.setText(prefix + msg);

            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    /**
     * 短时间显示Toast【居上】
     *
     * @param msg 显示的内容-字符串
     */
    public static void showShortToastTop(Context context, String msg) {
        if (context != null) {
            if (toast == null) {
                toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
            }
            toast.setText(prefix + msg);

            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        }
    }

    /**
     * 长时间显示Toast【居下】
     *
     * @param msg 显示的内容-字符串
     */
    public static void showLongToast(Context context, String msg) {
        if (context != null) {
            if (toast == null) {
                toast = Toast.makeText(context, null, Toast.LENGTH_LONG);
            }
            toast.setText(prefix + msg);

            toast.setGravity(Gravity.BOTTOM, 0, dip2px(context, 64));
            toast.show();
        }
    }

    /**
     * 长时间显示Toast【居中】
     *
     * @param msg 显示的内容-字符串
     */
    public static void showLongToastCenter(Context context, String msg) {
        if (context != null) {
            if (toast == null) {
                toast = Toast.makeText(context, null, Toast.LENGTH_LONG);
            }
            toast.setText(prefix + msg);

            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    /**
     * 长时间显示Toast【居上】
     *
     * @param msg 显示的内容-字符串
     */
    public static void showLongToastTop(Context context, String msg) {
        if (context != null) {
            if (toast == null) {
                toast = Toast.makeText(context, null, Toast.LENGTH_LONG);
            }
            toast.setText(prefix + msg);

            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        }
    }

    /*=================================常用公共方法============================*/
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
