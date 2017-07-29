package com.beini.ui.fragment.multimedia.camera;


import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.bind.ViewInject;
import com.beini.constants.Constants;
import com.beini.util.BLog;
import com.beini.util.listener.ActivityResultListener;

/**
 * Create by beini 2017/7/26
 * 调用系统
 */
@ContentView(R.layout.fragment_call_system_camera)
public class CallSystemCameraFragment extends BaseFragment implements ActivityResultListener {

    private final String URI = Constants.URL_ALL_FILE;//图片保存地址
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    @ViewInject(R.id.image_get_system_pic)
    ImageView image_get_system_pic;

    @Override
    public void initView() {
        baseActivity.setActivityResultListener(this);
    }


    @Event({R.id.btn_call_camera, R.id.btn_call_video})
    private void mEvent(View view) {
        BLog.e("          URL=" + URI);
        switch (view.getId()) {
            case R.id.btn_call_camera:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// MediaStore.ACTION_IMAGE_CAPTURE// 拍摄照片；  MediaStore.ACTION_VIDEO_CAPTURE //拍摄视频；
                if (intent.resolveActivity(baseActivity.getPackageManager()) != null) {//向该方法传入包管理器可以对包管理器进行查询以确定是否有Activity能够启动该Intent
                    String s = URI + "/temp.png";
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, s);//设置拍摄后返回数据的地址：MediaStore.EXTRA_OUTPUT 参数用于指定拍摄完成后的照片/视频的储存路径。你可以使用Android默认的储存照片目录来保存：
                    baseActivity.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                }//可以做一些提示
                break;
            case R.id.btn_call_video:
                Intent intentVideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (intentVideo.resolveActivity(baseActivity.getPackageManager()) != null) {
                    intentVideo.putExtra(MediaStore.EXTRA_OUTPUT, URI);
                    baseActivity.startActivityForResult(intentVideo, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
                }//可以做一些提示
                break;
        }

    }


    @Override
    public void resultCallback(int requestCode, int resultCode, Intent data) {
        BLog.e("   requestCode=" + requestCode + "     resultCode=" + resultCode + "    ");
        // 如果是拍照
        if (CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE == requestCode && baseActivity.RESULT_OK == resultCode) {
            // Check if the result includes a thumbnail Bitmap
            if (data != null) {
                // 没有指定特定存储路径的时候
                BLog.e("        data is NOT null, file on default position. ");
                // 指定了存储路径的时候（intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);）
                // Image captured and saved to fileUri specified in the
                Toast.makeText(baseActivity, "Image saved to:\n" + data.getData(),
                        Toast.LENGTH_LONG).show();

                if (data.hasExtra("data")) {
                    Bitmap thumbnail = data.getParcelableExtra("data");
                    image_get_system_pic.setImageBitmap(thumbnail);
                }
            }

        }
        //录像
        if (CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE == requestCode && baseActivity.RESULT_OK == resultCode) {
            BLog.e("     ---------------------->录像");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        baseActivity.setActivityResultListener(null);
    }
}
