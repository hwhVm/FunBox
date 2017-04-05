package com.beini.ui.fragment.notification;

import android.support.v4.app.Fragment;
import android.view.View;

import com.beini.R;
import com.beini.base.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;



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
