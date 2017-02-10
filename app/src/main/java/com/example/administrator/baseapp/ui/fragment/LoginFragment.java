package com.example.administrator.baseapp.ui.fragment;


import android.view.View;

import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.base.BaseFragment;
import com.example.administrator.baseapp.bind.ContentView;
import com.example.administrator.baseapp.utils.BLog;

import butterknife.OnClick;

/**
 * Created by beini on 2017/2/9.
 */
@ContentView(R.layout.fragment_login)
public class LoginFragment extends BaseFragment {

    @Override
    public void initView() {

    }

    @OnClick(R.id.btn_go_home)
     void mOnClick() {
        baseActivity.replaceFragment(HomeFragment.class);
    }
}
