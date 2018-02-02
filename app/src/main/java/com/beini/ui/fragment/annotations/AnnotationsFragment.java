package com.beini.ui.fragment.annotations;

import android.support.annotation.NonNull;
import android.view.View;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.util.BLog;


/**
 * Create by beini  2017/3/24.
 * http://mobile.51cto.com/android-527752.htm
 * http://droidyue.com/blog/2016/08/14/android-annnotation/
 * 注解
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
