package com.beini.ui.fragment.aidl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.util.BLog;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Create by beini  2017/3/20
 */
@ContentView(R.layout.fragment_aidl)
public class AIDLFragment extends BaseFragment {

    @Override
    public void initView() {

    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BLog.d("     onServiceConnected    ");
//            myAIDLService = (MAIDLService) ((MService.Stub) service).getService();
        }
    };

    @Event({R.id.btn_bind, R.id.btn_aidl_add})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_aidl_add:
//                SnackbarUtil.showShort(view,myAIDLService.add(3, 4));
                break;
            case R.id.btn_bind:
                Intent intent = new Intent("MService");
                intent.setPackage("com.beini");
                baseActivity.bindService(intent, connection, BIND_AUTO_CREATE);
                break;

        }

    }
}
