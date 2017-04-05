package com.beini.ui.activity;

import com.beini.base.BaseActivity;
import com.beini.ui.fragment.login.LoginFragment;

/**
 * Created by beini on 2017/2/9.
 */

public class IndexActivity extends BaseActivity {

    @Override
    public void initView() {
//        goToHome();
        replaceFragment(LoginFragment.class);
    }

}
