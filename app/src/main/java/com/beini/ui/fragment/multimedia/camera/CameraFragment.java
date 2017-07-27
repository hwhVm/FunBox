package com.beini.ui.fragment.multimedia.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.bind.ViewInject;
import com.beini.ui.fragment.multimedia.camera.presenter.MainPresenter;
import com.beini.util.BLog;
import com.beini.util.ToastUtils;
import com.beini.util.WindowUtils;
import com.beini.util.listener.KeyBackListener;
import com.beini.util.listener.OnTouchEventListener;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * create by beini 2017/3/10
 */
@ContentView(R.layout.fragment_camera)
public class CameraFragment extends BaseFragment implements OnTouchEventListener, KeyBackListener {
    private Camera camera;
    private Camera.Parameters parameters = null;//参数设置
    private MainPresenter presenter;

    @ViewInject(R.id.buttonLayout)
    View layout;
    @ViewInject(R.id.surfaceView_camera)
    SurfaceView surfaceView;

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initView() {
        checkPermission(new CheckPermListener() {//申请权限
            @Override
            public void superPermission() {

            }
        }, 0x112, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE});
        //获取设备摄像头个数 ，因为很多手机又双摄
        int cameras = Camera.getNumberOfCameras();
        BLog.e("           cameras== " + cameras);
        presenter = new MainPresenter();
        baseActivity.setBottom(View.GONE);
        // 防止锁屏
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //获取屏幕高度宽度
        DisplayMetrics dm = WindowUtils.returnDM();
        surfaceView.getHolder().setFixedSize(dm.widthPixels, dm.heightPixels); //设置Surface分辨率
        surfaceView.getHolder().setKeepScreenOn(true);// 屏幕常亮
        surfaceView.getHolder().addCallback(new SurfaceCallback());//为SurfaceView的句柄添加一个回调函数
//        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        baseActivity.setOnTouchEventListener(this);

    }

    @Event({R.id.takepicture, R.id.scalePic})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.takepicture:
                BLog.e("      ---------->takepicture ");
                if (camera != null) {
                    camera.takePicture(new Camera.ShutterCallback() {
                        @Override
                        public void onShutter() {// 按下快门瞬间会执行此处代码

                        }
                    }, new Camera.PictureCallback() {//当相机获取原始照片时激发该监听器,返回未经压缩的RAW类型照片
                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {// 此处代码可以决定是否需要保存原始照片信息,返回经过压缩的JPEG类型照片

                        }
                    }, new MyPictureCallback());
                }
                break;
            case R.id.scalePic:
                baseActivity.replaceFragment(ShowPicFragment.class);
                break;
        }
    }


    private final class MyPictureCallback implements Camera.PictureCallback {//当相机获取JPG照片时激发该监听器

        @Override
        public void onPictureTaken(byte[] data, final Camera camera) {
            BLog.e("      onPictureTaken");
            camera.startPreview(); // 拍完照后，重新开始预览
            presenter._picToSd(data)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            ToastUtils.showShortToast(getString(R.string.camera_competele));
                        }
                    });

        }
    }


    private final class SurfaceCallback implements SurfaceHolder.Callback {

        // 拍照状态变化时调用该方法
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            BLog.e("  surfaceChanged   width=" + width + " height= " + height);
            parameters = camera.getParameters(); // 获取各项参数
            /**
             * Camera.Parameters.FLASH_MODE_AUTO 自动模式，当光线较暗时自动打开闪光灯；
             *  Camera.Parameters.FLASH_MODE_OFF 关闭闪光灯；
             *  Camera.Parameters.FLASH_MODE_ON 拍照时闪光灯；
             *  Camera.Parameters.FLASH_MODE_RED_EYE 闪光灯参数，防红眼模式，科普一下：防红眼；
             */
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
            /**
             * Camera.Parameters.FOCUS_MODE_AUTO 自动对焦模式，摄影小白专用模式；
             *Camera.Parameters.FOCUS_MODE_FIXED 固定焦距模式，拍摄老司机模式；
             *Camera.Parameters.FOCUS_MODE_EDOF 景深模式，文艺女青年最喜欢的模式；
             *Camera.Parameters.FOCUS_MODE_INFINITY 远景模式，拍风景大场面的模式；
             *Camera.Parameters.FOCUS_MODE_MACRO 微焦模式，拍摄小花小草小蚂蚁专用模式；
             */
            parameters.setFlashMode(Camera.Parameters.FOCUS_MODE_AUTO);
            /**
             * 场景模式配置参数，可以通过Parameters.getSceneMode()接口获取：
             * Camera.Parameters.SCENE_MODE_BARCODE 扫描条码场景，NextQRCode项目会判断并设置为这个场景；
             * Camera.Parameters.SCENE_MODE_ACTION 动作场景，就是抓拍跑得飞快的运动员、汽车等场景用的；
             *Camera.Parameters.SCENE_MODE_AUTO 自动选择场景；
             *Camera.Parameters.SCENE_MODE_HDR 高动态对比度场景，通常用于拍摄晚霞等明暗分明的照片；
             * Camera.Parameters.SCENE_MODE_NIGHT 夜间场景；
             * 在一些机型上可能是没有的自动对焦（虽然比较少见），需要对这种情况进行处理。
             */
            parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);


            parameters.setPictureFormat(ImageFormat.JPEG); // 设置图片格式
            parameters.setPreviewSize(width, height); // 设置预览大小
            parameters.setPreviewFpsRange(20, 30);  //设置每秒显示帧
            parameters.setPictureSize(width, height); // 设置保存的图片尺寸
            parameters.setJpegQuality(100); // 设置照片质量1-100
        }

        // 开始拍照时调用该方法
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            BLog.e("    surfaceCreated");
            try {
                camera = getCamera(0); // 打开摄像头
                if (camera == null) {
                    ToastUtils.showShortToast("打开摄像头失败");
                    return;
                }
                camera.setPreviewDisplay(holder); // 设置用于显示拍照影像的SurfaceHolder对象
                camera.setDisplayOrientation(getPreviewDegree(baseActivity));
                camera.setPreviewCallback(new Camera.PreviewCallback() {
                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera) {

                    }
                });
                camera.startPreview(); // 开始预览
                camera.autoFocus(new Camera.AutoFocusCallback() {//自动对焦
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        // 停止拍照时调用该方法
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            BLog.e("   surfaceDestroyed ");
            if (camera != null) {
                holder.removeCallback(this);
                camera.setPreviewCallback(null);
                camera.stopPreview();
                camera.lock();
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

    public Camera getCamera(int cameraId) {
        try {
            return Camera.open(cameraId);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 判断是否有相机
     *
     * @param ctx
     * @return
     */
    public static boolean hasCameraDevice(Context ctx) {
        return ctx.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * -判断是否支持自动对焦
     *
     * @param params
     * @return
     */
    public static boolean isAutoFocusSupported(Camera.Parameters params) {
        List<String> modes = params.getSupportedFocusModes();
        return modes.contains(Camera.Parameters.FOCUS_MODE_AUTO);
    }

}
