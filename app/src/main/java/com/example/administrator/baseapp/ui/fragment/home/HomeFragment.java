package com.example.administrator.baseapp.ui.fragment.home;


import android.Manifest;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.adapter.HomeListAdapter;
import com.example.administrator.baseapp.base.BaseFragment;
import com.example.administrator.baseapp.bean.BaseBean;
import com.example.administrator.baseapp.bind.ContentView;
import com.example.administrator.baseapp.bind.ViewInject;
import com.example.administrator.baseapp.ui.ani.AniFragment;
import com.example.administrator.baseapp.ui.fragment.aidl.AIDLFragment;
import com.example.administrator.baseapp.ui.fragment.audio.AudioFragment;
import com.example.administrator.baseapp.ui.fragment.bluetooth.SppFragment;
import com.example.administrator.baseapp.ui.fragment.broadcast.BroadcastFragment;
import com.example.administrator.baseapp.ui.fragment.camera.CameraFragment;
import com.example.administrator.baseapp.ui.fragment.localmusic.LocalMusicFragment;
import com.example.administrator.baseapp.ui.fragment.music.MusicFragment;
import com.example.administrator.baseapp.ui.fragment.notification.NotificationFragment;
import com.example.administrator.baseapp.ui.fragment.popupwindow.PopupWindowFragment;
import com.example.administrator.baseapp.ui.fragment.recording.VoiceFragment;
import com.example.administrator.baseapp.ui.fragment.service.ServiceFragment;
import com.example.administrator.baseapp.ui.fragment.sideslip.SideslipFragment;
import com.example.administrator.baseapp.ui.fragment.shake.ShakeFragment;
import com.example.administrator.baseapp.ui.fragment.sms.SmsFragment;
import com.example.administrator.baseapp.ui.fragment.zxing.ZxingFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beini on 2017/2/9.
 */
@ContentView(R.layout.fragment_home)
public class HomeFragment extends BaseFragment {
    @ViewInject(R.id.recycler_view_home)
    RecyclerView recycler_view_home;
    private List<String> functionList = new ArrayList<>();
    String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    public void initView() {
        checkPermissionMethod(perms, " ask  camera permission", 22);
        functionList.add("dlna demo");
        functionList.add("recording");
        functionList.add("Shake");
        functionList.add("Sideslip");
        functionList.add("Local  Music");
        functionList.add("Carmera");
        functionList.add("spp ble");
        functionList.add("sms");
        functionList.add("QR code");
        functionList.add("notification");
        functionList.add("broadcast");
        functionList.add("ani");
        functionList.add("AudioFragment");
        functionList.add("AIDL");
        functionList.add("service  bind unbind  start  stop");
        functionList.add("PopupWindowFragment");
        baseActivity.setBottom(View.VISIBLE);
        baseActivity.setTopBar(View.GONE);
        recycler_view_home.setLayoutManager(new LinearLayoutManager(baseActivity));
        HomeListAdapter homeListAdapter = new HomeListAdapter(new BaseBean<>(R.layout.item_home, functionList));
        recycler_view_home.setAdapter(homeListAdapter);
        homeListAdapter.setItemClick(onItemClickListener);

    }

    public HomeListAdapter.OnItemClickListener onItemClickListener = new HomeListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            switch (position) {
                case 0:
                    baseActivity.replaceFragment(MusicFragment.class);
                    break;
                case 1:
                    baseActivity.replaceFragment(VoiceFragment.class);
                    break;
                case 2:
                    baseActivity.replaceFragment(ShakeFragment.class);
                    break;
                case 3:
                    baseActivity.replaceFragment(SideslipFragment.class);
                    break;
                case 4:
                    baseActivity.replaceFragment(LocalMusicFragment.class);
                    break;
                case 5:
                    baseActivity.replaceFragment(CameraFragment.class);
                    break;
                case 6:
                    baseActivity.replaceFragment(SppFragment.class);
                    break;
                case 7:
                    baseActivity.replaceFragment(SmsFragment.class);
                    break;
                case 8:
                    baseActivity.replaceFragment(ZxingFragment.class);
                    break;
                case 9:
                    baseActivity.replaceFragment(NotificationFragment.class);
                    break;
                case 10:
                    baseActivity.replaceFragment(BroadcastFragment.class);
                    break;
                case 11:
                    baseActivity.replaceFragment(AniFragment.class);
                    break;
                case 12:
                    baseActivity.replaceFragment(AudioFragment.class);
                    break;
                case 13:
                    baseActivity.replaceFragment(AIDLFragment.class);
                    break;
                case 14:
                    baseActivity.replaceFragment(ServiceFragment.class);
                    break;
                case 15:
                    baseActivity.replaceFragment(PopupWindowFragment.class);
                    break;
            }
        }
    };


}
