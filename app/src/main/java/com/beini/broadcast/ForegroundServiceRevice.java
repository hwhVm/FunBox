package com.beini.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.beini.service.ForegroundService;
import com.beini.util.BLog;

/**
 * Created by beini on 2017/7/20.
 */

public class ForegroundServiceRevice extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case ForegroundService.ACTION_CLICK:
                BLog.e("  ForegroundService.ACTION_CLICK    ");
                break;
        }

    }
}
