package com.beini.ui.activity;

import com.beini.base.BaseActivity;
import com.idescout.sql.SqlScoutServer;

/**
 * Created by beini on 2017/2/9.
 */

public class IndexActivity extends BaseActivity {

    @Override
    public void initView() {
        SqlScoutServer.create(this, getPackageName());
        goToHome();
//        replaceFragment(LoginFragment.class);
    }

}
