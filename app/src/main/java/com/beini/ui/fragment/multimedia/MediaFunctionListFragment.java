package com.beini.ui.fragment.multimedia;


import android.view.View;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.ui.fragment.multimedia.audio.AudioFragment;
import com.beini.ui.fragment.multimedia.audio.SoundPoolFragment;
import com.beini.ui.fragment.multimedia.camera.CallSystemCameraFragment;
import com.beini.ui.fragment.multimedia.camera.Camera2Fragment;
import com.beini.ui.fragment.multimedia.camera.CameraFragment;
import com.beini.ui.fragment.multimedia.localmusic.LocalMusicFragment;
import com.beini.ui.fragment.multimedia.recording.AudioRecordFragment;
import com.beini.ui.fragment.multimedia.recording.VoiceFragment;
import com.beini.ui.fragment.multimedia.video.MediacodecFragment;
import com.beini.ui.fragment.multimedia.video.VideoFragment;
import com.beini.ui.fragment.multimedia.zxing.ZxingFragment;

/**
 * Create by beini 2017/7/26
 * 多媒体功能列表
 */
@ContentView(R.layout.fragment_camera_function_list)
public class MediaFunctionListFragment extends BaseFragment {


    @Override
    public void initView() {

    }

    @Event({R.id.btn_camera_customer, R.id.btn_camera_system, R.id.btn_camera2_system, R.id.btn_media_video, R.id.btn_media_audio, R.id.btn_media_sound_pool,
            R.id.btn_media_mediacodec, R.id.btn_media_record, R.id.btn_media_local_music, R.id.btn_media_qr,R.id.btn_media_record_audiorecord})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_camera_customer:
                baseActivity.replaceFragment(CameraFragment.class);
                break;
            case R.id.btn_camera_system:
                baseActivity.replaceFragment(CallSystemCameraFragment.class);
                break;
            case R.id.btn_camera2_system:
                baseActivity.replaceFragment(Camera2Fragment.class);
                break;
            case R.id.btn_media_video:
                baseActivity.replaceFragment(VideoFragment.class);
                break;
            case R.id.btn_media_mediacodec:
                baseActivity.replaceFragment(MediacodecFragment.class);
                break;

            case R.id.btn_media_audio:
                baseActivity.replaceFragment(AudioFragment.class);
                break;
            case R.id.btn_media_sound_pool:
                baseActivity.replaceFragment(SoundPoolFragment.class);
                break;

            case R.id.btn_media_record:
                baseActivity.replaceFragment(VoiceFragment.class);
                break;
            case R.id.btn_media_local_music:
                baseActivity.replaceFragment(LocalMusicFragment.class);
                break;
            case R.id.btn_media_qr:
                baseActivity.replaceFragment(ZxingFragment.class);
                break;
            case R.id.btn_media_record_audiorecord:
                baseActivity.replaceFragment(AudioRecordFragment.class);
                break;
        }
    }
}
