package top.potens.ptchat.util;

import android.content.Context;
import android.os.Environment;


import java.io.File;



/**
 * Created by wenshao on 2017/4/20.
 * 文件管理
 */

public class FileManageUtil {
    // sd卡项目目录
    private static String projectPath;
    // 语音文件存放路径
    private static String audioPath;


    /**
     * 1、判断SD卡是否存在
     */
    public static boolean hasSdcard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }
    /**
     * 获取项目根路径
     */
    public static String getProjectPath(Context context) {
        if (projectPath==null) {
            if (hasSdcard()) {
                projectPath = Environment.getExternalStorageDirectory()+"/ptchat";
            } else {
                projectPath = context.getFilesDir().getPath();

            }
            createPath(projectPath);
        }
        return projectPath;
    }

    public static String getAudioPath(Context context) {
        if (audioPath==null){
            audioPath = getProjectPath(context)+"/audio/";
            createPath(audioPath);
        }
        return audioPath;
    }

    /**
     * 创建目录
     */
    private static void createPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
    }
}
