package com.example.administrator.baseapp.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.baseapp.R;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by beini on 2017/3/31.
 */

public class MusicWaveView extends View {

    private float width;//控件宽度
    private float height;//空间高度
    private float rect_w;//柱状图的宽度
    private int rect_t1, rect_t2, rect_t3, rect_t4;
    private int random;//动态变换的高度比例
    private int INVALIDATE = 0x1233;

    private boolean status = true;//控件的状态
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == INVALIDATE) {
                MusicWaveView.this.invalidate();//重绘更新view
            }
        }
    };

    public MusicWaveView(Context context) {
        super(context);
    }

    public MusicWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MusicWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (status == true) {
            // 定义一个定时器 ，让画图每个0.5秒执行一次
            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                    rect_t1 = new Random().nextInt(random);
                    rect_t2 = new Random().nextInt(random);
                    rect_t3 = new Random().nextInt(random);
                    rect_t4 = new Random().nextInt(random);
                    handler.sendEmptyMessage(INVALIDATE);
                }
            }, 500);
        }
        super.onDraw(canvas);

        //画柱状 动态图，就要改变柱状的top值  rect_w:每根柱形宽度，  rect_t1-rect_t4:控件高度

        RectF r1 = new RectF(rect_w, rect_t1 * 4, rect_w * 2, height);

        RectF r2 = new RectF(rect_w * 3, rect_t2 * 4, rect_w * 4, height);

        RectF r3 = new RectF(rect_w * 5, rect_t3 * 4, rect_w * 6, height);

        RectF r4 = new RectF(rect_w * 7, rect_t4 * 4, rect_w * 8, height);

        Paint paint1 = new Paint();
        paint1.setColor(ContextCompat.getColor(getContext(), R.color.app_backound_color));
        paint1.setStyle(Paint.Style.FILL);
        paint1.setAntiAlias(true);//抗锯齿
        canvas.drawRect(r1, paint1);

        canvas.drawRect(r2, paint1);

        canvas.drawRect(r3, paint1);

        canvas.drawRect(r4, paint1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //拿到控件的宽高度,并动态计算柱子的宽度
        height = MeasureSpec.getSize(heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        rect_w = (width / 8);//柱子的宽度
        random = (int) (height / 4);//动态变换的高度比例
    }


}
