package com.beini.util.listener;

import android.view.View;

import java.util.Calendar;

/**
 * Created by beini on 2017/5/8.
 */
public class AvoidRepetClickListener implements View.OnClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    @Override
    public void onClick(View view) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onAvoidRepetClick(view);
        }
    }

    private void onAvoidRepetClick(View view) {
    }
}
