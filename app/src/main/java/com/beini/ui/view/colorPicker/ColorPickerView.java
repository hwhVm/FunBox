package com.beini.ui.view.colorPicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.beini.util.BLog;

/**
 * 颜色选择器
 */
public class ColorPickerView extends View {

    private final static int PANEL_SAT_VAL = 0;
    private final static int PANEL_HUE = 1;

    private float mDensity = 1f;

    private OnColorChangedListener mListener;

    private Paint mSatValPaint;
    private Paint mSatValTrackerPaint;
    private Paint mHuePaint;
    private Paint mHueTrackerPaint;

    private Shader mValShader;
    private Shader mHueShader;

    private int mAlpha = 1;
    private float mHue = 360f;
    private float mSat = 0f;
    private float mVal = 0f;

    private int mSliderTrackerColor = 0xff1c1c1c;
    private int mBorderColor = 0xff6E6E6E;

    private int colorStart = 0xffffffff;
    private int colorEnd = 0xff000000;

    private int colorCicular = 0xffdddddd;
    /*
     * 记录最后一个选择的颜色版
     */
    private int mLastTouchedPanel = PANEL_SAT_VAL;

    private RectF mSatValRect;
    private RectF mHueRect;

    private float RECTANGLE_TRACKER_OFFSET = 2f;

    private Point mStartTouchPoint = null;

    private int screenWidth;
    private int screenHeight;
    private int statusBarHeight = 0;

    private int circularWidth = 30;//圆形半径

    public interface OnColorChangedListener {
        void onColorChanged(int color);
    }

    public ColorPickerView(Context context) {
        this(context, null);
    }

    public ColorPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorPickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getRealMetrics(dm);
        screenWidth = dm.widthPixels;// 屏幕宽
        screenHeight = dm.heightPixels;// 屏幕高

        // 获取状态栏高度——方法1
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        BLog.e("        statusBarHeigh=" + statusBarHeight);
        mDensity = dm.density;//获取屏幕密度并初始化三区域各项参数

        RECTANGLE_TRACKER_OFFSET *= mDensity;

        //初始化绘制三区域的画笔
        mSatValPaint = new Paint();
        mSatValTrackerPaint = new Paint();
        mHuePaint = new Paint();
        mHueTrackerPaint = new Paint();

        mSatValTrackerPaint.setStyle(Paint.Style.STROKE);
        mSatValTrackerPaint.setStrokeWidth(2f * mDensity);
        mSatValTrackerPaint.setAntiAlias(true);

        mHueTrackerPaint.setColor(mSliderTrackerColor);
        mHueTrackerPaint.setStyle(Paint.Style.STROKE);
        mHueTrackerPaint.setStrokeWidth(2f * mDensity);
        mHueTrackerPaint.setAntiAlias(true);

