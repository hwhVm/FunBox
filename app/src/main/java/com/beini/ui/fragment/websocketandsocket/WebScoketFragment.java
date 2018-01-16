package com.beini.ui.fragment.websocketandsocket;


import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.bind.ViewInject;
import com.beini.net.retrofit.NetUtil;
import com.beini.ui.fragment.notification.NotifyUtility;
import com.beini.util.BLog;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.NotYetConnectedException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Create by beini 2017/4/24
 */
@ContentView(R.layout.fragment_web_scoket)
public class WebScoketFragment extends BaseFragment {
    @ViewInject(R.id.viewMain)
    LinearLayout viewMain;
    private Client mClient;

    @Override
    public void initView() {

    }

    @Event({R.id.btn_login_web_scoket,R.id.btn_web_socket_conntect, R.id.btn_web_socket_disconntect, R.id.btn_web_socket_send, R.id.btn_global_web_scoket})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_web_socket_conntect:
                    connectToServer();
                break;
            case R.id.btn_web_socket_disconntect:
                if (null != mClient) {
                    mClient.close();
                }
                break;
            case R.id.btn_web_socket_send:
                if (null != mClient) {
                    String msg = "------->sssssss";
                    if (!TextUtils.isEmpty(msg)) {
                        try {
                            mClient.send(msg);
                        } catch (NotYetConnectedException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                }
                break;
            case R.id.btn_global_web_scoket:
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
                break;
            case R.id.btn_login_web_scoket:
                Map<String, String> maps = new HashMap<>();
                maps.put("userId", UUID.randomUUID().toString());
                NetUtil.getSingleton().sendPostWithParm("webSocketSaveSession", maps).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                            BLog.d("       "+response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
                break;
        }
    }

    /**
     * // draft = new Draft_10();
     * //    draft = new Draft_17();
     * //   draft = new Draft_75();
     * //    draft = new Draft_76();
     */
    private void connectToServer() {
        String ip = "10.0.0.53";
        String port = "8080/webSocketServer";
        if (TextUtils.isEmpty(ip) || TextUtils.isEmpty(port)) {
            Snackbar.make(viewMain, "IP and Port 不能为空", Snackbar.LENGTH_LONG).show();
            return;
        }
        String address = String.format("ws://%s:%s", ip, port);
        Draft draft = new Draft_17();
        try {
            URI uri = new URI(address);
            mClient = new Client(uri, draft);
            mClient.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
    }

    private class Client extends WebSocketClient {

        public Client(URI serverUri, Draft draft) {
            super(serverUri, draft);
        }

        @Override
        public void onOpen(ServerHandshake handShakeData) {
            BLog.d("       onOpen   " + getURI());
        }

        @Override
        public void onMessage(String message) {
            BLog.d("       onMessage  message=" + message);
            if (Integer.parseInt(message) != 0) {
                NotifyUtility.start(baseActivity, null, "来自服务器的消息", message);
            }
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            BLog.d("    onClose  code=" + code + "  reason=" + reason + "   remote==" + remote);
        }

        @Override
        public void onError(Exception ex) {
            BLog.d("    ex==" + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }
}
