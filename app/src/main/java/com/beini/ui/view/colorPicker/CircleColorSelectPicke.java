package com.beini.ui.view.colorPicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.beini.R;
import com.beini.app.GlobalApplication;
import com.beini.util.BLog;
import com.beini.util.Px_DipUtils;

/**
 * Created by beini on 2017/7/24.
 */

public class CircleColorSelectPicke extends View {
    private int color;

    private int coreX, coreY;//圆心坐标
    private int ballRadius;//小白球半径
    private float ballX, ballY;
    private Paint mPaint, ballPaint;
    private Bitmap mBackgroundBitmap;
    private Bitmap swOff, swOn;
    private boolean isOpenSwitch;
    private onSwitchrCallback mSwitchCallback;
    private onColorCallback mCallback;
    private boolean isInitPalette;


    public CircleColorSelectPicke(Context context) {
        super(context);
    }

    public CircleColorSelectPicke(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleColorSelectPicke(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        mPaint = new Paint();
        ballPaint = new Paint();
        ballPaint.setColor(Color.WHITE);
        swOff = BitmapFactory.decodeResource(getResources(), R.mipmap.btn_color_plate_off3x);
        swOn = BitmapFactory.decodeResource(getResources(), R.mipmap.btn_color_plate_on3x);
        mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.btn_color_plate_color3x);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawColorBitmap(canvas);
        drawBall(canvas);
        drawSwitch(canvas);

    }

    /***
     * 画小白球
     * @param canvas
     */
    public void drawBall(Canvas canvas) {
        canvas.drawCircle(ballX, ballY, ballRadius, ballPaint);
    }

    /**
     * 画色环
     *
     * @param canvas
     */
    public void drawColorBitmap(Canvas canvas) {
        Rect src = new Rect();
        src.left = 0;
        src.top = 0;
        src.right = mBackgroundBitmap.getWidth();
        src.bottom = mBackgroundBitmap.getHeight();


        RectF dst = new RectF();
        dst.left = 0;
        dst.top = 0;
        dst.right = mBackgroundBitmap.getWidth();
        dst.bottom = mBackgroundBitmap.getHeight();
        coreX = (int) dst.centerX();
        coreY = (int) dst.centerY();

        canvas.drawBitmap(mBackgroundBitmap, src, dst, mPaint);


    }


    /**
     * 画开关
     *
     * @param canvas
     */
    public void drawSwitch(Canvas canvas) {

        if (isOpenSwitch) {
            canvas.drawBitmap(swOn, coreX - swOn.getWidth() / 2, coreY - swOn.getWidth() / 2, mPaint);

        } else {
            canvas.drawBitmap(swOff, coreX - swOff.getWidth() / 2, coreY - swOff.getWidth() / 2, mPaint);
        }

    }

    private void setPaletteSize() {
        if (ballX == 0) {
            ballX = mBackgroundBitmap.getWidth() / 5;
            ballY = mBackgroundBitmap.getHeight() / 3;
        }


        ballRadius = mBackgroundBitmap.getWidth() / 34;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST && !isInitPalette) {
            setPaletteSize();
            isInitPalette = true;
        }
        setMeasuredDimension(mBackgroundBitmap.getWidth(), mBackgroundBitmap.getHeight());
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (checkDistance((int) event.getX(), (int) event.getY())) {

            ballX = event.getX();
            ballY = event.getY();
            invalidate();
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (Math.sqrt(Math.pow((event.getX() - coreX), 2) + Math.pow(event.getY() - coreY, 2)) < swOff.getHeight() / 2) {
                    isOpenSwitch = (!isOpenSwitch);
                    if (mSwitchCallback != null) {
                        mSwitchCallback.onSwitch(isOpenSwitch);
                    }
                    invalidate();
                } else {


                    color = getColor(event.getX(), event.getY());
                    if (mCallback != null) {
                        mCallback.onColor((int) ballX, (int) ballY, color);
                    }
                }
                break;
        }
        return true;
    }


    public int getColor(float x, float y) {
        if (Math.abs(x) > mBackgroundBitmap.getWidth() - 1) {
            x = mBackgroundBitmap.getWidth() - 1;
        }
        if (Math.abs(y) > mBackgroundBitmap.getHeight() - 1) {
            y = mBackgroundBitmap.getHeight() - 1;
        }
        int color = mBackgroundBitmap.getPixel((int) Math.abs(x), (int) Math.abs(y));

        if (color == 0) {
            for (int i = 0; i < mBackgroundBitmap.getWidth(); i++) {
                if (mBackgroundBitmap.getPixel((int) Math.abs(x - i), (int) Math.abs(y)) != 0) {
                    BLog.e("循环-------------》" + mBackgroundBitmap.getPixel((int) Math.abs(x - i), (int) Math.abs(y)));
                    color = mBackgroundBitmap.getPixel((int) Math.abs(x - i), (int) Math.abs(y));
                    break;
                }
            }
        }
        BLog.e("获取颜色");
        return color;
    }

    public boolean checkDistance(int x, int y) {

        boolean flag = false;
        if (x < coreX && y < coreY) {
            flag = Math.sqrt(Math.pow((x - coreX - ballRadius), 2) + Math.pow(y - coreY - ballRadius, 2)) < mBackgroundBitmap.getWidth() / 2 && (Math.sqrt(Math.pow((x - coreX + ballRadius), 2) + Math.pow(y - coreY + ballRadius, 2)) > Px_DipUtils.dip2px(GlobalApplication.getInstance(), 62));
        } else if (x > coreX && y < coreY) {
            flag = Math.sqrt(Math.pow((x - coreX + ballRadius), 2) + Math.pow(y - coreY - ballRadius, 2)) < mBackgroundBitmap.getWidth() / 2 && (Math.sqrt(Math.pow((x - coreX - ballRadius), 2) + Math.pow(y - coreY + ballRadius, 2)) > Px_DipUtils.dip2px(GlobalApplication.getInstance(), 62));
        } else if (y > coreY && x < coreX) {
            flag = Math.sqrt(Math.pow((x - coreX - ballRadius), 2) + Math.pow(y - coreY + ballRadius, 2)) < mBackgroundBitmap.getWidth() / 2 && (Math.sqrt(Math.pow((x - coreX + ballRadius), 2) + Math.pow(y - coreY - ballRadius, 2)) > Px_DipUtils.dip2px(GlobalApplication.getInstance(), 62));

        } else if (y > coreY && x > coreX) {
            flag = Math.sqrt(Math.pow((x - coreX + ballRadius), 2) + Math.pow(y - coreY + ballRadius, 2)) < mBackgroundBitmap.getWidth() / 2 && (Math.sqrt(Math.pow((x - coreX - ballRadius), 2) + Math.pow(y - coreY - ballRadius, 2)) > Px_DipUtils.dip2px(GlobalApplication.getInstance(), 62));

        }
        return flag;
    }

    public void setColorx(int ballX, int ballY) {
        this.ballX = ballX;
        this.ballY = ballY;
        postInvalidate();
    }


    public interface onColorCallback {
        void onColor(int colorX, int colorY, int color);
    }

    public interface onSwitchrCallback {
        void onSwitch(boolean isOpenSwitch);
    }

    public void setCallback(onColorCallback callback) {
        mCallback = callback;
    }

    public void setSwitchCallback(onSwitchrCallback switchCallback) {
        mSwitchCallback = switchCallback;
    }

    public void setOpenSwitch(boolean openSwitch) {
        isOpenSwitch = openSwitch;
        invalidate();
    }
}
