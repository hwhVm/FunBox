package com.beini.ui.fragment.webtest;


import android.view.View;

import com.beini.R;
import com.beini.base.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.net.NetUtil;
import com.beini.util.BLog;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Create by beini 2017/6/9
 */
@ContentView(R.layout.fragment_shiro)
public class ShiroFragment extends BaseFragment {

    @Override
    public void initView() {
    }

    @Event({R.id.btn_shiro_test, R.id.btn_shiro_has_role})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_shiro_test:
                BLog.d("         开始测试shiro");
                Map<String, String> maps = new HashMap<>();
                maps.put("name", "beini1");
//                maps.put("password","1234560");
                maps.put("password", "d8e423a9d5eb97da9e2d58cd57b92808");
                NetUtil.getSingleton().sendPostWithParm("dologin", maps).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        BLog.d("   respone==" + response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        BLog.d("    t==" + t.getLocalizedMessage());
                    }
                });
                break;
            case R.id.btn_shiro_has_role:
                Map<String, String> noParm = new HashMap<>();
                    NetUtil.getSingleton().sendPostWithParm("queryAllLeader", noParm).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        BLog.d("   respone==" + response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        BLog.d("    t==" + t.getLocalizedMessage());
                    }
                });
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
