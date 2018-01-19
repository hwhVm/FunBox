package com.beini.ui.fragment.multimedia.audio;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.broadcast.ControlReceiver;
import com.beini.util.BLog;

/**
 * 控制Audio输出通道切换
 * Audio 输出通道有很多，Speaker、headset、bluetooth A2DP等。
 * isBluetoothA2dpOn()：检查A2DPAudio是否通过蓝牙耳机；
 * isSpeakerphoneOn()：检查扬声器是否打开；
 * isWiredHeadsetOn()：检查线控耳机是否连着；注意这个方法只是用来判断耳机是否是插入状态，并不能用它的结果来判定当前的Audio是通过耳机输出的，这还依赖于其他条件。
 * http://blog.csdn.net/hellofeiya/article/details/9667879
 * http://blog.csdn.net/u011068702/article/details/51730042
 * http://blog.csdn.net/u010779707/article/details/51320267
 * http://www.jianshu.com/p/b52754c50f89
 * http://wenku.baidu.com/link?url=dRDN8d9yI1ZUFDFMIK0m5BAxeMv79qqZYLHimYUc9joABuPgl7WUhh3ESSAx6kJ4cGTzfQDp7-BgZSL8Js1MKqx6Z8OhKZe5Dl9z95kqx8K
 */
@ContentView(R.layout.fragment_audio)
public class AudioFragment extends BaseFragment {
    public AudioManager audioManager;
    ComponentName mComponentName;

