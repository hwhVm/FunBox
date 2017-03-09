package com.example.administrator.baseapp.ui.fragment.recording;


import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.base.BaseFragment;
import com.example.administrator.baseapp.bind.ContentView;
import com.example.administrator.baseapp.bind.ViewInject;
import com.example.administrator.baseapp.event.PlayVoiceStatus;
import com.example.administrator.baseapp.ui.fragment.recording.model.VoiceModel;
import com.example.administrator.baseapp.utils.BLog;
import com.example.administrator.baseapp.utils.VoiceUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.subscribers.ResourceSubscriber;


/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_voice)
public class VoiceFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener, View.OnTouchListener {
    private VoiceUtils voice;
    private boolean isStart = false;
    @ViewInject(value = R.id.voice_text)
    TextView voice_text;
    @ViewInject(value = R.id.voice_say)
    ImageView voice_say;
    @ViewInject(value = R.id.voice_mic) ImageView voice_mic;

    @ViewInject(value = R.id.voice_blackspot) ImageView voice_blackspot;
    @ViewInject(value = R.id.soundStyleRadioGroup)
    RadioGroup radioGroup;

    @Override
    public void initView() {
        baseActivity.setBottom(View.GONE);
        EventBus.getDefault().register(this);
        voice = new VoiceUtils();
        checkPermission();
        voice_say.setOnTouchListener(this);
        radioGroup.setOnCheckedChangeListener(this);
    }
    /**
     * 检查App有没录音权限，
     * 没有的话会弹出授系统权对话框。
     */
    private void checkPermission() {
        boolean permission = (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO));// pm.checkPermission(Manifest.permission.RECORD_AUDIO, "packageName"));
        if (permission) {
            voice.Init();

        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (permissions[0] == Manifest.permission.RECORD_AUDIO) {
                voice.Init();
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {


        if (checkedId == R.id.orginalSound) {
            voice.setVariations(0.0f, 0.0f, 0.0f);
//            SPPService.getInstance().write(CmdManager.setTalkMode(true, (byte) 0));
        } else if (checkedId == R.id.manSound) {
            voice.setVariations(0f, -5.0f, -5.0f);
//            SPPService.getInstance().write(CmdManager.setTalkMode(true, (byte) 1));
        } else if (checkedId == R.id.womanSound) {
            voice.setVariations(0f, 5.0f, 5.0f);
//            SPPService.getInstance().write(CmdManager.setTalkMode(true, (byte) 2));
        } else if (checkedId == R.id.babySound) {
            voice.setVariations(-20.0f, 10.0f, 0.0f);
//            SPPService.getInstance().write(CmdManager.setTalkMode(true, (byte) 3));
        }
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                BLog.d("按下录音------------》"+voice.isStartTouch());
                if (voice!=null&&voice.isStartTouch()) {
//                    VoiceModel.setMic(voice_text,voice_mic,voice_say,voice_blackspot,true);
                    isStart = true;

                    voice.Start();
                      Observable.just(true)
                            .delay(50, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new ResourceObserver<Boolean>() {
                                @Override
                                public void onNext(Boolean value) {
                                    BLog.d("    onNext");
                                    endRecordings();
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isStart){
                    endRecordings();
                    isStart=false;
                }

                break;

        }
        return true;
    }

    private void endRecordings(){
        BLog.e("结束录音------->");
        if (voice==null){
            return;
        }
        try {
            Thread.sleep(500);
            isStart=false;
            voice.isRecording=false;
            voice_say.setImageDrawable(getResources().getDrawable(R.mipmap.ysq_btn_dian_n));
            voice_blackspot.setImageDrawable(getResources().getDrawable(R.mipmap.ysq_btn_b_o));
            VoiceModel.setMic(voice_text, voice_mic, voice_say, voice_mic,false);
//            getPlayVoiceStatus();//获取播放状态
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Subscribe
    public void onMessageEvent(PlayVoiceStatus event) {
        BLog.e("播放状态返回"+event.status);
        if (event.status == 1) {

//            SPPService.getInstance().write(CmdManager.setPlayVoice((byte) 1));
            try {
                Thread.sleep(500);
                voice.write();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }else {
//            getPlayVoiceStatus();
        }
    }

}
