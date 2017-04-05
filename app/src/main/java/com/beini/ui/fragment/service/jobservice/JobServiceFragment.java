package com.beini.ui.fragment.service.jobservice;

import android.app.job.JobScheduler;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.beini.R;
import com.beini.base.BaseFragment;
import com.beini.bind.ContentView;

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
