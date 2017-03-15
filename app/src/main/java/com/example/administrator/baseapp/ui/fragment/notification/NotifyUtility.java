package com.example.administrator.baseapp.ui.fragment.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import com.example.administrator.baseapp.R;

/** 通知工具
 * Created by beini
 */

public class NotifyUtility {
    public static final int NOTIFICATION_FLAG = 1;
    private static final String MSG = "MSG";

    /**
     * @param curActivity 当前的activity
     * @param cls 新起的activity
     * @param ContentText 显示的msg
     */
    static public void start(Context curActivity, Class<?> cls, String TitleText, String ContentText) {


        NotificationManager manager = (NotificationManager)curActivity.getSystemService(Context.NOTIFICATION_SERVICE);

        // 通过Notification.Builder来创建通知，注意API Level
        // API16之后才支持
        Notification notify = new Notification.Builder(curActivity)
                .setContentTitle(TitleText)
                .setTicker(ContentText) //通知首次出现在通知栏，带上升动画效果的
                .setContentText(ContentText)
                .setSmallIcon(R.mipmap.ic_launcher) //设置通知小ICON
               .setNumber(0).build(); // 需要注意build()是在API
        notify.defaults |= Notification.DEFAULT_SOUND;
        // level16及之后增加的，API11可以使用getNotificatin()来替代
        notify.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
        manager.notify(NOTIFICATION_FLAG, notify);// 步骤4：通过通知管理器来发起通知。如果id不同，则每click，在status哪里增加一个提示

    }
}
