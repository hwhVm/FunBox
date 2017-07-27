package com.beini.ui.fragment.multimedia.audio;


import android.media.AudioManager;
import android.media.SoundPool;
import android.view.View;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;

/**
 * Create by beini  2017/7/25
 */
@ContentView(R.layout.fragment_sound_pool)
public class SoundPoolFragment extends BaseFragment {
    private SoundPool soundPool;
    private int soundID;

    @Override
    public void initView() {
        // soundPool = new SoundPool.Builder().build();//5.0
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundID = soundPool.load(getActivity(), R.raw.mix_alto, 1);
    }

    @Event(R.id.btn_click_sound_pool)
    private void mEvent(View view) {
        soundPool.play(soundID,
                0.1f,//左声道音量
                0.5f,//右声道音量
                0,//播放优先级 0代表最低优先级
                1,//循环播放 0代表一次 ,-1代表无限,
                1//播放速度 1 正常  范围0-2;
        );
    }
}
