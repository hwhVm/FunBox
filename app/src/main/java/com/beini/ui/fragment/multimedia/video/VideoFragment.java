package com.beini.ui.fragment.multimedia.video;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.bind.ViewInject;
import com.beini.constants.Constants;
import com.beini.util.BLog;

import java.io.File;
import java.io.IOException;

/**
 * Create by beini 2017/3/22
 * 系统解析视频格式--->得到视频编码---->对视频编码进行解码---->得到一帧一帧的图像--->在画布上显示
 */
@ContentView(R.layout.fragment_video)
public class VideoFragment extends BaseFragment {
    @ViewInject(R.id.surface_view_video)
    private SurfaceView sv_media_surface;//可以直接从内存或者是DMA硬件里获取图像数据...将这些图像数据迅速的显示出来，通过启动另外一个线程可以迅速的完成画面的更新操作，不会导致主线程阻塞
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

    /**
     * SurfaceView的双缓冲机制是非常消耗资源的，因此Android规定，在SurfaceView可见的时候，SurfaceView会对文件进行解析并显示，
     * 当其不可见时，要直接销毁掉SurfaceView以节省资源
     */
    public final class SurfaceCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {//一般在这里调用画图的线程。然后画图的工作开始
            BLog.e("          surfaceCreated      " + (mediaRecorder == null));
            mediaRecorder.reset();
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

            //设置格式
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            //设置保存路径
            filePath = Constants.URL_ALL_FILE
                    + "/APIC/" + System.currentTimeMillis() + ".mp4";
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            mediaRecorder.setOutputFile(filePath);
            mediaRecorder.setOrientationHint(270);
            mediaRecorder.setPreviewDisplay(sv_media_surface.getHolder().getSurface());

            media.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {//销毁时激发，一般在这里将画图的线程停止、释放。

        }
    }

    @Event({R.id.btn_start_video, R.id.btn_stop_video, R.id.btn_play_video, R.id.btn_video_view})
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
                baseActivity.replaceFragment(VideoListFragment.class);
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
        BLog.e("          surfaceCreated      ");
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
