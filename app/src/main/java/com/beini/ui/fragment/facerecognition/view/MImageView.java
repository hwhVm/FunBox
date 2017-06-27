package com.beini.ui.fragment.facerecognition.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.beini.util.BLog;


/**
 * Created by beini on 2017/3/22.
 */

public class MImageView extends View {

    public MImageView(Context context) {
        super(context);
    }

    public MImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Canvas canvas;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
    }

    public void setLine() {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        BLog.e("            " + (paint == null) + "     " + (canvas == null));
        canvas.drawLine(0, 0, 100, 100, paint);
        invalidate();
    }
}
