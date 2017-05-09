package com.beini.ui.fragment.popupwindow.mode;

import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beini.R;
import com.beini.ui.fragment.popupwindow.PopupWindowFragment;

/**
 * Created by beini on 2017/4/26.
 */

public class PopuWindwoModel {
    private PopupWindowFragment popupWindowFragment;

    public PopuWindwoModel(PopupWindowFragment popupWindowFragment) {
        this.popupWindowFragment = popupWindowFragment;
    }


    public void openPopupWindow(View v) {
        //防止重复按按钮
        if ( popupWindowFragment.getPopupWindow() != null &&  popupWindowFragment.getPopupWindow().isShowing()) {
            return;
        }
        //设置PopupWindow的View
        View view = LayoutInflater.from(popupWindowFragment.getActivity().getApplicationContext()).inflate(R.layout.view_popupwindow, null);
        popupWindowFragment.setPopupWindow( new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        //设置背景,这个没什么效果，不添加会报错
        popupWindowFragment.getPopupWindow().setBackgroundDrawable(new BitmapDrawable());
        //设置点击弹窗外隐藏自身
        popupWindowFragment.getPopupWindow().setFocusable(true);
        popupWindowFragment.getPopupWindow().setOutsideTouchable(true);
        //设置动画
        popupWindowFragment.getPopupWindow().setAnimationStyle(R.style.PopupWindow);
        //设置位置
        popupWindowFragment.getPopupWindow().showAtLocation(v, Gravity.BOTTOM, 0, popupWindowFragment.getNavigationHeight());
        //设置消失监听
        popupWindowFragment.getPopupWindow().setOnDismissListener(popupWindowFragment);
        //设置PopupWindow的View点击事件
        setOnPopupViewClick(view);
        //设置背景色
//        setBackgroundAlpha(0.5f);
    }

    private void setOnPopupViewClick(View view) {
        TextView tv_pick_phone, tv_pick_zone, tv_cancel;
        tv_pick_phone = (TextView) view.findViewById(R.id.tv_pick_phone);
        tv_pick_zone = (TextView) view.findViewById(R.id.tv_pick_zone);
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_pick_phone.setOnClickListener(popupWindowFragment);
        tv_pick_zone.setOnClickListener(popupWindowFragment);
        tv_cancel.setOnClickListener(popupWindowFragment);
    }


    //设置屏幕背景透明效果
    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = popupWindowFragment.getActivity().getWindow().getAttributes();
        lp.alpha = alpha;
        popupWindowFragment.getActivity().getWindow().setAttributes(lp);
    }




}
