package com.beini.ui.activity;

import com.beini.app.AppRouter;
import com.beini.app.BaseActivity;

/**
 * Created by beini on 2017/2/9.
 */

public class IndexActivity extends BaseActivity {

    @Override
    public void initView() {
//        SqlScoutServer.create(this, getPackageName());
//        replaceFragment(LoginFragment.class);
        AppRouter.baseActivity = this;
        AppRouter.indexArouter();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Debug.stopMethodTracing();
    }
}
