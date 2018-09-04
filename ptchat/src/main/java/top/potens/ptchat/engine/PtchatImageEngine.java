package top.potens.ptchat.engine;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by wenshao on 2018/8/25.
 * chat页面图片加载引擎
 */
public interface PtchatImageEngine {
    public void loadChatHead(Context context, String url, ImageView view);
    public void loadChatImage(Context context, String url, ImageView view);
    public void loadCommonImage(Context context, String url, ImageView view);
}
