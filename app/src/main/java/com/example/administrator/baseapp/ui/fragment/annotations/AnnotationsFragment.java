package com.example.administrator.baseapp.ui.fragment.annotations;


import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.base.BaseFragment;
import com.example.administrator.baseapp.bind.ContentView;
import com.example.administrator.baseapp.bind.Event;
import com.example.administrator.baseapp.bind.ViewInject;
import com.example.administrator.baseapp.utils.BLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Create by beini  2017/3/24.
 * http://mobile.51cto.com/android-527752.htm
 */
@ContentView(R.layout.fragment_annotations)
public class AnnotationsFragment extends BaseFragment {


    @Override
    public void initView() {

    }

    @Event(R.id.btn_send_ann)
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_send_ann:
                BLog.d("     btn_send_ann       ");
                String str="dd";
                mSubscribe(str);
                break;
        }
    }


    public void mSubscribe(@NonNull String evetn) {
        BLog.d("       DemoEvetn   evetn=" + evetn);
    }


}
