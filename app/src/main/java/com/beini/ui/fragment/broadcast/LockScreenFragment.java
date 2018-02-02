package com.beini.ui.fragment.broadcast;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.util.BLog;

/**
 * Create by beini 2018/2/2
 */
@ContentView(R.layout.fragment_lock_screen)
public class LockScreenFragment extends BaseFragment {


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Intent.ACTION_SCREEN_ON:
                    BLog.e("ACTION_SCREEN_ON");
                    break;
                case Intent.ACTION_SCREEN_OFF:
                    BLog.e("ACTION_SCREEN_OFF");
                    break;
                case Intent.ACTION_USER_PRESENT:
                    BLog.e("ACTION_USER_PRESENT");
                    break;
            }

        }
    };

    @Override
    public void initView() {
        baseActivity.setBottom(View.GONE);
        baseActivity.setTopBar(View.GONE);
        initReceiver();

    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter();
        //动态注册，静态注册没有测试过   PowerManager发出的广播
        filter.addAction(Intent.ACTION_SCREEN_ON); // 屏幕亮屏广播
        filter.addAction(Intent.ACTION_SCREEN_OFF);//屏幕灭屏广播
        filter.addAction(Intent.ACTION_USER_PRESENT);// 屏幕解锁广播
        baseActivity.registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        baseActivity.unregisterReceiver(broadcastReceiver);
    }
}
