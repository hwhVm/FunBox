package com.beini.ui.fragment.multimedia.recording;

import android.Manifest;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.util.BLog;


/**
 * Create by beini 2017/7/10
 */
@ContentView(R.layout.fragment_voice)
public class VoiceFragment extends BaseFragment {

    @Override
    public void initView() {
        checkPermission(new CheckPermListener() {
            @Override
            public void superPermission() {
                BLog.e("       superPermission         ");
            }
        }, 0x11, new String[]{Manifest.permission.RECORD_AUDIO});
    }


}
