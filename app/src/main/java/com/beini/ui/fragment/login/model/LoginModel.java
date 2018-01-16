package com.beini.ui.fragment.login.model;

import android.app.Fragment;

import com.beini.net.retrofit.NetUtil;
import com.beini.net.request.LoginRequest;
import com.beini.net.response.BaseResponseJson;
import com.beini.ui.fragment.login.LoginFragment;
import com.beini.util.BLog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by beini on 2017/2/10.
 */

public class LoginModel {
    Fragment fragment;

    public LoginModel(Fragment fragment) {
        this.fragment = fragment;
    }

    public void login() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(((LoginFragment) fragment).getEd_username().getText().toString());
        loginRequest.setPassword(((LoginFragment) fragment).getEd_password().getText().toString());

        NetUtil.getSingleton().sendRequestPost("login", loginRequest).enqueue(new Callback<BaseResponseJson>() {
            @Override
            public void onResponse(Call<BaseResponseJson> call, Response<BaseResponseJson> response) {//回调在主线程执行
                if (response.body().getReturnCode() == 0) {
                    ((LoginFragment) fragment).goToNexPage();
                    ((LoginFragment) fragment).baseActivity.remove(fragment);
                } else {

                }

            }

            @Override
            public void onFailure(Call<BaseResponseJson> call, Throwable t) {
                BLog.d("onFailure" + t.getMessage());
            }
        });

    }

}
