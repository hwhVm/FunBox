package com.example.administrator.baseapp.base;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.bind.ContentView;
import com.example.administrator.baseapp.bind.ViewInjectorImpl;
import com.example.administrator.baseapp.utils.FragmentHelper;
import com.example.administrator.baseapp.utils.ObjectUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by beini on 2017/2/8.
 */

@ContentView(R.layout.activity_base)
public abstract class BaseActivity extends AppCompatActivity implements BaseImpl {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        fragmentManager = getFragmentManager();
        this.initView();
        ButterKnife.bind(this);
        ViewInjectorImpl.registerInstance(this);
    }
    @OnClick({})
    public void mOnClick(View view) {
        switch (view.getId()) {

        }
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


}