    @Override
    public void initView() {
        audioManager = (AudioManager) baseActivity.getSystemService(Context.AUDIO_SERVICE);
        int current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        BLog.d("   curent volume ==" + current + "  max  volume==" + max);

        mComponentName = new ComponentName(baseActivity.getPackageName(), ControlReceiver.class.getName());

        /**
         * AUDIOFOCUS_GAIN
         指示申请得到的Audio Focus不知道会持续多久，一般是长期占有；
         AUDIOFOCUS_GAIN_TRANSIENT
         指示要申请的AudioFocus是暂时性的，会很快用完释放的；
         AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK不但说要申请的AudioFocus是暂时性的，还指示当前正在使用AudioFocus的可以继续播放，只是要“duck”一下（降低音量）。
         */
        int request = audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        BLog.d("  request==" + request);
        if (request == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            BLog.d("    申请成功  registerMediaButtonEventReceiver");
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                //注册媒体按键 API 21+（Android 5.0）
                setMediaButtonEvent();
            } else {
                //注册媒体按键 API 21 以下， 通常的做法
                audioManager.registerMediaButtonEventReceiver(mComponentName);
            }
        } else if (request == AudioManager.AUDIOFOCUS_REQUEST_FAILED) {
            BLog.d("    申请失败");
        }

    }

    MediaSession session;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setMediaButtonEvent() {
        session = new MediaSession(baseActivity, "随便写一串 tag 就行");
        session.setCallback(new MediaSession.Callback() {
            @Override
            public void onPlay() {
                super.onPlay();
                //这里处理播放器逻辑 播放
                updatePlaybackState(true);//播放暂停更新控制中心播放状态
            }

            @Override
            public void onPause() {
                super.onPause();
                //这里处理播放器逻辑 暂停
                updatePlaybackState(false);//播放暂停更新控制中心播放状态
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                //CMD NEXT 这里处理播放器逻辑 下一曲
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                //这里处理播放器逻辑 上一曲
            }
        });
        session.setActive(true  );
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void unRegisterMediaButton() {
//        |mComponentName==null
        if (audioManager == null) return;

        audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        audioManager.unregisterMediaButtonEventReceiver(mComponentName);
        if (session != null) {
            session.setCallback(null);
            session.setActive(false);
            session.release();
        }
    }

    /*
    * update mediaCenter state
    */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void updatePlaybackState(boolean isPlaying) {
        PlaybackState.Builder stateBuilder = new PlaybackState.Builder()
                .setActions(PlaybackState.ACTION_PLAY
                        | PlaybackState.ACTION_PLAY_PAUSE
                        | PlaybackState.ACTION_PLAY_FROM_MEDIA_ID
                        | PlaybackState.ACTION_PAUSE
                        | PlaybackState.ACTION_SKIP_TO_NEXT
                        | PlaybackState.ACTION_SKIP_TO_PREVIOUS
                        | PlaybackState.CONTENTS_FILE_DESCRIPTOR);
        if (isPlaying) {
            stateBuilder.setState(PlaybackState.STATE_PLAYING,
                    PlaybackState.PLAYBACK_POSITION_UNKNOWN,
                    SystemClock.elapsedRealtime());
        } else {
            stateBuilder.setState(PlaybackState.STATE_PAUSED,
                    PlaybackState.PLAYBACK_POSITION_UNKNOWN,
                    SystemClock.elapsedRealtime());
        }
        session.setPlaybackState(stateBuilder.build());
    }

    //点击播放 注册监听后调用此方法才能将媒体焦点抢过来
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void updateMediaCenterInfo(String title, String artist) {
        if (session == null) return;
        MediaMetadata.Builder metadataBuilder = new MediaMetadata.Builder();
        metadataBuilder.putString(MediaMetadata.METADATA_KEY_TITLE, title);//歌曲名
        metadataBuilder.putString(MediaMetadata.METADATA_KEY_ARTIST, artist);//歌手
        session.setMetadata(metadataBuilder.build());
        updatePlaybackState(true);
    }


    /**
     * AUDIOFOCUS_GAIN：获得了Audio Focus；
     * AUDIOFOCUS_LOSS：失去了Audio Focus，并将会持续很长的时间。这里因为可能会停掉很长时间，所以不仅仅要停止Audio的播放，最好直接释放掉Media资源。而因为停止播放Audio的时间会很长，如果程序因为这个原因而失去AudioFocus，最好不要让它再次自动获得AudioFocus而继续播放，不然突然冒出来的声音会让用户感觉莫名其妙，感受很不好。这里直接放弃AudioFocus，当然也不用再侦听远程播放控制【如下面代码的处理】。要再次播放，除非用户再在界面上点击开始播放，才重新初始化Media，进行播放。
     * AUDIOFOCUS_LOSS_TRANSIENT：暂时失去Audio Focus，并会很快再次获得。必须停止Audio的播放，但是因为可能会很快再次获得AudioFocus，这里可以不释放Media资源
     * AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK：暂时失去AudioFocus，但是可以继续播放，不过要在降低音量。
     */
    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                //短暂性丢失焦点，当其他应用申请AUDIOFOCUS_GAIN_TRANSIENT或AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE时，
                //会触发此回调事件，例如播放短视频，拨打电话等。
                //通常需要暂停音乐播放
                // Pause playback
                BLog.d("  AudioManager.AUDIOFOCUS_LOSS_TRANSIENT");
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                //释放焦点，该方法可根据需要来决定是否调用
                //若焦点释放掉之后，将不会再自动获得
                BLog.d("   AudioManager.AUDIOFOCUS_LOSS");
                audioManager.unregisterMediaButtonEventReceiver(mComponentName);
                audioManager.abandonAudioFocus(afChangeListener);
                unRegisterMediaButton();
                // Stop playback
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                BLog.d("    AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                //短暂性丢失焦点并作降音处理
                // Lower the volume
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                //当其他应用申请焦点之后又释放焦点会触发此回调
                //可重新播放音乐
                BLog.d("    AudioManager.AUDIOFOCUS_GAIN");
                // Resume playback or Raise it back to normal
            }
        }
    };

    AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        int aband = audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        BLog.d("  aband==" + aband);
    }

    /**
     * streamType音源类型，在AudioManager中定义
     * 通话
     * public static final int STREAM_VOICE_CALL = AudioSystem.STREAM_VOICE_CALL;
     * 系统声音
     * public static final int STREAM_SYSTEM = AudioSystem.STREAM_SYSTEM;
     * 铃声
     * public static final int STREAM_RING = AudioSystem.STREAM_RING;
     * 音乐
     *public static final int STREAM_MUSIC = AudioSystem.STREAM_MUSIC;
     * 闹铃声
     * public static final int STREAM_ALARM = AudioSystem.STREAM_ALARM;
     *  通知音
     *public static final int STREAM_NOTIFICATION = AudioSystem.STREAM_NOTIFICATION;
     *  蓝牙电话
     *public static final int STREAM_BLUETOOTH_SCO = AudioSystem.STREAM_BLUETOOTH_SCO;
     *  强制的系统声音
     * public static final int STREAM_SYSTEM_ENFORCED = AudioSystem.STREAM_SYSTEM_ENFORCED;
     *  DTMF拨号音
     * public static final int STREAM_DTMF = AudioSystem.STREAM_DTMF;
     *   文本识别音
     * public static final int STREAM_TTS = AudioSystem.STREAM_TTS;
     */

}
