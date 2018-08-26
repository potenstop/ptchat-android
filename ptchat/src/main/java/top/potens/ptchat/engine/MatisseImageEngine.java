package top.potens.ptchat.engine;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.zhihu.matisse.engine.ImageEngine;

/**
 * Created by wenshao on 2018/8/25.
 * Matisse的图片加载
 */
public interface MatisseImageEngine  {

    void loadThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri);

    void loadGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri);

    void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri);

    void loadGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri);

    boolean supportAnimatedGif();
}
