package com.beini.ui.view.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.beini.util.BLog;

/**
 * Created by beini on 2017/8/21.
 */

public final class CustomerCanvas extends View {

    Paint paint = new Paint();

    /**
     * 通过代码创建view的时候调用这个构造函数
     *
     * @param context
     */
    public CustomerCanvas(Context context) {
        super(context);
    }

    /**
     * 在布局文件里创建view的时候调用这个构造函数
     * @param context
     * @param attrs
     */
    public CustomerCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 在布局文件创建view的时候并且设置了自定义的属性(attribute)
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CustomerCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     *  在布局文件创建view的时候并且,设置了自定义的属性(attribute)或者资源文件
     */
//    public CustomerCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        //圆
//        canvas.drawColor(Color.RED);
//        canvas.drawCircle(100, 100, 50, paint);
        //正方形
//        paint.setColor(Color.BLACK);
//        canvas.drawRect(100, 200, 200, 400, paint);
        //椭圆形
//        paint.setColor(Color.WHITE);黄
//        canvas.drawOval(250,300,400,600,paint);
        //线
//        canvas.drawLine(0, 0, 100, 100, paint);
//        float lines[] = {100,20,200,20, 70,50,230,50, 150,0,150,50};
//        canvas.drawLines(lines,paint);
        //扇形
        canvas.drawArc(100, 200, 300, 300, 0, -90, true, paint);//顺时针
        //重点
//      canvas.drawPath();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        BLog.e("   widthMeasureSpec=" + getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec) + "    heightMeasureSpec=" + getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }
}
