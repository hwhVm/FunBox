package com.example.administrator.baseapp.ui.activity;


import com.example.administrator.baseapp.base.BaseActivity;
import com.example.administrator.baseapp.ui.fragment.LoginFragment;

/**
 * Created by beini on 2017/2/9.
 */

public class IndexActivity extends BaseActivity {

    @Override
    public void initView() {
        replaceFragment(LoginFragment.class);
    }

}
