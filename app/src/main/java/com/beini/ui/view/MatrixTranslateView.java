package com.beini.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.beini.R;


/**
 * Created by beini on 2017/10/18.
 */

public class MatrixTranslateView extends View {
    private Matrix matrix;
    private Bitmap bitmap;
    private Paint paint;

    public MatrixTranslateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        matrix = new Matrix();
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon_serch);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawBitmap(bitmap, matrix, paint);
        //Matrix中以set开头的方法都会把之前的set清除
        //缩放
//        matrix.setScale(-1f, -1f, bitmap.getWidth(), bitmap.getHeight());
//        matrix.setScale(-.5f, -.5f, bitmap.getWidth(), bitmap.getHeight());
//        matrix.setScale(1f, -.5f, bitmap.getWidth(), bitmap.getHeight());
//        matrix.setScale(.3f, .3f);
        //平移
//       matrix.setTranslate(0, bitmap.getHeight() + 150);
        //旋转
        // 以（0，0）为参照点旋转60度
//        matrix.setRotate(60);
//        canvas.drawBitmap(bitmap, matrix, paint);
// 以图片右下角为参照点旋转30度
//        matrix.setRotate(30, bitmap.getWidth(), bitmap.getHeight());
//        canvas.drawBitmap(bitmap, matrix, paint);
// 画布往下平移
//        canvas.save();
//        canvas.translate(0, 2 * bitmap.getHeight());
        //// 旋转角度为负
//mMatrix.setRotate(-30, bitmap.getWidth(), bitmap.getHeight());
//canvas.drawBitmap(bitmap, matrix, paint);
        //错切  水平，垂直错切
        matrix.setSkew(1.5f, 1.2f);
        canvas.drawBitmap(bitmap, matrix, paint);
        bitmap.recycle();
//        角度为正的时候全都是按顺时针方向，如果为负的时候是按照逆时针方向旋转的
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(bitmap.getWidth(), bitmap.getHeight());
    }


}
