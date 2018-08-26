package top.potens.ptchat.constant;

import android.Manifest;
import android.app.Activity;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.List;

import top.potens.ptchat.bean.PermissionInfoBean;

/**
 * Created by wenshao on 2017/4/18.
 * 权限检查
 */

public class PermissionConstant {
    private static final HashMap<String, String> permsMap = new HashMap<>();

    static {
        // permsMap.put(Manifest.permission.READ_EXTERNAL_STORAGE, "读手机储存");
        permsMap.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, "读写手机储存");
        permsMap.put(Manifest.permission.CAMERA, "相机");
        permsMap.put(Manifest.permission.RECORD_AUDIO, "录音");
        permsMap.put(Manifest.permission.ACCESS_COARSE_LOCATION, "定位");
        permsMap.put(Manifest.permission.READ_CONTACTS, "读取联系人");
        permsMap.put(Manifest.permission.SEND_SMS, "发送短信");
        permsMap.put(Manifest.permission.CALL_PHONE, "拨打电话");
    }

    /**
     * 获取sd存储卡读写权限
     */
    public static PermissionInfoBean getExternalStoragePermissions(int requestCode) {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        return new PermissionInfoBean(permissions, requestCode, permissions.length);
    }

    /**
     * 获取拍照权限
     */
    public static PermissionInfoBean getCameraPermissions(int requestCode) {

        String[] permissions = {Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        return new PermissionInfoBean(permissions, requestCode, permissions.length);
    }

    /**
     * 获取麦克风权限
     */
    public static PermissionInfoBean getAudioPermissions(int requestCode) {
        String[] permissions = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        return new PermissionInfoBean(permissions, requestCode, permissions.length);
    }

    /**
     * 获取定位权限
     */
    public static PermissionInfoBean getLocationPermissions(int requestCode) {
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION};
        return new PermissionInfoBean(permissions, requestCode, permissions.length);
    }

    /**
     * 获取读取联系人权限
     */
    public static PermissionInfoBean getContactsPermissions(int requestCode) {
        String[] permissions = {Manifest.permission.READ_CONTACTS};
        return new PermissionInfoBean(permissions, requestCode, permissions.length);
    }

    /**
     * 获取发送短信权限
     */
    public static PermissionInfoBean getSendSMSPermissions(int requestCode) {
        String[] permissions = {Manifest.permission.SEND_SMS};
        return new PermissionInfoBean(permissions, requestCode, permissions.length);
    }

    /**
     * 获取拨打电话权限
     */
    public static PermissionInfoBean getCallPhonePermissions(int requestCode) {
        String[] permissions = {Manifest.permission.CALL_PHONE};
        return new PermissionInfoBean(permissions, requestCode, permissions.length);
    }

    /**
     * perms 转中文描述
     * @param perms     权限
     * @return          描述
     */
    public static String getDescribes(String[] perms) {
        StringBuilder sb = new StringBuilder();
        for (String perm : perms) {
            if (permsMap.containsKey(perm)) {
                sb.append(permsMap.get(perm));
                sb.append("、");
            }
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }
    public static String getDescribes(List<String> perms) {
        String[] keys=perms.toArray(new String[perms.size()]);
        return getDescribes(keys);
    }

}
