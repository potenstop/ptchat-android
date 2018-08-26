package top.potens.ptchat.engine;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by wenshao on 2018/8/25.
 * chat页面图片加载引擎
 */
public interface ChatImageEngine {
    public void loadHead(Context context, String url, ImageView view);
    public void loadImage(Context context, String url, ImageView view);
}
