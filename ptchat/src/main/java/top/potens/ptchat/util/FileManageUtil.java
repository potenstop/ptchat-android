package top.potens.ptchat.util;

import android.content.Context;
import android.os.Environment;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import top.potens.ptchat.activity.ImageMessageOverallActivity;


/**
 * Created by wenshao on 2017/4/20.
 * 文件管理
 */

public class FileManageUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileManageUtil.class);

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



    /**
     * 获取临时录音目录
     * @param context       Context
     * @return              路径
     */
    public static String getTmpAudioPath(Context context) {
        String tmpFilepath = context.getFilesDir().getPath() + "/audio/";
        createPath(tmpFilepath);
        return tmpFilepath;
    }


    /**
     * 创建目录
     */
    private static void createPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 复制单个文件
     * @param oldPath       原文件路径 如：data/video/xxx.mp4
     * @param newPath       复制后路径 如：data/oss/xxx.mp4
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int byteRead = 0;


            URI uri = URI.create(oldPath);
            oldPath = uri.getPath();

            uri = URI.create(newPath);
            newPath = uri.getPath();

            File oldFile = new File(oldPath);
            File newFile = new File(newPath);

            String parent = newFile.getParent();
            createPath(parent);
            if (!newFile.exists()) {
                boolean createResult = newFile.createNewFile();
                if (!createResult) return;
            }
            if (oldFile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                while ((byteRead = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteRead);
                }
                inStream.close();
            }
        } catch (Exception e) {
            logger.error("copyFile error", e);
        }

    }
}
