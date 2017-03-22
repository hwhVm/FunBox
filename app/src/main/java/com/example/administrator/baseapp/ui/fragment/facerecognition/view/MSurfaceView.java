package com.example.administrator.baseapp.ui.fragment.facerecognition.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Vector;

/**
 * Created by beini on 2017/3/22.
 */

public class MSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holder=null; //控制对象
    private Vector<Float> xs=new Vector<>();
    private Vector<Float> ys=new Vector<>();

    public MSurfaceView(Context context) {
        super(context);
    }

    public MSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);//这里是绘制背景
        Paint p=new Paint(); //笔触
        p.setAntiAlias(true); //反锯齿
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        for(int i=0;i<xs.size();i++)
            canvas.drawCircle(xs.elementAt(i),ys.elementAt(i),10, p);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            xs.add(event.getX());
            ys.add(event.getY());
        }
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
