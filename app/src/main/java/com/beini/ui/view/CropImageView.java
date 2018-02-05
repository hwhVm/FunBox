package com.beini.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CropImageView extends android.support.v7.widget.AppCompatImageView {

    public CropImageView(Context context) {
        super(context);
    }

    public CropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CropImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private Bitmap mOrigBitmap = null;
    private Bitmap mCropBitmap = null;
    private Rect mRect = new Rect(0, 0, 0, 0);

    public void setOrigBitmap(Bitmap orig) {
        mOrigBitmap = orig;
    }

    public Bitmap getOrigBitmap() {
        return mOrigBitmap;
    }

    public Bitmap getCropBitmap() {
        return mCropBitmap;
    }

    public boolean setBitmapRect(Rect rect) {
        if (mOrigBitmap == null) {
            return false;
        }
        if (rect.left < 0 || rect.left > mOrigBitmap.getWidth()) {
            return false;
        }
        if (rect.top < 0 || rect.top > mOrigBitmap.getHeight()) {
            return false;
        }
        if (rect.right <= 0 || rect.left + rect.right > mOrigBitmap.getWidth()) {
            return false;
        }
        if (rect.bottom <= 0 || rect.top + rect.bottom > mOrigBitmap.getHeight()) {
            return false;
        }
        mRect = rect;
        setPadding(mRect.left, mRect.top, 0, 0);
        mCropBitmap = Bitmap.createBitmap(mOrigBitmap,
                mRect.left, mRect.top, mRect.right, mRect.bottom);
        setImageBitmap(mCropBitmap);
        postInvalidate();
        return true;
    }

    public Rect getBitmapRect() {
        return mRect;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mOriginX = event.getX();
                mOriginY = event.getY();
                mOriginRect = mRect;
                mDragMode = getDragMode(mOriginX, mOriginY);
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = (int) (event.getX() - mOriginX);
                int offsetY = (int) (event.getY() - mOriginY);
                Rect rect = null;
                if (mDragMode == DRAG_NONE) {
                    return true;
                } else if (mDragMode == DRAG_WHOLE) {
                    rect = new Rect(mOriginRect.left + offsetX, mOriginRect.top + offsetY, mOriginRect.right, mOriginRect.bottom);
                } else if (mDragMode == DRAG_LEFT) {
                    rect = new Rect(mOriginRect.left + offsetX, mOriginRect.top, mOriginRect.right - offsetX, mOriginRect.bottom);
                } else if (mDragMode == DRAG_RIGHT) {
                    rect = new Rect(mOriginRect.left, mOriginRect.top, mOriginRect.right + offsetX, mOriginRect.bottom);
                } else if (mDragMode == DRAG_TOP) {
                    rect = new Rect(mOriginRect.left, mOriginRect.top + offsetY, mOriginRect.right, mOriginRect.bottom - offsetY);
                } else if (mDragMode == DRAG_BOTTOM) {
                    rect = new Rect(mOriginRect.left, mOriginRect.top, mOriginRect.right, mOriginRect.bottom + offsetY);
                } else if (mDragMode == DRAG_LEFT_TOP) {
                    rect = new Rect(mOriginRect.left + offsetX, mOriginRect.top + offsetY, mOriginRect.right - offsetX, mOriginRect.bottom - offsetY);
                } else if (mDragMode == DRAG_RIGHT_TOP) {
                    rect = new Rect(mOriginRect.left, mOriginRect.top + offsetY, mOriginRect.right + offsetX, mOriginRect.bottom - offsetY);
                } else if (mDragMode == DRAG_LEFT_BOTTOM) {
                    rect = new Rect(mOriginRect.left + offsetX, mOriginRect.top, mOriginRect.right - offsetX, mOriginRect.bottom + offsetY);
                } else if (mDragMode == DRAG_RIGHT_BOTTOM) {
                    rect = new Rect(mOriginRect.left, mOriginRect.top, mOriginRect.right + offsetX, mOriginRect.bottom + offsetY);
                }
                setBitmapRect(rect);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;
    }

    private int DRAG_NONE = 0;
    private int DRAG_WHOLE = 1;
    private int DRAG_LEFT = 2;
    private int DRAG_RIGHT = 3;
    private int DRAG_TOP = 4;
    private int DRAG_BOTTOM = 5;
    private int DRAG_LEFT_TOP = 6;
    private int DRAG_RIGHT_TOP = 7;
    private int DRAG_LEFT_BOTTOM = 8;
    private int DRAG_RIGHT_BOTTOM = 9;

    private int mDragMode = DRAG_NONE;
    private int mInterval = 10;
    private float mOriginX, mOriginY;
    private Rect mOriginRect;

    private int getDragMode(float f, float g) {
        int left = mRect.left;
        int top = mRect.top;
        int right = mRect.left + mRect.right;
        int bottom = mRect.top + mRect.bottom;
        if (Math.abs(f - left) <= mInterval && Math.abs(g - top) <= mInterval) {
            return DRAG_LEFT_TOP;
        } else if (Math.abs(f - right) <= mInterval && Math.abs(g - top) <= mInterval) {
            return DRAG_RIGHT_TOP;
        } else if (Math.abs(f - left) <= mInterval && Math.abs(g - bottom) <= mInterval) {
            return DRAG_LEFT_BOTTOM;
        } else if (Math.abs(f - right) <= mInterval && Math.abs(g - bottom) <= mInterval) {
            return DRAG_RIGHT_BOTTOM;
        } else if (Math.abs(f - left) <= mInterval && g > top + mInterval && g < bottom - mInterval) {
            return DRAG_LEFT;
        } else if (Math.abs(f - right) <= mInterval && g > top + mInterval && g < bottom - mInterval) {
            return DRAG_RIGHT;
        } else if (Math.abs(f - left) <= mInterval && g > top + mInterval && g < bottom - mInterval) {
            return DRAG_LEFT;
        } else if (Math.abs(g - top) <= mInterval && f > left + mInterval && f < right - mInterval) {
            return DRAG_TOP;
        } else if (Math.abs(g - bottom) <= mInterval && f > left + mInterval && f < right - mInterval) {
            return DRAG_BOTTOM;
        } else if (f > left + mInterval && f < right - mInterval
                && g > top + mInterval && g < bottom - mInterval) {
            return DRAG_WHOLE;
        } else {
            return DRAG_NONE;
        }
    }

}
