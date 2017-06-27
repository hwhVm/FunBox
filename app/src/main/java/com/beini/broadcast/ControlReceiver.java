package com.beini.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.view.KeyEvent;

import com.beini.util.BLog;


/**
 * Created by beini on 2017/3/16.
 */

public class ControlReceiver extends BroadcastReceiver {//还没有注册
    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();

        if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intentAction)) {
            BLog.d("音频系统连接 或者 断开");
        } else if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
            KeyEvent event = intent
                    .getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (event == null) {
                return;
            }
            int keycode = event.getKeyCode();
            int action = event.getAction();
            long eventtime = event.getEventTime();

            // single quick press: pause/resume.
            // double press: next track
            // long press: start auto-shuffle mode.
            switch (keycode) {
                case KeyEvent.KEYCODE_MEDIA_STOP:
                    BLog.d("监听到蓝牙耳机按键 停止");
                    break;
                case KeyEvent.KEYCODE_HEADSETHOOK:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    BLog.d("监听到蓝牙耳机按键 暂停 或者播放");
                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    BLog.d("监听到蓝牙耳机按键 下一首");
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    BLog.d("监听到蓝牙耳机按键 上一首");
                    break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    BLog.d("监听到蓝牙耳机按键 暂停");
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                    BLog.d("监听到蓝牙耳机按键 播放");
                    break;

            }
        }
    }
}
