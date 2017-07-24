package com.beini.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.beini.R;
import com.beini.broadcast.ForegroundServiceRevice;
import com.beini.ui.activity.IndexActivity;
import com.beini.util.BLog;

/**
 * Created by beini on 2017/7/20.
 */

public class ForegroundService extends Service {

    private final int FOREGROUND_ID = 0x110;
    public static final String ACTION_CLICK = "ACTION_CLICK";
    private static final int REQUEST_CODE = 0x111;
    private ForegroundServiceRevice foregroundServiceRevice;

    private final IBinder mBinder = new ForegroundService.LocalBinder();


    public class LocalBinder extends Binder {

        public ForegroundService getService() {
            return ForegroundService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        BLog.e("   ForegroundService  onCreate()  ");
        beginForegroundService();
//        beginForegroundServiceCustomerNotification();
        //注册广播
        foregroundServiceRevice = new ForegroundServiceRevice();
        IntentFilter filterIntent = new IntentFilter();
        filterIntent.addAction(ACTION_CLICK);
        registerReceiver(foregroundServiceRevice, filterIntent);
    }

    /**
     * 自定义通知以及点击事情的处理，内容的改变等
     * PendingIntent类提供了一种创建可由其它应用程序在稍晚时间触发的Intent的机制。用于启动Activity ,Service,广播
     */
    private void beginForegroundServiceCustomerNotification() {
        BLog.e("     beginForegroundServiceCustomerNotification  ");
        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.customer_notification);
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext());
        builder.setContent(remoteViews);
        builder.setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher);
        Notification notification = builder.build();

        Intent intentClick = new Intent((ACTION_CLICK));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), REQUEST_CODE, intentClick, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.btn_foreground_click, pendingIntent);
        startForeground(FOREGROUND_ID, notification);//开始前台服务
        //改变通知栏控件的内容
//        notification.contentView.setTextViewText(R.id.btn_foreground_click,"我改变你的内容");
    }

    /**
     * 调用系统的通知
     */
    private void beginForegroundService() {
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext());//api > 11
        Intent intent = new Intent(this, IndexActivity.class);
        builder.setContentIntent(PendingIntent.getActivity(this, 0, intent, 0))// 设置PendingIntent
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))// 设置下拉列表中的图标(大图标)
                .setContentTitle("title")// 设置下拉列表里的标题
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("content")// 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间
        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        startForeground(FOREGROUND_ID, notification);//开始前台服务
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
        if (foregroundServiceRevice != null) {
            unregisterReceiver(foregroundServiceRevice);
        }
    }
}
