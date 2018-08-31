package top.potens.ptchat_android.image;

import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by wenshao on 2018/4/29.
 */
@GlideExtension
public class ImageGlideExtension {
    // 缩略图的最小尺寸，单位：px
    private static final int MINI_THUMB_SIZE = 100;

    /**
     * 将构造方法设为私有，作为工具类使用
     */
    private ImageGlideExtension() {
    }

    /**
     * 联系人头像快速设置
     */
    @GlideOption
    public static void configConcatHead(RequestOptions options) {
        options.placeholder(top.potens.ptchat.R.mipmap.load_ing).override(60, 60);

    }
    /**
     * 联系人头像快速设置
     */
    @GlideOption
    public static void configChatImage(RequestOptions options, int width, int height) {
        options.placeholder(top.potens.ptchat.R.mipmap.load_ing).override(width, height);
    }

    /**
     * 1.自己新增的方法的第一个参数必须是RequestOptions options
     * 2.方法必须是静态的
     * @param options   RequestOptions
     */
    @GlideOption
    public static void miniThumb(RequestOptions options) {
        options.fitCenter().override(MINI_THUMB_SIZE);
    }
}
