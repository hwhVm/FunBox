package com.beini.base;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.beini.R;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.bind.ViewInject;
import com.beini.bind.ViewInjectorImpl;
import com.beini.ui.fragment.home.HomeFragment;
import com.beini.ui.fragment.home.Rb2Fragment;
import com.beini.ui.fragment.home.Rb3Fragment;
import com.beini.utils.BLog;
import com.beini.utils.FragmentHelper;
import com.beini.utils.ObjectUtil;
import com.beini.utils.WindowUtils;
import com.beini.utils.listener.ActivityResultListener;
import com.beini.utils.listener.KeyBackListener;
import com.beini.utils.listener.OnTouchEventListener;

/**
 * Created by beini on 2017/2/8.
 */

@ContentView(R.layout.activity_base)
public abstract class BaseActivity extends AppCompatActivity implements BaseImpl {
    @ViewInject(R.id.toolbar)
    public Toolbar toolbar;
    @ViewInject(R.id.layout_coor)
    LinearLayout layout_coor;
    @ViewInject(R.id.rg_bottom)
    RadioGroup rg_bottom;
    @ViewInject(R.id.rb_1)
    RadioButton rb_1;
    @ViewInject(R.id.top_bar_title)
    TextView top_bar_title;


    private FragmentManager fragmentManager;
    private KeyBackListener keyBackListener;
    private ActivityResultListener activityResultListener;

    private OnTouchEventListener onTouchEventListener;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            WindowUtils.setHide(this.getWindow().getDecorView());
        }
    }

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
        if (!(baseFragment instanceof HomeFragment || baseFragment instanceof Rb2Fragment || baseFragment instanceof Rb3Fragment)) {
            this.setBottom(View.GONE);
        }
        Fragment newFragment = fragmentManager.findFragmentByTag(fragment.getName());

        if (newFragment != null) {
            FragmentHelper.showFragment(fragmentManager, newFragment);
        } else {
            FragmentHelper.hideAllFragment(fragmentManager);
            FragmentHelper.addFragment(fragmentManager, baseFragment, fragment.getName());
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyBackListener != null) {
                keyBackListener.keyBack();
            } else {
                onBackPressed();
            }
            return true;
        } else {
            if (keyBackListener != null) {
                keyBackListener.onKeyDown(event);
            }
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BLog.d("   onActivityResult ");
        if (activityResultListener != null) {
            BLog.d("   ----dd------------> ");
            activityResultListener.resultCallback(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        FragmentHelper.removePreFragment(layout_coor, fragmentManager, this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (onTouchEventListener != null) {
            onTouchEventListener.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
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


    public KeyBackListener getKeyBackListener() {
        return keyBackListener;
    }

    public void setKeyBackListener(KeyBackListener keyBackListener) {
        this.keyBackListener = keyBackListener;
    }

    public OnTouchEventListener getOnTouchEventListener() {
        return onTouchEventListener;
    }

    public void setOnTouchEventListener(OnTouchEventListener onTouchEventListener) {
        this.onTouchEventListener = onTouchEventListener;
    }

    public ActivityResultListener getActivityResultListener() {
        return activityResultListener;
    }

    public void setActivityResultListener(ActivityResultListener activityResultListener) {
        this.activityResultListener = activityResultListener;
    }

    /**
     * /**
     * 释放activity的资源，例如释放网络连接，注销监听广播接收者
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * UI已经隐藏
     *
     * @param level
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            //释放UI资源
        }
    }
}
