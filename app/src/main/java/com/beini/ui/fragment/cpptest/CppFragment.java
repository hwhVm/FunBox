package com.beini.ui.fragment.cpptest;


import android.view.View;
import android.widget.TextView;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;
import com.beini.ndk.NDKMain;
import com.beini.util.BLog;

/**
 * Create by beini 2017/5/2
 */
@ContentView(R.layout.fragment_cpp)
public class CppFragment extends BaseFragment {
    @ViewInject(R.id.text_cpp)
    TextView text_cpp;

    @Override
    public void initView() {
        baseActivity.setTopBar(View.GONE);
        baseActivity.setBottom(View.GONE);
        NDKMain ndkMain = new NDKMain();
        BLog.e("befor--------------->str=" + ndkMain.str);
        ndkMain.accessField();
        BLog.e("after--------------->str=" + ndkMain.str);
        BLog.e("--------------->ndkMain.num="+ndkMain.num);
    }


}
