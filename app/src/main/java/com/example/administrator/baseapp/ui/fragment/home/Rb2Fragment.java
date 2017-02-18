package com.example.administrator.baseapp.ui.fragment.home;


import android.view.View;

import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.base.BaseFragment;
import com.example.administrator.baseapp.bind.ContentView;

/**
 * Created by beini on 2017/2/9.
 */
@ContentView(R.layout.fragment_rb2)
public class Rb2Fragment extends BaseFragment {


    @Override
    public void initView() {
        baseActivity.setBottom(View.VISIBLE);
        baseActivity.setTopBar(View.GONE);
    }

}
