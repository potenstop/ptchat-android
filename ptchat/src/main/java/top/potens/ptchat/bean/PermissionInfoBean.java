package top.potens.ptchat.bean;

/**
 * Created by wenshao on 2018/8/26.
 * 权限bean对象
 */
public class PermissionInfoBean {
    private String[] permissions;
    private int code;
    private int member;

    public PermissionInfoBean(String[] permissions, int code, int member) {
        this.permissions = permissions;
        this.code = code;
        this.member = member;
    }

    public String[] getPermissions() {
        return permissions;
    }

    public int getCode() {
        return code;
    }

    public int getMember() {
        return member;
    }
}
