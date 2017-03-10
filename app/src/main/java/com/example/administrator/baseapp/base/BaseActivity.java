package com.example.administrator.baseapp.base;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.bind.ContentView;
import com.example.administrator.baseapp.bind.Event;
import com.example.administrator.baseapp.bind.ViewInject;
import com.example.administrator.baseapp.bind.ViewInjectorImpl;
import com.example.administrator.baseapp.ui.fragment.home.HomeFragment;
import com.example.administrator.baseapp.ui.fragment.home.Rb2Fragment;
import com.example.administrator.baseapp.ui.fragment.home.Rb3Fragment;
import com.example.administrator.baseapp.utils.BLog;
import com.example.administrator.baseapp.utils.listener.KeyBackListener;
import com.example.administrator.baseapp.utils.permission.EasyPermissions;
import com.example.administrator.baseapp.utils.FragmentHelper;
import com.example.administrator.baseapp.utils.ObjectUtil;
import java.util.List;

/**
 * Created by beini on 2017/2/8.
 */

@ContentView(R.layout.activity_base)
public abstract class BaseActivity extends AppCompatActivity implements BaseImpl {
    @ViewInject(R.id.toolbar)
    public    Toolbar toolbar;
    @ViewInject(R.id.layout_coor)
    CoordinatorLayout layout_coor;
    @ViewInject(R.id.rg_bottom)
    RadioGroup rg_bottom;
    @ViewInject(R.id.rb_1)
    RadioButton rb_1;
    @ViewInject(R.id.top_bar_title)
    TextView top_bar_title;


    private FragmentManager fragmentManager;
    public KeyBackListener keyBackListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        fragmentManager = getFragmentManager();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);  //透明导航栏
        ViewInjectorImpl.registerInstance(this);
        this.initView();
    }

    @Event({R.id.rb_1, R.id.rb_2, R.id.rb_3})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.rb_1:
                this.replaceFragment(HomeFragment.class);
                FragmentHelper.homeTag = 0;
                break;
            case R.id.rb_2:
                this.replaceFragment(Rb2Fragment.class);
                FragmentHelper.homeTag = 1;
                break;
            case R.id.rb_3:
                this.replaceFragment(Rb3Fragment.class);
                FragmentHelper.homeTag = 2;
                break;
        }

    }

    public void goToHome() {
        mEvent(rb_1);
        rb_1.setChecked(true);
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
            FragmentHelper.addFragment(fragmentManager, baseFragment, fragment.getName());
        }

    }

    @Override
    public void onBackPressed() {
        if (keyBackListener != null) {
            keyBackListener.keyBack();
        } else {
            FragmentHelper.removePreFragment(layout_coor, fragmentManager, this);
        }
    }

    @Override
    public void back() {
        FragmentHelper.removePreFragment(layout_coor, fragmentManager, this);
    }

    @Override
    public void remove(Fragment fragment) {
        FragmentHelper.removeFragment(fragmentManager, fragment);
    }

    @Override
    public void setBottom(int isHide) {
        rg_bottom.setVisibility(isHide);
    }

    @Override
    public void setTopBar(int isHide) {
        toolbar.setVisibility(isHide);
    }

    @Override
    public void setTopBarTitle(String title) {
        top_bar_title.setText(title);
    }



}
