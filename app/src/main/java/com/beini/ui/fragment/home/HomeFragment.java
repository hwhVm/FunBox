package com.beini.ui.fragment.home;

import android.Manifest;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beini.R;
import com.beini.adapter.HomeListAdapter;
import com.beini.base.BaseFragment;
import com.beini.bean.BaseBean;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;
import com.beini.ui.fragment.aidl.AIDLFragment;
import com.beini.ui.fragment.ani.AniFragment;
import com.beini.ui.fragment.audio.AudioFragment;
import com.beini.ui.fragment.bluetooth.SppFragment;
import com.beini.ui.fragment.broadcast.BroadcastFragment;
import com.beini.ui.fragment.camera.CameraFragment;
import com.beini.ui.fragment.cpptest.CppFragment;
import com.beini.ui.fragment.localmusic.LocalMusicFragment;
import com.beini.ui.fragment.notification.NotificationFragment;
import com.beini.ui.fragment.popupwindow.PopupWindowFragment;
import com.beini.ui.fragment.recording.VoiceFragment;
import com.beini.ui.fragment.service.ServiceFragment;
import com.beini.ui.fragment.shake.ShakeFragment;
import com.beini.ui.fragment.sideslip.SideslipFragment;
import com.beini.ui.fragment.sms.SmsFragment;
import com.beini.ui.fragment.zxing.ZxingFragment;

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
        functionList.add("cpp");
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
                    baseActivity.replaceFragment(CppFragment.class);
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
