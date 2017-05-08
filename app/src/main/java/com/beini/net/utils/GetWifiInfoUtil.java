package com.beini.net.utils;

import android.app.Activity;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.beini.utils.BLog;


/**
 * Created by beini on 2017/5/8.
 */

public class GetWifiInfoUtil {

    public  static String getWifiInfo(Activity context) {
        WifiManager wifiManager = ((WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE));
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
        StringBuilder sb = new StringBuilder();
        sb.append("网络信息：");
        sb.append("\nipAddress：" + intToIp(dhcpInfo.ipAddress));
        sb.append("\nnetmask：" + intToIp(dhcpInfo.netmask));
        sb.append("\ngateway：" + intToIp(dhcpInfo.gateway));
        sb.append("\nserverAddress：" + intToIp(dhcpInfo.serverAddress));
        sb.append("\ndns1：" + intToIp(dhcpInfo.dns1));
        sb.append("\ndns2：" + intToIp(dhcpInfo.dns2));
        sb.append("\n");
        BLog.d(intToIp(dhcpInfo.ipAddress));
        BLog.d(intToIp(dhcpInfo.netmask));
        BLog.d(intToIp(dhcpInfo.gateway));
        BLog.d(intToIp(dhcpInfo.serverAddress));
        BLog.d(intToIp(dhcpInfo.dns1));
        BLog.d(intToIp(dhcpInfo.dns2));
        BLog.d(dhcpInfo.leaseDuration+"");


        sb.append("Wifi信息：");
        sb.append("\nIpAddress：" + intToIp(wifiInfo.getIpAddress()));
        sb.append("\nMacAddress：" + wifiInfo.getMacAddress());
        return sb.toString();
    }

    private static String intToIp(int paramInt) {
        return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "." + (0xFF & paramInt >> 16) + "."
                + (0xFF & paramInt >> 24);
    }
}
