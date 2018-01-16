package com.beini.ui.fragment.wifi;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ProxyInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.util.BLog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Create by beini  2018/1/11
 */
@ContentView(R.layout.fragment_net)
public class NetFragment extends BaseFragment {
    /**
     * WifiManager
     * WifiP2pManager
     * WifiConfiguration
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void initView() {
        //wifi
        WifiManager wifiManager = (WifiManager) baseActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        //网络
        ConnectivityManager connectivityManager = (ConnectivityManager) baseActivity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netWorkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);//TYPE_MOBILE

        ProxyInfo proxyInfo = connectivityManager.getDefaultProxy();
        BLog.e("------->" + (proxyInfo != null));
        if (proxyInfo != null) {//设置了代理
            BLog.e("------------->  proxyInfo.getPort()=" + proxyInfo.getPort());
            BLog.e("------------->  proxyInfo.getHost()=" + proxyInfo.getHost());
            BLog.e("------------->  proxyInfo.getPacFileUrl()=" + proxyInfo.getPacFileUrl());
            BLog.e("------------->  proxyInfo.getExclusionList()=" + Arrays.toString(proxyInfo.getExclusionList()));
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Event(R.id.btn_set_proxy)
    private void clickMethod(View view) {
        switch (view.getId()) {
            case R.id.btn_set_proxy:
                try {
                    Class<?> class1 = Class.forName("android.net.Proxy");
//                    Method[] methods = class1.getDeclaredMethods();
//                    for (int i = 0; i < methods.length; i++) {
//                        BLog.e("Demo6,取得SuperMan类的方法：");
//                        BLog.e("函数名：" + methods[i].getName());
//                        BLog.e("函数返回类型：" + methods[i].getReturnType());
//                        BLog.e("函数访问修饰符：" + Modifier.toString(methods[i].getModifiers()));
//                        BLog.e("函数代码写法： " + methods[i]);
//                    }
                    Method method = class1.getMethod("setHttpProxySystemProperty", ProxyInfo.class);
                    method.invoke(class1.newInstance(), ProxyInfo.buildDirectProxy(" 10.0.0.30", 8888));

                } catch (ClassNotFoundException | IllegalAccessException | java.lang.InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
//                ConnectivityManager connectivityManager = (ConnectivityManager) baseActivity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//                ProxyInfo proxyInfo = connectivityManager.getDefaultProxy();
//                BLog.e("       " + proxyInfo.getPort() + "   " + proxyInfo.getHost());

                WifiManager wifiManager = (WifiManager) baseActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifiManager.disconnect();
                wifiManager.reconnect();
                break;
        }
    }


}
