package com.example.administrator.baseapp.ui.fragment.service.jobservice;


import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.base.BaseFragment;
import com.example.administrator.baseapp.bind.ContentView;
import com.example.administrator.baseapp.service.MJobService;

/**
 * Create by beini 2017/3/29
 */
@ContentView(R.layout.fragment_job)
public class JobServiceFragment extends BaseFragment {


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initView() {
        JobScheduler jobScheduler = (JobScheduler) baseActivity.getSystemService(Context.JOB_SCHEDULER_SERVICE);

    }
}
