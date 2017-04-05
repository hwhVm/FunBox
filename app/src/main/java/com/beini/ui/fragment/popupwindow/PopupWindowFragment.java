package com.beini.ui.fragment.popupwindow;

import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beini.R;
import com.beini.base.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.utils.BLog;


/**
 * Create by beini 2017/3/21
 * http://www.webpiaoliang.com/xuexi/wysj/biancheng/269177.html
 */
@ContentView(R.layout.fragment_popup_window)
public class PopupWindowFragment extends BaseFragment implements PopupWindow.OnDismissListener, View.OnClickListener {
    private PopupWindow popupWindow;
    private int navigationHeight;

    @Override
    public void initView() {
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        navigationHeight = getResources().getDimensionPixelSize(resourceId);
        BLog.d("     navigationHeight==="+navigationHeight);
        navigationHeight=15;
    }

    @Event({R.id.btn_open})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_open:
                openPopupWindow(view);
                break;
        }
    }

    private void openPopupWindow(View v) {
        //防止重复按按钮
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        //设置PopupWindow的View
        View view = LayoutInflater.from(baseActivity).inflate(R.layout.view_popupwindow, null);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置背景,这个没什么效果，不添加会报错
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击弹窗外隐藏自身
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置动画
        popupWindow.setAnimationStyle(R.style.PopupWindow);
        //设置位置
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, navigationHeight);
        //设置消失监听
        popupWindow.setOnDismissListener(this);
        //设置PopupWindow的View点击事件
        setOnPopupViewClick(view);
        //设置背景色
        setBackgroundAlpha(0.5f);
    }


    //设置屏幕背景透明效果
    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = baseActivity.getWindow().getAttributes();
        lp.alpha = alpha;
        baseActivity.getWindow().setAttributes(lp);
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1);
    }

    private void setOnPopupViewClick(View view) {
        TextView tv_pick_phone, tv_pick_zone, tv_cancel;
        tv_pick_phone = (TextView) view.findViewById(R.id.tv_pick_phone);
        tv_pick_zone = (TextView) view.findViewById(R.id.tv_pick_zone);
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_pick_phone.setOnClickListener(this);
        tv_pick_zone.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open:
                openPopupWindow(view);
                break;
            case R.id.tv_pick_phone:
                Toast.makeText(baseActivity, "从手机相册选择", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                break;
            case R.id.tv_pick_zone:
                Toast.makeText(baseActivity, "从空间相册选择", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                break;
            case R.id.tv_cancel:
                popupWindow.dismiss();
                break;
        }
    }
}
