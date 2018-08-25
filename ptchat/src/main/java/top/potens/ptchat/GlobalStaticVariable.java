package top.potens.ptchat;

import top.potens.ptchat.activity.ChatWindowActivity;

/**
 * Created by wenshao on 2018/8/21.
 * 全局静态变量
 */

public class GlobalStaticVariable {
    private static ChatWindowActivity chatWindowActivity;
    private static Ptchat ptchat;

    public static Ptchat getPtchat() {
        return ptchat;
    }

    public static void setPtchat(Ptchat ptchat) {
        GlobalStaticVariable.ptchat = ptchat;
    }

    public static ChatWindowActivity getChatWindowActivity() {
        return chatWindowActivity;
    }

    public static void setChatWindowActivity(ChatWindowActivity chatWindowActivity) {
        GlobalStaticVariable.chatWindowActivity = chatWindowActivity;
    }
}
