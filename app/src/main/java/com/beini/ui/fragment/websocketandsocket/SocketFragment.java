package com.beini.ui.fragment.websocketandsocket;


import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.Toast;

import com.beini.R;
import com.beini.base.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;
import com.beini.util.BLog;
import com.jakewharton.rxbinding2.view.RxView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.functions.Consumer;

/**
 * create by beini  2017/6/26
 */
@ContentView(R.layout.fragment_socket)
public class SocketFragment extends BaseFragment {
    @ViewInject(R.id.btn_connetc)
     Button btn_connetc;
    @ViewInject(R.id.btn_screen_shot)
     Button btn_screen_shot;
    @ViewInject(R.id.btn_alt_tab)
     Button btn_alt_tab;
    @ViewInject(R.id.btn_stop_socket)
    Button btn_stop_socket;

    /**
     * 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
     * 并不会自动销毁空闲状态的线程
     */
    private ExecutorService fixedThreadPool;
    private String TAG = "com.beini";
    private UIHandler uIhandler;
    private int UNKNOWN_HOST_EXCEPTION = 0x110;
    private int SOCKET_EXCEPTION = 0x111;
    private int IO_EXCEPTION = 0x112;
    private int SUCCESS = 0x113;
    private int FAILED = 0x114;
    private Thread threadConnect;
    private String IP_ADDRESS = "10.0.0.35";
    private int IP_PORT = 10000;
    private int TIME_OUT=10000;

    @Override
    public void initView() {

        RxView.clicks(btn_connetc).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                option(0);
            }
        });
        RxView.clicks(btn_screen_shot).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                option(1);
            }
        });
        RxView.clicks(btn_alt_tab).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                option(6);
            }
        });
        RxView.clicks(btn_stop_socket).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                threadConnect.interrupt();
            }
        });
        fixedThreadPool = Executors.newFixedThreadPool(5);
        uIhandler = new UIHandler();
    }

    /**
     * Socket 操作
     */
    public void option(final int i) {
        threadConnect = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Socket s = new Socket(IP_ADDRESS, IP_PORT);
                    s.setSoTimeout(TIME_OUT);
                    BLog.d("    连接已经建立");
                    OutputStream os = s.getOutputStream();
                    os.write(String.valueOf(i).getBytes());
                    InputStream is = s.getInputStream();
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(is));
                    String message = br.readLine();
                    if (message != null&&message.equals("0")) {
                        uIhandler.sendEmptyMessage(SUCCESS);
                    } else {
                        uIhandler.sendEmptyMessage(FAILED);
                    }
                    os.close();
                    is.close();
                    s.close();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    uIhandler.sendEmptyMessage(UNKNOWN_HOST_EXCEPTION);
                } catch (SocketException e) {
                    e.printStackTrace();
                    uIhandler.sendEmptyMessage(SOCKET_EXCEPTION);
                } catch (IOException e) {
                    e.printStackTrace();
                    uIhandler.sendEmptyMessage(IO_EXCEPTION);
                }
            }
        };
        fixedThreadPool.execute(threadConnect);
    }


    private class UIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int message = msg.what;
            if (UNKNOWN_HOST_EXCEPTION == message) {
                Toast.makeText(getActivity(), "UNKNOWN_HOST_EXCEPTION ", Toast.LENGTH_SHORT).show();
            } else if (SOCKET_EXCEPTION == message) {
                Toast.makeText(getActivity(), "SOCKET_EXCEPTION ", Toast.LENGTH_SHORT).show();
            } else if (IO_EXCEPTION == message) {
                Toast.makeText(getActivity(), "IO_EXCEPTION ", Toast.LENGTH_SHORT).show();
            } else if (SUCCESS == message) {
                Toast.makeText(getActivity(), "SUCCESS ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "FAILED ", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
