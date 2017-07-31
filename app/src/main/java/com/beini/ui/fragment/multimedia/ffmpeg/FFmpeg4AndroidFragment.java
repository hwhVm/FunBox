package com.beini.ui.fragment.multimedia.ffmpeg;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.VideoView;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.bind.ViewInject;
import com.beini.ui.fragment.multimedia.ffmpeg.model.FFmpegModel;
import com.beini.ui.fragment.multimedia.view.FfmpegMediaController;
import com.beini.ui.fragment.picPicker.GifSizeFilter;
import com.beini.util.BLog;
import com.beini.util.UriUtil;
import com.beini.util.listener.ActivityResultListener;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Create by beini 2017/7/29
 */
@ContentView(R.layout.fragment_ffmpeg4_android)
public class FFmpeg4AndroidFragment extends BaseFragment implements ActivityResultListener {
    @ViewInject(R.id.video_view_ffmpeg)
    VideoView video_view_ffmpeg;
    @ViewInject(R.id.layout_relative_video_view)
    VideoView layout_relative_video_view;


    private final int REQUEST_GALLERY = 0x111;
    private FfmpegMediaController mediaController;
    private String srcPath = null;

    @Override
    public void initView() {
        baseActivity.setActivityResultListener(this);
        mediaController = new FfmpegMediaController(baseActivity);
//        mediaController.setVisibility(View.INVISIBLE);  //隐藏VideoView自带的进度条
        mediaController.setAnchorView(layout_relative_video_view);//指定浮动在某个控件上

        video_view_ffmpeg.setMediaController(mediaController);
        video_view_ffmpeg.requestFocus();
        video_view_ffmpeg.setOnCompletionListener(onCompletionListener);  //防止屏幕锁屏


        mediaController = new FfmpegMediaController(baseActivity);

        video_view_ffmpeg.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });//视频无法播放监听,播放发生错误监听
        video_view_ffmpeg.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //准备完成，比如可以隐藏加载框
            }
        });//加载网络资源黑屏结束监听,/播放前缓冲监听事件，比如加载视频时有加载提示框

    }

    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {

        }
    };


    @Event({R.id.btn_ffmpeg_rotate_90, R.id.btn_choice_form_gallery})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_choice_form_gallery:
                Matisse.from(getActivity())
                        .choose(MimeType.ofVideo(), true)//// 选择 mime 的类型
                        .countable(true)
                        .maxSelectable(1)// 图片选择的最多数量
                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)// 缩略图的比例
                        .imageEngine(new GlideEngine())// 使用的图片加载引擎 GlideEngine
                        .forResult(REQUEST_GALLERY);//// 设置作为标记的请求码
                break;
            case R.id.btn_ffmpeg_rotate_90:
                FFmpegModel.rotateVideo(90, "", srcPath).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String string) throws Exception {
                        FFmpegModel.startVideoView(string, video_view_ffmpeg);
                    }
                });
                break;
        }
    }

    List<Uri> mSelected;

    @Override
    public void resultCallback(int requestCode, int resultCode, Intent data) {
        BLog.e("           onActivityResult     ");
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_GALLERY) {
            mSelected = Matisse.obtainResult(data);
            Uri uri = mSelected.get(0);
            BLog.e("        " + UriUtil.getSDPath(baseActivity, uri));
            srcPath = UriUtil.getSDPath(baseActivity, uri);
            FFmpegModel.startVideoView(UriUtil.getSDPath(baseActivity, uri), video_view_ffmpeg);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        baseActivity.setActivityResultListener(null);
        super.onDestroy();
    }
}
