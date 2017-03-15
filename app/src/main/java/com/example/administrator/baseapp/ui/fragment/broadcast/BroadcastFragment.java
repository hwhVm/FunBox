package com.example.administrator.baseapp.ui.fragment.broadcast;


import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.base.BaseFragment;
import com.example.administrator.baseapp.bind.ContentView;
import com.example.administrator.baseapp.bind.Event;
import com.example.administrator.baseapp.bind.ViewInject;
import com.example.administrator.baseapp.utils.BLog;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_broadcast)
public class BroadcastFragment extends BaseFragment {


    @Override
    public void initView() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        baseActivity.registerReceiver(receiver, filter);
    }

    boolean isServiceRunning = false;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                Log.d("com.beini", "--------------------------------------------->每分钟触发一次");
                ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                    if ("main.beini.com.notificationapp.MyService".equals(service.service.getClassName())) {
                        BLog.d("    服务正常");
                        isServiceRunning = true;
                    }
                }

                if (!isServiceRunning) {
                    BLog.d("-------服务被杀死，重新启动------>");
                    Intent i = new Intent(context, MyService.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startService(i);
                }

            }
        }
    };

    @Event(R.id.btn_start_my_service)
    private void mEvent(View view) {
        Intent i = new Intent(baseActivity, MyService.class);
       baseActivity.startService(i);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        baseActivity.unregisterReceiver(receiver);
    }
}
