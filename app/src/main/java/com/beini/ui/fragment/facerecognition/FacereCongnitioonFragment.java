package com.beini.ui.fragment.facerecognition;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.View;
import com.beini.R;
import com.beini.base.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.bind.ViewInject;
import com.beini.ui.fragment.facerecognition.utils.Util;
import com.beini.ui.fragment.facerecognition.view.MImageView;
import com.beini.ui.fragment.facerecognition.view.MSurfaceView;
import com.beini.util.BLog;


/**
 * create by beini  2017/3/22
 * http://blog.csdn.net/yide55/article/details/51164000
 */
@ContentView(R.layout.fragment_facere_congnitioon)
public class FacereCongnitioonFragment extends BaseFragment {
    private Camera camera;
    private Camera.Parameters parameters = null;
    @ViewInject(R.id.surfaceView)
    MSurfaceView surfaceView;
    @ViewInject(R.id.image_test)
    MImageView image_test;
    @Override
    public void initView() {
        SurfaceHolder surface = surfaceView.getHolder();
        surface.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surface.setFixedSize(176, 144); //设置Surface分辨率
        surface.setKeepScreenOn(true);// 屏幕常亮
        surface.addCallback(new SurfaceCallback());//为SurfaceView的句柄添加一个回调函数


    }

    private final class SurfaceCallback implements SurfaceHolder.Callback {

        // 拍照状态变化时调用该方法
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            BLog.e("           surfaceChanged");
            parameters = camera.getParameters(); // 获取各项参数
            parameters.setPictureFormat(PixelFormat.JPEG); // 设置图片格式
            parameters.setPreviewSize(width, height); // 设置预览大小
            parameters.setPreviewFrameRate(5);  //设置每秒显示4帧
            parameters.setPictureSize(width, height); // 设置保存的图片尺寸
            parameters.setJpegQuality(100); // 设置照片质量
            parameters.setZoom(12);//设置放大倍数
//开启闪光灯         parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

        }

        // 开始拍照时调用该方法
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            BLog.e("           surfaceCreated");
            try {
                camera = Camera.open(1); // 打开摄像头
                camera.setPreviewDisplay(holder); // 设置用于显示拍照影像的SurfaceHolder对象
                camera.setDisplayOrientation(Util.getPreviewDegree(baseActivity));
                BLog.d("   getPreviewDegree(baseActivity)=" + Util.getPreviewDegree(baseActivity));
                camera.startPreview(); // 开始预览
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        // 停止拍照时调用该方法
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            BLog.e("           surfaceDestroyed");
            if (camera != null) {
                camera.release(); // 释放照相机
                camera = null;
            }
        }
    }

    @Event(R.id.btn_begin_testing)
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_begin_testing:
                starScan();
                break;
        }
    }


    private void starScan() {
//        if (camera != null) {
//            camera.takePicture(null, null, new MyPictureCallback());
//        }
        image_test.setLine();
    }


    private final class MyPictureCallback implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(byte[] data, final Camera camera) {
            camera.startPreview(); // 拍完照后，重新开始预览
            Bitmap bitmap = Util.rotaingImageView(270, BitmapFactory.decodeByteArray(data, 0, data.length));
//            faceOverlayView.setBitmap(bitmap);
//            surfaceView.setVisibility(View.GONE);
//            camera.startPreview(); // 拍完照后，重新开始预览
//           Util.getFaceData(bitmap,getActivity());
        }
    }

}
