package com.beini.ui.fragment.video;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.bind.ViewInject;

import java.io.IOException;

/**
 * Create by beini 2017/3/22
 * http://www.tuicool.com/articles/YvMFjyv
 */
@ContentView(R.layout.fragment_video)
public class VideoFragment extends BaseFragment {
    @ViewInject(R.id.surface_view_video)
    private SurfaceView sv_media_surface;
    private MediaRecorder mediaRecorder;
    private String filePath;
    private MediaPlayer media;

    @Override
    public void initView() {
        //实例化媒体录制器
        mediaRecorder = new MediaRecorder();
        sv_media_surface.getHolder().addCallback(new SurfaceCallback());

        media = new MediaPlayer();
    }

    public final class SurfaceCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

            //设置格式
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            //设置保存路径
            filePath = Environment.getExternalStorageDirectory()
                    + "/APIC/" + System.currentTimeMillis() + ".mp4";
            mediaRecorder.setOutputFile(filePath);
            mediaRecorder.setPreviewDisplay(sv_media_surface.getHolder().getSurface());

            media.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

    @Event({R.id.btn_start_video, R.id.btn_stop_video, R.id.btn_play_video,R.id.btn_video_view})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_start_video:
                start();
                break;
            case R.id.btn_stop_video:
                stop();
                break;
            case R.id.btn_play_video:
                play();
                break;
            case R.id.btn_video_view:
                baseActivity.replaceFragment(VideoViewFragment.class);
                break;

        }
    }

    private void play() {
        try {
            media.setDataSource(filePath);//这里
            media.prepare();
            media.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
}