        //接收轨迹球运动焦点 设置焦点
        setFocusable(true);
        setFocusableInTouchMode(true);

    }

    private int[] buildHueColorArray() {
        int[] hue = new int[361];

        int count = 0;
//        for (int i = hue.length - 1; i >= 0; i--, count++) {
//            hue[count] = Color.HSVToColor(new float[]{i, 1f, 1f});
        //360 ---> 0
        //0--->360
//        }
        for (int i = 0; i <= 360; i++, count++) {
            hue[count] = Color.HSVToColor(new float[]{i, 1f, 1f});
        }
        return hue;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        drawSatValPanel(canvas);//绘制饱和度选择区域
        drawHuePanel(canvas);//绘制下面色相选择区域

    }

    /**
     * 绘制饱和度选择区域
     *
     * @param canvas
     */
    private void drawSatValPanel(Canvas canvas) {

        final RectF rect = mSatValRect;
        //明度线性渲染器
        if (mValShader == null) {
            mValShader = new LinearGradient(rect.left, rect.top, rect.left, rect.bottom,
                    colorStart, colorEnd, TileMode.CLAMP);
        }
        //HSV转化为RGB
        int rgb = Color.HSVToColor(new float[]{mHue, 1f, 1f});
        //饱和线性渲染器
        Shader mSatShader = new LinearGradient(rect.left, rect.top, rect.right, rect.top,
                colorStart, rgb, TileMode.CLAMP);
        //组合渲染 = 明度线性渲染器 + 饱和线性渲染器
        ComposeShader mShader = new ComposeShader(mValShader, mSatShader, PorterDuff.Mode.MULTIPLY);
        mSatValPaint.setShader(mShader);

        canvas.drawRect(rect, mSatValPaint);

        //初始化选择圆块的位置
        Point p = satValToPoint(mSat, mVal);
        mSatValTrackerPaint.setStyle(Paint.Style.FILL);
        mSatValTrackerPaint.setColor(colorCicular);
        canvas.drawCircle(p.x, p.y - circularWidth, this.circularWidth, mSatValTrackerPaint);

    }

    private Point satValToPoint(float mSat, float mVal) {

        final RectF rect = mSatValRect;
        final float height = rect.height();
        final float width = rect.width();
        Point p = new Point();
        p.x = (int) (mSat * width + rect.left);
        p.y = (int) ((1f - mVal) * height + rect.top);

        return p;
    }

    /**
     * 绘制右侧色相选择区域
     *
     * @param canvas
     */
    private void drawHuePanel(Canvas canvas) {
        final RectF rect = mHueRect;
        //初始化色相线性渲染器 水平方向
        if (mHueShader == null) {
            mHueShader = new LinearGradient(rect.left, rect.top, rect.right, rect.top, buildHueColorArray(), null, TileMode.CLAMP);
            mHuePaint.setShader(mHueShader);
        }
        canvas.drawRect(rect, mHuePaint);

        float rectHeight = 4 * mDensity / 2;
        //初始化色相选择器选择条位置
        Point p = hueToPointX(mHue);

        RectF r = new RectF();
        r.left = p.x - rectHeight;
        r.top = rect.top - RECTANGLE_TRACKER_OFFSET;
        r.right = p.x + rectHeight;
        r.bottom = rect.bottom + RECTANGLE_TRACKER_OFFSET;

        //绘制选择条
//        canvas.drawRoundRect(r, 2, 2, mHueTrackerPaint);
        canvas.drawCircle(p.x, r.top+(r.bottom-r.top)/2-statusBarHeight, 10, mSatValTrackerPaint);
    }

    private Point hueToPointX(float hue) {

        final RectF rect = mHueRect;
        final float width = rect.width();

        Point p = new Point();

//      p.x = (int) (width - (hue * width / 360f) + rect.left);
        p.x = (int) ((hue * width / 360f) + rect.left);
        p.y = (int) rect.top;

        return p;
    }


    private float[] pointToSatVal(float x, float y) {

        final RectF rect = mSatValRect;
        float[] result = new float[2];

        float width = rect.width();
        float height = rect.height();

        if (x < rect.left) {
            x = 0f;
        } else if (x > rect.right) {
            x = width;
        } else {
            x = x - rect.left;
        }

        if (y < rect.top) {
            y = 0f;
        } else if (y > rect.bottom) {
            y = height;
        } else {
            y = y - rect.top;
        }


        result[0] = 1.f / width * x;
        result[1] = 1.f - (1.f / height * y);

        return result;
    }

    private float pointToHueX(float x) {

        final RectF rect = mHueRect;

        float width = rect.width();

        if (x < rect.left) {
            x = 0f;
        } else if (x > rect.right) {
            x = width;
        } else {
            x = x - rect.left;
        }
        return x * 360f / width;
//        return 360f - (x * 360f / width);
    }


    /**
     * 轨迹球的事件处理
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        boolean update = false;//是否需要更新颜色
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            switch (mLastTouchedPanel) {
                case PANEL_SAT_VAL://饱和度&亮度选择区域
                    float sat, val;
                    sat = mSat + x / 50f;
                    val = mVal - y / 50f;

                    if (sat < 0f) {
                        sat = 0f;
                    } else if (sat > 1f) {
                        sat = 1f;
                    }

                    if (val < 0f) {
                        val = 0f;
                    } else if (val > 1f) {
                        val = 1f;
                    }

                    mSat = sat;
                    mVal = val;

                    update = true;
                    break;

                case PANEL_HUE:
                    float hue = mHue - y * 10f;

                    if (hue < 0f) {
                        hue = 0f;
                    } else if (hue > 360f) {
                        hue = 360f;
                    }

                    mHue = hue;

                    update = true;
                    break;
            }

        }
        if (update) {
            if (mListener != null) {
                mListener.onColorChanged(Color.HSVToColor(mAlpha, new float[]{mHue, mSat, mVal}));
            }
            invalidate();
            return true;
        }
        return super.onTrackballEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean update = false;

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                mStartTouchPoint = new Point((int) event.getX(), (int) event.getY());

                update = moveTrackersIfNeeded(event);

                break;

            case MotionEvent.ACTION_MOVE:

                update = moveTrackersIfNeeded(event);

                break;

            case MotionEvent.ACTION_UP:

                mStartTouchPoint = null;

                update = moveTrackersIfNeeded(event);

                break;

        }

        if (update) {

            if (mListener != null) {
                mListener.onColorChanged(Color.HSVToColor(mAlpha, new float[]{mHue, mSat, mVal}));
            }

            invalidate();
            return true;
        }


        return super.onTouchEvent(event);
    }

    private boolean moveTrackersIfNeeded(MotionEvent event) {

        if (mStartTouchPoint == null) return false;

        boolean update = false;

        int startX = mStartTouchPoint.x;
        int startY = mStartTouchPoint.y;

        if (mHueRect.contains(startX, startY)) {
            mLastTouchedPanel = PANEL_HUE;
            mHue = pointToHueX(event.getX());
            update = true;
        } else if (mSatValRect.contains(startX, startY)) {
            mLastTouchedPanel = PANEL_SAT_VAL;
            float[] result = pointToSatVal(event.getX(), event.getY());

            mSat = result[0];
            mVal = result[1];

            update = true;
        }
        return update;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * view大小变化时执行
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //初始化饱和度区域
        float leftSat = 0;
        float topSat = 0;
        float bottomSat = screenHeight - statusBarHeight - 200;
        float rightSat = screenWidth;
        mSatValRect = new RectF(leftSat, topSat, rightSat, bottomSat);
        BLog.e(" 初始化饱和度区域    leftSat=" + leftSat + " topSat=" + topSat + " bottomSat=" + bottomSat + " rightSat=" + rightSat);

        float leftHue = leftSat;
        float topHue = bottomSat;
        float bottomHue = screenHeight - statusBarHeight;
        float rightHue = rightSat;
        BLog.e("   leftHue=" + leftHue + "   topHue=" + topHue + "   bottomHue=" + bottomHue + "   rightHue=" + rightHue);
        mHueRect = new RectF(leftHue, topHue, rightHue, bottomHue);

    }

    /**
     * Set a OnColorChangedListener to get notified when the color
     * selected by the user has changed.
     *
     * @param listener
     */
    public void setOnColorChangedListener(OnColorChangedListener listener) {
        mListener = listener;
    }

    /**
     * Set the color of the border surrounding all panels.
     *
     * @param color
     */
    public void setBorderColor(int color) {
        mBorderColor = color;
        invalidate();
    }

    /**
     * Get the color of the border surrounding all panels.
     */
    public int getBorderColor() {
        return mBorderColor;
    }

    /**
     * Get the current color this view is showing.
     *
     * @return the current color.
     */
    public int getColor() {
        return Color.HSVToColor(mAlpha, new float[]{mHue, mSat, mVal});
    }

    /**
     * Set the color the view should show.
     *
     * @param color The color that should be selected.
     */
    public void setColor(int color) {
        setColor(color, false);
    }

    /**
     * Set the color this view should show.
     *
     * @param color    The color that should be selected.
     * @param callback If you want to get a callback to
     *                 your OnColorChangedListener.
     */
    public void setColor(int color, boolean callback) {
        int alpha = Color.alpha(color);
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        mAlpha = alpha;
        mHue = hsv[0];
        mSat = hsv[1];
        mVal = hsv[2];

        if (callback && mListener != null) {
            mListener.onColorChanged(Color.HSVToColor(mAlpha, new float[]{mHue, mSat, mVal}));
        }

        invalidate();
    }


}