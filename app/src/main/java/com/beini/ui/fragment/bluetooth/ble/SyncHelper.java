package com.beini.ui.fragment.bluetooth.ble;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;

import com.beini.utils.BLog;

import java.util.Map;
import java.util.Set;

/**
 * Created by beini on 2017/3/17.
 */

public class SyncHelper {

    /**
     * 初始化蓝牙
     *
     * @param context
     */
    public static BluetoothAdapter initBluetooth_manual(Activity context) {
        BluetoothManager mBluetoothManager = null;

        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);

            // 没有蓝牙模块
            if (mBluetoothManager == null) {
                return null;
            }
        }
        BluetoothAdapter mBtAdapter = mBluetoothManager.getAdapter();

        // 判断机器是否有蓝牙
        if (!mBtAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivityForResult(enableIntent, Global.REQUEST_ENABLE_BLUETOOTH);
        }
        return mBtAdapter;
    }

    public static boolean isContainBoundDevice(Map<Band, Integer> deviceMap, String lastSyncDeviceAddress) {
        if (deviceMap != null && lastSyncDeviceAddress != null) {
            Set<Band> keySet = deviceMap.keySet();
            for (Band band : keySet) {
                String address = band.getAddress();
                if (address != null && address.equals(lastSyncDeviceAddress)) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * 获取距离最近的设备
     *
     * @param deviceMap
     * @return
     */
    public static Band getNearestDevice(Map<Band, Integer> deviceMap) {
        if (deviceMap != null) {
            Set<Band> keySet = deviceMap.keySet();

            int rssiMax = -9999;
            Band nearestDevice = null;

            for (Band band : keySet) {
                Integer rssiValue = deviceMap.get(band);

                if (rssiValue != null) {
                    if (rssiValue > rssiMax) {
                        rssiMax = rssiValue;
                        nearestDevice = band;
                    }
                }
            }
            if (nearestDevice != null) {
                BLog.d("nearest address: " + nearestDevice.getAddress());
            } else {
                BLog.d("nearest band is null");
            }

            return nearestDevice;
        }

        return null;
    }

    /**
     * 寻找合适设备
     *
     * @param device
     */
    public static void matchDevice(Map<Band, Integer> deviceMap, BluetoothDevice device, int rssi) {

        String name = device.getName();
        String address = device.getAddress();

        if (device != null && name != null && address != null) {
//            if (name.startsWith(Global.DEVICE_NAME_WB013)) {
            if (device.getName().equals("TimeBox-mini-light-xiaohuang")) {
                BLog.d(address + ", " + rssi);
                Band band = new Band();
                band.setName(name);
                band.setAddress(address);
                deviceMap.put(band, rssi);
            }
        }
    }
}
