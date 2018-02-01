package com.beini.ui.view.decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by beini on 2018/2/1.
 */

public class HomeDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2f);
        int childCount = parent.getLayoutManager().getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int left = child.getLeft() + 40;
            int right = child.getRight() - 40;
            int top = child.getTop();
            int bottom = child.getBottom();
            Rect rect = new Rect(left, top, right, bottom);
            c.drawRect(rect, paint);
        }
    }
}
