package com.example.administrator.baseapp.ui.fragment.broadcast;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
	public MyService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("com.beini", "------------------onStartCommand--------------------->启动服务");

		return START_STICKY;//START_STICKY
	}


}
