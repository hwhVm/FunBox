package com.beini.imageload;

import android.graphics.drawable.Animatable;
import android.net.Uri;

import com.beini.util.BLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Created by beini on 2017/3/15.
 * 加载gif 需要依赖: compile 'com.facebook.fresco:animated-gif:0.10.0'
 */

public class FrescoUtil {
    public static void loadPic( SimpleDraweeView draweeView,Uri uri ) {
//        Uri uri = Uri.parse("http://img4.imgtn.bdimg.com/it/u=4236942158,2307642402&fm=21&gp=0.jpg");
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setAutoRotateEnabled(true)
                .build();
        draweeView.setImageURI(uri);
    }

    public static void loadGif(SimpleDraweeView mSimpleDraweeView,Uri uri) {
//        Uri uri = Uri.parse("http://img.zcool.cn/community/01f08155b607c032f875251fc81d45.gif");
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .setControllerListener(listener)
                .build();

        mSimpleDraweeView.setController(controller);
    }


    static ControllerListener listener = new BaseControllerListener() {
        @Override
        public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {
            super.onFinalImageSet(id, imageInfo, animatable);
            BLog.d("onFinalImageSet---");
//				if (animatable != null) {
//					animatable.start();
//				}
        }

        @Override
        public void onFailure(String id, Throwable throwable) {
            super.onFailure(id, throwable);
            BLog.d("onFailure--- id==" + id + "throwable==" + throwable.toString());
        }

        @Override
        public void onIntermediateImageFailed(String id, Throwable throwable) {
            super.onIntermediateImageFailed(id, throwable);
            BLog.d("onIntermediateImageFailed---");
        }
    };
}
