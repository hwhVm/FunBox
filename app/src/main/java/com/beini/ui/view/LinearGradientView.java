package com.beini.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by beini on 2017/6/23.
 */

public class LinearGradientView extends View {


    private LinearGradient linearGradient = null;
    private Paint paint = null;

    public LinearGradientView(Context context) {
        super(context);
    }

    public LinearGradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //控件的宽高固定为256dp
//        setMeasuredDimension((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 256, getResources().getDisplayMetrics()), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 256, getResources().getDisplayMetrics()));
    }

    /**
     * 1 水平方向上渲染不同的颜色 起始的位置为（0，0），结束位置为（getMeasuredWidth(), 0） LinearGradient.TileMode.CLAMP:水平方向上渲染不同的颜色
     * 2 左上角到右下角 起始的位置为（0，0），结束位置为（getMeasuredWidth(),  getMeasuredHeight()）
     * 3 LinearGradient.TileMode.CLAMP模式表示重复最后一种颜色直到该View结束的地方，也就是说从View宽度的1/2处直到View结束的地方都将是蓝色（因为View1/2处的颜色是蓝色）
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        LinearGradient linearGradient = new LinearGradient(0, 0, getMeasuredWidth(), 0, new int[]{Color.RED, Color.WHITE, Color.BLUE}, null, LinearGradient.TileMode.CLAMP);
        paint.setShader(linearGradient);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);
    }
}
