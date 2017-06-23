package com.beini.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by beini on 2017/6/23.
 * progressDrawable,thumb属性必须设置
 */

public class ColorSelectBar extends android.support.v7.widget.AppCompatSeekBar {
    private int[] mSelectorArray;
    private int offset;
    private int defaultColor = -15204608;//drawable == null || dot == null给个默认值 绿色

    public ColorSelectBar(Context context) {
        this(context, null);
    }

    public ColorSelectBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorSelectBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getColor() {
        int width, height;
        if (mSelectorArray == null || mSelectorArray.length == 0) {
            try {
                BitmapDrawable drawable = (BitmapDrawable) getProgressDrawable();
                BitmapDrawable dot = (BitmapDrawable) getThumb();

                Bitmap dotmap = dot.getBitmap();
                offset = dotmap.getWidth() / 2;
                Bitmap bitmap = drawable.getBitmap();
                width = bitmap.getWidth();
                height = bitmap.getHeight() / 2;
                mSelectorArray = new int[width];

                for (int i = 0; i < width; i++) {
                    mSelectorArray[i] = bitmap.getPixel(i, height);//获取颜色值
                }

            } catch (NullPointerException e) {
                return defaultColor;
            }
        }

        int index = (int) (((double) (mSelectorArray.length)) / getMax() * getProgress() + offset);

        if (index > (mSelectorArray.length - 1)) {
            return mSelectorArray[mSelectorArray.length - 1];
        }
        return mSelectorArray[index];
    }

    public double matchingColor(int color) {
        double Progress = 0;
        if (mSelectorArray == null) {
            getColor();
        }
        for (int k = 0; k < mSelectorArray.length; k++) {
            if (mSelectorArray[k] == color) {
                Progress = Math.abs((k - offset) / ((double) (mSelectorArray.length) / getMax()));
                break;
            }

        }
        return Progress;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.getParent().requestDisallowInterceptTouchEvent(true);
        super.onTouchEvent(event);
        return true;
    }
}
