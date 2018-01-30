package com.beini.ui.fragment.sound;


import android.view.View;
import android.widget.Button;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.bind.ViewInject;
import com.beini.ndk.NDKMain;

import org.fmod.FMOD;

/**
 * Create by beini 2018/1/30
 */
@ContentView(R.layout.fragment_sound)
public class SoundFragment extends BaseFragment {
    @ViewInject(R.id.btn_play_fmod_sound)
    Button btn_play_fmod_sound;

    @Override
    public void initView() {
        baseActivity.setBottom(View.GONE);
        baseActivity.setTopBar(View.GONE);
        FMOD.init(baseActivity);
    }

    @Event(R.id.btn_play_fmod_sound)
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_play_fmod_sound:
                NDKMain ndkMain = new NDKMain();
                ndkMain.playsMusic("");
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FMOD.close();
    }
}
