package com.beini.ui.fragment.gesture;

import android.content.Context;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.beini.util.BLog;

/**
 * Created by beini on 2017/6/27.
 */

public class GestureImageView extends android.support.v7.widget.AppCompatImageView {

    private ScaleGestureDetector gestureDetector;
    private Matrix matrix = new Matrix();
    private float scale = 1f;

    public GestureImageView(Context context) {
        super(context);
        gestureDetector = new ScaleGestureDetector(context, new SimpleListener());
    }

    public GestureImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new ScaleGestureDetector(context, new SimpleListener());
    }

    public GestureImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        gestureDetector = new ScaleGestureDetector(this.getContext(), new SimpleListener());
    }

    class SimpleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            BLog.d("       onScaleBegin");
            return super.onScaleBegin(detector);
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale *= detector.getScaleFactor();
            BLog.d(" --------------->onScale");
            scale = Math.max(0.1f, Math.min(scale, 5.0f));
            matrix.setScale(scale, scale);
            setImageMatrix(matrix);
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            BLog.d("------------->onScaleEnd");
            super.onScaleEnd(detector);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        BLog.d("      onTouchEvent");
        gestureDetector.onTouchEvent(event);
        return true;
    }
}
