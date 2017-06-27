package com.beini.ui.fragment.wifi;

import android.net.wifi.ScanResult;
import android.support.v7.widget.RecyclerView;

import com.beini.R;
import com.beini.adapter.BaseAdapter;
import com.beini.bean.BaseBean;

import java.util.List;

/**
 * Created by beini on 2017/4/13.
 */

public class WiFiInfoAdapter extends BaseAdapter {

    List<ScanResult> scanResults;

    public WiFiInfoAdapter(BaseBean<ScanResult> baseBean) {
        super(baseBean);
        this.scanResults = baseBean.getBaseList();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        ScanResult scanResult = scanResults.get(position);
//        BLog.d("       信号强度=  " + scanResult.level);
        getTextView((ViewHolder) holder, R.id.text_wifi_name).setText(scanResult.SSID);
    }
}
