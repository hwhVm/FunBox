package com.beini.ui.fragment.video;

import android.annotation.SuppressLint;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Create by beini 2017/3/28
 * 此demo演示如何从Camera中获取实时画面，通过MediaCodec编码成H264格式的视频，并保存成H264视频文件
 */
@ContentView(R.layout.fragment_mediacodec)
public class MediacodecFragment extends BaseFragment implements SurfaceHolder.Callback ,Camera.PreviewCallback {
    @ViewInject(R.id.surface_view)
    SurfaceView surfaceView;

    private SurfaceHolder surfaceHolder;

    private Camera camera;

    private Camera.Parameters parameters;

    int width = 1280;

    int height = 720;

    int framerate = 30;

    int biterate = 8500*1000;

    private static int yuvqueuesize = 10;

    public static ArrayBlockingQueue<byte[]> YUVQueue = new ArrayBlockingQueue<byte[]>(yuvqueuesize);

//    private AvcEncoder avcCodec;


    @Override
    public void initView() {
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        SupportAvcCodec();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = getBackCamera();
        startcamera(camera);
//        avcCodec = new AvcEncoder(width,height,framerate,biterate);
//        avcCodec.StartEncoderThread();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (null != camera) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
//            avcCodec.StopThread();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        putYUVData(data,data.length);
    }
    public void putYUVData(byte[] buffer, int length) {
        if (YUVQueue.size() >= 10) {
            YUVQueue.poll();
        }
        YUVQueue.add(buffer);
    }

    @SuppressLint("NewApi")
    private boolean SupportAvcCodec(){
        if(Build.VERSION.SDK_INT>=18){
            for(int j = MediaCodecList.getCodecCount() - 1; j >= 0; j--){
                MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(j);

                String[] types = codecInfo.getSupportedTypes();
                for (int i = 0; i < types.length; i++) {
                    if (types[i].equalsIgnoreCase("video/avc")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void startcamera(Camera mCamera){
        if(mCamera != null){
            try {
                mCamera.setPreviewCallback(this);
                mCamera.setDisplayOrientation(90);
                if(parameters == null){
                    parameters = mCamera.getParameters();
                }
                parameters = mCamera.getParameters();
                parameters.setPreviewFormat(ImageFormat.NV21);
                parameters.setPreviewSize(width, height);
                mCamera.setParameters(parameters);
                mCamera.setPreviewDisplay(surfaceHolder);
                mCamera.startPreview();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Camera getBackCamera() {
        Camera c = null;
        try {
            c = Camera.open(0); // attempt to get a Camera instance
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c; // returns null if camera is unavailable
    }
}
