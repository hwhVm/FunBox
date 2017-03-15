package com.example.administrator.baseapp.ui.fragment.notification;


import android.support.v4.app.Fragment;
import android.view.View;
import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.base.BaseFragment;
import com.example.administrator.baseapp.bind.ContentView;
import com.example.administrator.baseapp.bind.Event;


/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_notification)
public class NotificationFragment extends BaseFragment {
    @Override
    public void initView() {

    }

    @Event({R.id.btn_send_notification})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_send_notification:
                NotifyUtility.start(baseActivity, null, "ooo", "ffff");
                break;
        }
    }
}
