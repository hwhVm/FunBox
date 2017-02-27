package com.example.administrator.baseapp.ui.fragment.login;


import android.Manifest;
import android.os.Build;
import android.view.View;
import android.widget.EditText;

import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.base.BaseFragment;
import com.example.administrator.baseapp.bind.ContentView;
import com.example.administrator.baseapp.bind.Event;
import com.example.administrator.baseapp.bind.ViewInject;
import com.example.administrator.baseapp.ui.fragment.home.HomeFragment;
import com.example.administrator.baseapp.ui.fragment.login.model.LoginModel;
import com.example.administrator.baseapp.utils.BLog;
import com.example.administrator.baseapp.utils.permission.EasyPermissions;

import java.util.List;

/**
 * Created by beini on 2017/2/9.
 */
@ContentView(R.layout.fragment_login)
public class LoginFragment extends BaseFragment {
    @ViewInject(R.id.ed_username)
    EditText ed_username;
    @ViewInject(R.id.ed_password)
    EditText ed_password;


    LoginModel loginModel;

    @Override
    public void initView() {
        loginModel = new LoginModel(this);
        baseActivity.setBottom(View.GONE);
    }

    @Override
    protected void onInvisible() {

    }

    @Override
    protected void onVisible() {

    }

    @Override
    protected void lazyLoad() {

    }

    @Event({R.id.btn_login})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                checkPermissionMethod(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},"ff",44);
//                loginModel.login();
                break;
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            BLog.d("     "+(!EasyPermissions.somePermissionPermanentlyDenied(this, perms)));

            if (!EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                baseActivity.remove(LoginFragment.this);
                baseActivity.goToHome();
            } else {

                return;
            }
        }


    }

    // get   set
    public void goToNexPage() {
        baseActivity.replaceFragment(HomeFragment.class);
    }

    public EditText getEd_username() {
        return ed_username;
    }

    public EditText getEd_password() {
        return ed_password;
    }

}
