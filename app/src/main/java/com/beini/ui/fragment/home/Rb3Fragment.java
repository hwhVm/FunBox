package com.beini.ui.fragment.home;

import android.view.View;

import com.beini.R;
import com.beini.base.BaseFragment;
import com.beini.bind.ContentView;

/**
 * Created by beini on 2017/2/9.
 */
@ContentView(R.layout.fragment_rb3)
public class Rb3Fragment extends BaseFragment {


    @Override
    public void initView() {
        baseActivity.setBottom(View.VISIBLE);
        baseActivity.setTopBar(View.GONE);
    }
}
