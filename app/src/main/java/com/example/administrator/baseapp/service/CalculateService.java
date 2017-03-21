package com.example.administrator.baseapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import com.example.administrator.baseapp.utils.BLog;

/**
 * Created by beini on 2017/3/21.
 */

public class CalculateService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
