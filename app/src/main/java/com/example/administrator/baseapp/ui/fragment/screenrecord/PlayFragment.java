package com.example.administrator.baseapp.ui.fragment.screenrecord;


import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.base.BaseFragment;
import com.example.administrator.baseapp.bind.ContentView;
import com.example.administrator.baseapp.bind.ViewInject;
import com.example.administrator.baseapp.ui.fragment.login.LoginFragment;
import com.example.administrator.baseapp.utils.FragmentHelper;

import org.greenrobot.eventbus.EventBus;

/**
 * Create by beini 2017/3/28
 */
@ContentView(R.layout.fragment_play)
public class PlayFragment extends BaseFragment {
    @ViewInject(R.id.videoView1)
    private VideoView videoView1;
    private MediaController mController;

    @Override
    public void initView() {
        mController = new MediaController(baseActivity);
        mController.setVisibility(View.INVISIBLE);  //隐藏VideoView自带的进度条
        videoView1.setMediaController(mController);
        videoView1.requestFocus();
        videoView1.setOnCompletionListener(onCompletionListener);
        String url = EventBus.getDefault().getStickyEvent(String.class);
        Uri uri = Uri.parse(url);
        videoView1.setVideoURI(uri);
        videoView1.start();
    }

    MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            baseActivity.back();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        videoView1.stopPlayback();
        videoView1 = null;
    }
}
