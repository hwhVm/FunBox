package com.beini.ui.fragment.camera;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.beini.R;
import com.beini.base.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.bind.ViewInject;
import com.beini.ui.fragment.camera.presenter.MainPresenter;
import com.beini.util.BLog;
import com.beini.util.SnackbarUtil;
import com.beini.util.listener.KeyBackListener;
import com.beini.util.listener.OnTouchEventListener;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * create by beini 2017/3/10
 */
@ContentView(R.layout.fragment_camera)
public class CameraFragment extends BaseFragment implements OnTouchEventListener, KeyBackListener {
    private Camera camera;
    private Camera.Parameters parameters = null;

    MainPresenter presenter;
    @ViewInject(R.id.buttonLayout)
    View layout;
    @ViewInject(R.id.surfaceView)
    SurfaceView surfaceView;
    @ViewInject(R.id.layout_tack_pic_index)
    FrameLayout layout_tack_pic_index;

    @Override
    public void initView() {
        presenter = new MainPresenter();
        baseActivity.setBottom(View.GONE);
        surfaceView.getHolder().setFixedSize(176, 144); //设置Surface分辨率
        surfaceView.getHolder().setKeepScreenOn(true);// 屏幕常亮
        surfaceView.getHolder().addCallback(new SurfaceCallback());//为SurfaceView的句柄添加一个回调函数
        baseActivity.setOnTouchEventListener(this);
//        checkPermissionMethod(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, "ff", 44);
    }

    @Event({R.id.takepicture, R.id.scalePic})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.takepicture:
                if (camera != null) {
                    camera.takePicture(null, null, new MyPictureCallback());
                }
                break;
            case R.id.scalePic:
                baseActivity.replaceFragment(ShowPicFragment.class);
                break;
        }
    }


    private final class MyPictureCallback implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(byte[] data, final Camera camera) {
            camera.startPreview(); // 拍完照后，重新开始预览
            presenter._picToSd(data)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            SnackbarUtil.showShort(layout_tack_pic_index, "complete ");
                        }
                    });

        }
    }


    private final class SurfaceCallback implements SurfaceHolder.Callback {

        // 拍照状态变化时调用该方法
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            parameters = camera.getParameters(); // 获取各项参数
            parameters.setPictureFormat(PixelFormat.JPEG); // 设置图片格式
            parameters.setPreviewSize(width, height); // 设置预览大小
            parameters.setPreviewFpsRange(0,1000);  //设置每秒显示4帧
            parameters.setPictureSize(width, height); // 设置保存的图片尺寸
            parameters.setJpegQuality(80); // 设置照片质量
        }

        // 开始拍照时调用该方法
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera = Camera.open(); // 打开摄像头
                camera.setPreviewDisplay(holder); // 设置用于显示拍照影像的SurfaceHolder对象
                camera.setDisplayOrientation(getPreviewDegree(baseActivity));
                camera.startPreview(); // 开始预览
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        // 停止拍照时调用该方法
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (camera != null) {
                camera.release(); // 释放照相机
                camera = null;
            }
        }
    }

    /**
     * 点击手机屏幕是，显示两个按钮
     */
    @Override
    public void onTouchEvent(MotionEvent event) {
        BLog.d("  event.getAction()= " + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                layout.setVisibility(ViewGroup.VISIBLE);
                break;
        }
    }


    @Override
    public void keyBack() {

    }

    @Override
    public void onKeyDown(KeyEvent event) {
        if (event.getAction() == KeyEvent.KEYCODE_CAMERA) {
            if (camera != null && event.getRepeatCount() == 0) {
                // 拍照
                //注：调用takePicture()方法进行拍照是传入了一个PictureCallback对象——当程序获取了拍照所得的图片数据之后
                //，PictureCallback对象将会被回调，该对象可以负责对相片进行保存或传入网络
                camera.takePicture(null, null, new MyPictureCallback());
            }
        }

    }

    // 提供一个静态方法，用于根据手机方向获得相机预览画面旋转的角度
    public static int getPreviewDegree(Activity activity) {
        // 获得手机的方向
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degree = 0;
        // 根据手机的方向计算相机预览画面应该选择的角度
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }
        return degree;
    }
}
