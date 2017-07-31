package com.beini.ui.fragment.multimedia.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;

/**
 * Created by beini on 2017/7/31.
 * <p>
 * 一个包含MediaPlayer控制的视图,包含:播放，暂停，倒退,
 */

public class FfmpegMediaController extends MediaController {
    public FfmpegMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FfmpegMediaController(Context context, boolean useFastForward) {
        super(context, useFastForward);
    }

    public FfmpegMediaController(Context context) {
        super(context);
    }

    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int height = View.MeasureSpec.getSize(heightMeasureSpec);
//        int width = View.MeasureSpec.getSize(widthMeasureSpec);
//        setMeasuredDimension(width, 250);
    }
}
