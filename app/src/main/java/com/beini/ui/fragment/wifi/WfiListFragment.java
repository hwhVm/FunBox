package com.beini.ui.fragment.wifi;


import android.net.wifi.ScanResult;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.beini.R;
import com.beini.adapter.BaseAdapter;
import com.beini.base.BaseFragment;
import com.beini.bean.BaseBean;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.bind.ViewInject;
import com.beini.net.utils.GetWifiInfoUtil;
import com.beini.utils.BLog;

import java.util.List;

/**
 * Create by beini  2017/4/13
 * http://www.2cto.com/kf/201310/253617.html
 */
@ContentView(R.layout.fragment_wfi_list)
public class WfiListFragment extends BaseFragment {
    @ViewInject(R.id.recycle_wifi)
    RecyclerView recycle_wifi;
    @ViewInject(R.id.text_wifi_info)
    TextView text_wifi_info;

    private WifiTool wiFiAdmin;
    private List<ScanResult> list;
    private boolean isFirstLoad = true;
    private String WIFI_CONNECT_SUCCESS = "WIFI_CONNECT_SUCCESS";
    private String WIFI_CONNECT_ERROR = "WIFI_CONNECT_ERROR";

    @Override
    public void initView() {
        wiFiAdmin = new WifiTool(baseActivity);
        getAllNetWorkList();
        text_wifi_info.setText(GetWifiInfoUtil.getWifiInfo(getActivity()));
    }

    @Event({R.id.btn_wifi_refresh})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_wifi_refresh:
                getAllNetWorkList();
                break;
        }
    }

    WiFiInfoAdapter wifiInfoAdapter;

    public void getAllNetWorkList() {
        wiFiAdmin.startScan();
        list = wiFiAdmin.getWifiList();
        if (list != null && list.size() > 0) {
            if (isFirstLoad) {
                wiFiAdmin.getConfiguration();
                recycle_wifi.setLayoutManager(new LinearLayoutManager(baseActivity));
                wifiInfoAdapter = new WiFiInfoAdapter(new BaseBean<>(R.layout.item_wifi_list, list));
                recycle_wifi.setAdapter(wifiInfoAdapter);
                wifiInfoAdapter.setItemClick(onItemClickListener);
                isFirstLoad = false;
            } else {
                wiFiAdmin.getConfiguration();
                wifiInfoAdapter.notifyDataSetChanged();
            }
        }
    }

    WiFiInfoAdapter.OnItemClickListener onItemClickListener = new BaseAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            ScanResult scanResult = list.get(position);
            String strResult = connectWifi("divoomzhongke508", scanResult);
            BLog.d("         strResult=" + strResult + "   SSID==" + scanResult.SSID);
        }
    };

    /**
     * @param wifiPassword
     * @param scanResult
     */
    public String connectWifi(String wifiPassword, ScanResult scanResult) {
        String wifiItemSSID = scanResult.SSID;

        int wifiItemId = wiFiAdmin.IsConfiguration("\""
                + scanResult.SSID + "\"");
        if (wifiItemId != -1) {
            BLog.d("    连接已保存密码的WiFi ");
            if (wiFiAdmin.ConnectWifi(wifiItemId)) { // 连接已保存密码的WiFi
//                getAllNetWorkList();
            }
            return WIFI_CONNECT_SUCCESS;
        } else { // 没有配置好信息，配置
            BLog.d("    没有配置好信息，配置 ");
            if (!TextUtils.isEmpty(wifiPassword)) {
                int netId = wiFiAdmin
                        .AddWifiConfig(list,
                                wifiItemSSID,
                                wifiPassword);

                if (netId != -1) {
                    wiFiAdmin.getConfiguration();// 添加了配置信息，要重新得到配置信息
                    if (wiFiAdmin
                            .ConnectWifi(netId)) {
//                        getAllNetWorkList();
                        return WIFI_CONNECT_SUCCESS;
                    }
                } else {
                    return WIFI_CONNECT_ERROR;
                }
            } else {
                return WIFI_CONNECT_ERROR;
            }

        }
        return WIFI_CONNECT_ERROR;
    }

}
