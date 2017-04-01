package com.example.administrator.baseapp.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

/**
 * Created by beini on 2017/3/29.
 * <service android:name=".JobSchedulerService"
 * android:permission="android.permission.BIND_JOB_SERVICE" />
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MJobService extends JobService {
    private Handler mJobHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText(getApplicationContext(),
                    "JobService task running", Toast.LENGTH_SHORT)
                    .show();
            jobFinished((JobParameters) msg.obj, false);
            return true;
        }

    });

    @Override
    public boolean onStartJob(JobParameters params) {//任务开始
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

}
