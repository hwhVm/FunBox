package com.beini.ui.view.edit;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by beini on 2017/3/15.
 */

public class DropListView extends ListView {
    private int mWidth = 0;


    public DropListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // 重写onMeasure方法 解决默认横向占满屏幕问题
    // 当控件的父元素正要放置该控件时调用.父元素会问子控件一个问题，“你想要用多大地方啊？”，然后传入两个参数——widthMeasureSpec和heightMeasureSpec.
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = getMeasuredHeight();
        for(int i=0;i<getChildCount();i++) {
            int childWidth = getChildAt(i).getMeasuredWidth();
            int childHeight = getChildAt(i).getMeasuredHeight();
            mWidth = Math.max(mWidth, childWidth);
        }

        setMeasuredDimension(mWidth, height);
    }

    /**
     * 设置宽度，如果不设置，则默认包裹内容
     * @param width 宽度
     */
    protected void setListWidth(int width) {
        mWidth = width;
    }
}
