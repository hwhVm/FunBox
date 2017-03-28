package com.example.administrator.baseapp.ui.fragment.screenrecord;


import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.adapter.BaseAdapter;
import com.example.administrator.baseapp.base.BaseApplication;
import com.example.administrator.baseapp.base.BaseFragment;
import com.example.administrator.baseapp.bean.BaseBean;
import com.example.administrator.baseapp.bind.ContentView;
import com.example.administrator.baseapp.bind.Event;
import com.example.administrator.baseapp.bind.ViewInject;
import com.example.administrator.baseapp.utils.BLog;
import com.example.administrator.baseapp.utils.listener.ActivityResultListener;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by beini  2017/3/28
 */
@ContentView(R.layout.fragment_screen_record)
public class ScreenRecordFragment extends BaseFragment implements ActivityResultListener {
    private static final int RECORD_REQUEST_CODE = 101;
    private static final int STORAGE_REQUEST_CODE = 102;
    private static final int AUDIO_REQUEST_CODE = 103;

    private MediaProjectionManager projectionManager;
    private MediaProjection mediaProjection;
    private RecordService recordService;
    @ViewInject(R.id.btn_start_screen)
    private Button btn_start;
    private List<VideoBean> videoBeans;
    @ViewInject(R.id.recycle_view)
    private RecyclerView recycle_view;
    VideoAdapter videoAdapter;

    @Override
    public void initView() {
        projectionManager = (MediaProjectionManager) BaseApplication.getInstance().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            initPermission();
        }
        videoBeans = new ArrayList<>();
        baseActivity.setActivityResultListener(this);

        Intent intent = new Intent(baseActivity, RecordService.class);
        baseActivity.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        Log.d("com.beini", " initView()");
        new Thread() {
            @Override
            public void run() {
                super.run();
                updata();
                baseActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updata();
                        videoAdapter = new VideoAdapter(new BaseBean<>(R.layout.video_item, videoBeans),ScreenRecordFragment.this);
                        recycle_view.setLayoutManager(new LinearLayoutManager(baseActivity));
                        recycle_view.setAdapter(videoAdapter);
                        videoAdapter.setItemClick(onItemClickListener);
                    }
                });

            }
        }.start();
    }

    private void initPermission() {
        if (ContextCompat.checkSelfPermission(baseActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(baseActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
        }
        if (ContextCompat.checkSelfPermission(baseActivity, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(baseActivity, new String[]{Manifest.permission.RECORD_AUDIO}, AUDIO_REQUEST_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void resultCallback(int requestCode, int resultCode, Intent data) {
        BLog.d("              resultCallback ");
        if (requestCode == RECORD_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mediaProjection = projectionManager.getMediaProjection(resultCode, data);
            recordService.setMediaProject(mediaProjection);
            recordService.startRecord();
            btn_start.setText("stop_record");
            BLog.d("                   btn_start.setText(\"stop_record\"); ");
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Event(R.id.btn_start_screen)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_screen:
                BLog.d("            recordService.isRunning()="+recordService.isRunning());
                if (recordService.isRunning()) {
                    recordService.stopRecord();
                    btn_start.setText("start_record");
                    baseActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updata();
                            videoAdapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    Intent captureIntent = projectionManager.createScreenCaptureIntent();
                    baseActivity.startActivityForResult(captureIntent, RECORD_REQUEST_CODE);
                }
                break;
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            DisplayMetrics metrics = new DisplayMetrics();
            baseActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            RecordService.RecordBinder binder = (RecordService.RecordBinder) service;
            recordService = binder.getRecordService();
            recordService.setConfig(metrics.widthPixels, metrics.heightPixels, metrics.densityDpi);
            btn_start.setEnabled(true);
            btn_start.setText(recordService.isRunning() ? "stop_record" : "start_record");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        baseActivity.setActivityResultListener(null);
    }

    public void updata() {
        if (videoBeans != null) {
            videoBeans.clear();
        }
        List<String> strUrl = new ArrayList<>();
        String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "ScreenRecord" + "/";
        File fileFolder = new File(rootDir);
        if (fileFolder.exists()) {
            File[] files = fileFolder.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    if (files[i].getAbsolutePath().contains(".mp4")) {
                        Log.d("com.beini", " files[i].getAbsolutePath()==" + files[i].getAbsolutePath());
                        strUrl.add(files[i].getAbsolutePath());
                        VideoBean videoBean = new VideoBean();
                        videoBean.setUrl(files[i].getAbsolutePath());
                        videoBean.setName(files[i].getName());
                        videoBeans.add(videoBean);
                    }
                }
            }
        }
    }

    VideoAdapter.OnItemClickListener onItemClickListener = new BaseAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            EventBus.getDefault().postSticky(videoBeans.get(position).getUrl());
            baseActivity.replaceFragment(PlayFragment.class);
        }
    };

}
