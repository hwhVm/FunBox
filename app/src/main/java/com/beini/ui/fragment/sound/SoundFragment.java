package com.beini.ui.fragment.sound;


import android.view.View;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.ndk.NDKMain;

import org.fmod.FMOD;

import java.io.IOException;
import java.io.InputStream;

/**
 * Create by beini 2018/1/30
 */
@ContentView(R.layout.fragment_sound)
public class SoundFragment extends BaseFragment {
    public static final String music_path = "file:///android_asset/demo.wav";

    //音效类型
    public static final int MODE_NORMAL = 0;
    public static final int MODE_LUOLI = 1;
    public static final int MODE_DASHU = 2;
    public static final int MODE_JINGSONG = 3;
    public static final int MODE_GAOGUAI = 4;
    public static final int MODE_KONGLING = 5;
    private NDKMain ndkMain = new NDKMain();

    @Override
    public void initView() {
        baseActivity.setBottom(View.GONE);
        baseActivity.setTopBar(View.GONE);
        FMOD.init(baseActivity);
        try {
            InputStream inputStream = getActivity().getAssets().open("demo.wav");

//            byte[] bytes = new byte[(int) file.length()];

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Event({R.id.btn_click, R.id.btn_click_1, R.id.btn_click_2, R.id.btn_click_3, R.id.btn_click_4, R.id.btn_click_5})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_click:
//                ndkMain.playMusicByType(music_path, MODE_NORMAL);
                ndkMain.playsMusic(music_path);
                break;
            case R.id.btn_click_1:
                ndkMain.playMusicByType(music_path, MODE_LUOLI);
                break;
            case R.id.btn_click_2:
                ndkMain.playMusicByType(music_path, MODE_DASHU);
                break;
            case R.id.btn_click_3:
                ndkMain.playMusicByType(music_path, MODE_JINGSONG);
                break;
            case R.id.btn_click_4:
                ndkMain.playMusicByType(music_path, MODE_GAOGUAI);
                break;
            case R.id.btn_click_5:
                ndkMain.playMusicByType(music_path, MODE_KONGLING);
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        FMOD.close();
    }
}
