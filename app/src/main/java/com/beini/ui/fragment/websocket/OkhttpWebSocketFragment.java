package com.beini.ui.fragment.websocket;


import android.widget.Button;

import com.beini.R;
import com.beini.base.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;
import com.beini.net.NetUtil;
import com.beini.net.OkhttpWebSocketUtil;
import com.beini.utils.BLog;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Create by beini 2017/6/26
 */
@ContentView(R.layout.fragment_okhttp_web_socket)
public class OkhttpWebSocketFragment extends BaseFragment {
    private OkhttpWebSocketUtil mOkhttpWebSocketUtil;
    @ViewInject(R.id.btn_okhttp_connect_websocket)
    Button btn_okhttp_connect_websocket;
    @ViewInject(R.id.btn_send_to_service)
    Button btn_send_to_service;
    @ViewInject(R.id.btn_global_web_scoket_okhttp)
    Button btn_global_web_scoket_okhttp;
    
    @Override
    public void initView() {
        mOkhttpWebSocketUtil = new OkhttpWebSocketUtil();
        RxView.clicks(btn_okhttp_connect_websocket).subscribe(new Consumer<Object>() {//连接

            @Override
            public void accept(Object o) throws Exception {
                mOkhttpWebSocketUtil.webSocketHandler();
            }
        });
        RxView.clicks(btn_send_to_service).subscribe(new Consumer<Object>() {//发送
            @Override
            public void accept(Object o) throws Exception {
                if (mOkhttpWebSocketUtil.webSocket != null) {
                    mOkhttpWebSocketUtil.webSocket.send("-------->form beini");
                }
            }
        });
        RxView.clicks(btn_global_web_scoket_okhttp).subscribe(new Consumer<Object>() {

            @Override
            public void accept(Object o) throws Exception {
                Map<String, String> map = new HashMap<>();
                NetUtil.getSingleton().sendPostWithParm("auditing",map).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        BLog.d("  response==" + response.isSuccessful());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        BLog.d("            t=" + t.getLocalizedMessage());
                    }
                });
            }
        });
    }

}
