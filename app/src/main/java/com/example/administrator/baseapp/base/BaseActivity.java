package com.example.administrator.baseapp.base;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;

import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.bind.ContentView;
import com.example.administrator.baseapp.bind.Event;
import com.example.administrator.baseapp.bind.ViewInject;
import com.example.administrator.baseapp.bind.ViewInjectorImpl;
import com.example.administrator.baseapp.utils.FragmentHelper;
import com.example.administrator.baseapp.utils.ObjectUtil;
import com.example.administrator.baseapp.utils.SnackbarUtil;

/**
 * Created by beini on 2017/2/8.
 */

@ContentView(R.layout.activity_base)
public abstract class BaseActivity extends AppCompatActivity implements BaseImpl {
    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    FragmentManager fragmentManager;
    @ViewInject(R.id.layout_coor)
    CoordinatorLayout layout_coor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        fragmentManager = getFragmentManager();
        this.initView();
        ViewInjectorImpl.registerInstance(this);
    }

    @Event({})
    private void mEvent(View view) {

    }

    public abstract void initView();

    @Override
    public void replaceFragment(Class<?> fragment) {
        BaseFragment baseFragment = (BaseFragment) ObjectUtil.createInstance(fragment);
        Fragment newFragment = fragmentManager.findFragmentByTag(fragment.getName());
        if (newFragment != null) {
            FragmentHelper.showFragment(fragmentManager, newFragment);
        } else {
            FragmentHelper.hideAllFragment(fragmentManager);
            FragmentHelper.addFragment(fragmentManager, baseFragment, fragment.getName(), R.id.content_frame);
        }
    }

    @Override
    public void onBackPressed() {
        FragmentHelper.removePreFragment(layout_coor, fragmentManager);
    }

    @Override
    public void back() {
        FragmentHelper.removePreFragment(layout_coor, fragmentManager);
    }

    @Override
    public void remove(Fragment fragment) {
        FragmentHelper.removeFragment(fragmentManager, fragment);
    }
}
