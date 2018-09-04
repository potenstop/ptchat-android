package top.potens.ptchat.util;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wenshao on 2018/9/4.
 * view 工具类
 */

public class ViewUtil {

    /**
     * 判断点击事件是否点击的为该view
     * @param view       view
     * @param event      点击事件
     * @return           true: 是 false: 否
     */
    public static boolean isTouchView(View view, MotionEvent event) {
        if (view != null ) {
            int[] leftTop = {0, 0};
            //获取当前发送按钮的location
            view.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + view.getHeight();
            int right = left + view.getWidth();
            // 点击的是输入框区域，保留点击EditText的事件
            return event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom;
        }
        return false;
    }
}
