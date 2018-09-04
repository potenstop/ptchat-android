package top.potens.ptchat_android.engine;

import android.content.Context;
import android.widget.ImageView;

import top.potens.ptchat.engine.PtchatImageEngine;
import top.potens.ptchat_android.image.GlideApp;

/**
 * Created by wenshao on 2018/8/25.
 * chat页面图片加载
 */
public class PtchatGlide4Engine implements PtchatImageEngine {
    @Override
    public void loadChatHead(Context context, String url, ImageView view) {
       GlideApp.with(context)
                .load(url)
                .configConcatHead()
                .into(view);

    }

    @Override
    public void loadChatImage(Context context, String url, ImageView view) {
        GlideApp.with(context)
                .load(url)
                .configChatImage(view.getMaxWidth(), view.getMaxHeight())
                .into(view);
    }

    @Override
    public void loadCommonImage(Context context, String url, ImageView view) {
        GlideApp.with(context)
                .load(url)
                .into(view);
    }

}
