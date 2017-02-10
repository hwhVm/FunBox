package com.example.administrator.baseapp.ui.fragment.login;


import android.view.View;
import android.widget.EditText;
import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.base.BaseFragment;
import com.example.administrator.baseapp.bind.ContentView;
import com.example.administrator.baseapp.bind.Event;
import com.example.administrator.baseapp.bind.ViewInject;
import com.example.administrator.baseapp.ui.fragment.home.HomeFragment;
import com.example.administrator.baseapp.ui.fragment.login.model.LoginModel;

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
    }

    @Event({R.id.btn_login})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                loginModel.login();
                break;
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
