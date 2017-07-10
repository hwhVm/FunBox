package com.beini.ui.fragment.video;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.bind.ViewInject;

/**
 * Create by beini 2017/3/22
 */

/**
 * 本实例演示如何在Android中播放网络上的视频，这里牵涉到视频传输协议，视频编解码等知识点
 * Android当前支持两种协议来传输视频流一种是Http协议，另一种是RTSP协议
 * Http协议最常用于视频下载等，但是目前还不支持边传输边播放的实时流媒体
 * 同时，在使用Http协议 传输视频时，需要根据不同的网络方式来选择合适的编码方式，
 * 比如对于GPRS网络，其带宽只有20kbps,我们需要使视频流的传输速度在此范围内。
 * 比如，对于GPRS来说，如果多媒体的编码速度是400kbps，那么对于一秒钟的视频来说，就需要20秒的时间。这显然是无法忍受的
 * Http下载时，在设备上进行缓存，只有当缓存到一定程度时，才能开始播放。
 * <p>
 * 所以，在不需要实时播放的场合，我们可以使用Http协议
 * <p>
 * RTSP：Real Time Streaming Protocal，实时流媒体传输控制协议。
 * 使用RTSP时，流媒体的格式需要是RTP。
 * RTSP和RTP是结合使用的，RTP单独在Android中式无法使用的。
 * <p>
 * RTSP和RTP就是为实时流媒体设计的，支持边传输边播放。
 * <p>
 * 同样的对于不同的网络类型（GPRS，3G等），RTSP的编码速度也相差很大。根据实际情况来
 * <p>
 * 使用前面介绍的三种方式，都可以播放网络上的视频，唯一不同的就是URI
 * <p>
 * 本例中使用VideoView来播放网络上的视频
 * <p>
 * int getCurrentPosition()：获取当前播放的位置。
 * int getDuration()：获取当前播放视频的总长度。
 * isPlaying()：当前VideoView是否在播放视频。
 * void pause()：暂停
 * void seekTo(int msec)：从第几毫秒开始播放。
 */
@ContentView(R.layout.fragment_video_view)
public class VideoViewFragment extends BaseFragment {
    @ViewInject(R.id.videoView1)
    VideoView videoView;
    private MediaController mController;
    String localUrl = Environment.getExternalStorageDirectory().getPath() + "/APIC/aa.mp4";
    String netUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";

    @Override
    public void initView() {
        mController = new MediaController(getActivity());
        mController.setVisibility(View.INVISIBLE);  //隐藏VideoView自带的进度条
        videoView.setMediaController(mController);
        videoView.requestFocus();

        videoView.setOnCompletionListener(onCompletionListener);
//        videoView.setOnErrorListener();//视频无法播放监听
//        videoView.setOnInfoListener();//缓冲监听
//        videoView.setOnPreparedListener();//加载网络资源黑屏结束监听
    }

    MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {

        }
    };

    void startPlaying(String url) {
        Uri uri = Uri.parse(url);
        videoView.setVideoURI(uri);
        videoView.start();
    }

    @Event(R.id.btn_start_video_view)
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_start_video_view:
                startPlaying(localUrl);
                break;
        }
    }
}
