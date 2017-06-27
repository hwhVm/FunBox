package com.beini.ui.fragment.bluetooth.manage;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;

import com.beini.util.BLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by beini on 2017/3/16.
 */

public class DeviceManager {

    private static BluetoothAdapter mBluetoothAdapter;
    private List<BluetoothDevice> discoveryDevices;
    private List<BluetoothDevice> pairedDevices;

    private static DeviceManager instance;

    private DeviceManager() {
        super();
    }

    public static DeviceManager getInstance() {
        if (instance == null) {
            initLeScanCallback();
            instance = new DeviceManager();
            instance.discoveryDevices = new ArrayList<>();
            instance.getPairedDevices();

        }
        return instance;
    }

    public static void startDiscovery(Activity activity) {
        BLog.d("-------->开始搜索");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null)
            return;
        if (instance.discoveryDevices != null)
            instance.discoveryDevices.clear();

        if (!mBluetoothAdapter.isEnabled()) {  // 判断蓝牙是否可用, 如果不可用, 先打开蓝牙模块
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableIntent, Constant.REQUEST_ENABLE_BT);
            mBluetoothAdapter.cancelDiscovery();
            scanLeDevice(false);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//                DeviceDialogManager.dismissDiscoveryDialog(activity);
        } else {
            if (mBluetoothAdapter.isDiscovering()) {
                BLog.d("蓝牙模块正在扫描,关闭扫描");
                mBluetoothAdapter.cancelDiscovery();
                scanLeDevice(false);
            }
            BLog.d("蓝牙模快开始扫描");
            mBluetoothAdapter.startDiscovery();
            scanLeDevice(true);
        }
    }

    private static BluetoothAdapter.LeScanCallback mLeScanCallback;

    public static BluetoothAdapter.LeScanCallback initLeScanCallback() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                    if (device.getName() == null || device.getAddress() == null || device.getBluetoothClass() == null)
                        return;
                    Intent intent;
                    addDiscoveryDevice(device);

                    for (BluetoothDevice device2 : DeviceManager.getInstance().getPairedDevices()) {
//						发现的设备和已经配对的设备一样的话，就直接链接
                        if (device.getAddress().equals(device2.getAddress())) {
                            BLog.d("已配对的  device2 : " + device2.getAddress() + " " + device2.getName());
                            if (Build.VERSION.SDK_INT >= 0x12) {
                                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                            }
//                            if (SPPService.getInstance().getState() != SPPService.STATE_NONE) {
//                                break;
//                            }

                            BLog.d("BLE connect " + device.getName());

//									DeviceManager.getInstance().setCurrentDevice(device2);
//                            SPPService.getInstance().connect(device2);
                            break;
                        }

//                        intent = new Intent(Constant.ACTION_DEVICECONNECTACTIVITY_NEW_DEVICE_FOUND);
//                        GlobalApplication.getInstance().sendBroadcast(intent);
                    }
                }
            };

        }

        return mLeScanCallback;
    }


    public List<BluetoothDevice> getPairedDevices() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // 获取已经配对的设备
        if (mBluetoothAdapter == null) {
            pairedDevices = new ArrayList<BluetoothDevice>();
            return pairedDevices;
        }
        Set<BluetoothDevice> pairedDevicesSet = mBluetoothAdapter
                .getBondedDevices();
        pairedDevices = new ArrayList<BluetoothDevice>();
        // 将所有已配对设备信息放入列表中
        if (pairedDevicesSet.size() > 0) {
            for (BluetoothDevice device : pairedDevicesSet) {
                if (device.getName() != null && !"".equals(device.getName()) && device.getName().contains(Constant.DEVICE_NAME))
                    pairedDevices.add(device);
            }
        }
        return pairedDevices;
    }

    // Stops scanning after 10 seconds.
    public static void scanLeDevice(final boolean enable) {
        BLog.d("-------------------------->enable===" + enable + "  Build.VERSION.SDK_INT=" +
                Build.VERSION.SDK_INT + "   ox12==" + 0x12 + "  mBluetoothAdapter==mull=" + (mBluetoothAdapter == null));
        if (Build.VERSION.SDK_INT >= 0x12) {
            if (enable & mBluetoothAdapter != null) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                BLog.d("---------->start BLE scan");
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else if (!enable && mBluetoothAdapter != null) {
                BLog.d("---------->stop BLE scan");
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }
    }

    public static boolean addDiscoveryDevice(BluetoothDevice device) {
        boolean isAddSucc = false;
        if (device != null &&
                device.getName() != null &&
                device.getName().contains(Constant.DEVICE_NAME) &&
                device.getAddress() != null) {
            boolean isRepeat = false;
            //判断该列表中得 设备的 mac 地址是否与该设备的 mac 地址重复
            for (BluetoothDevice bluetoothDevice : getInstance().discoveryDevices) {
                if (device.getAddress().equals(bluetoothDevice.getAddress())) {
                    isRepeat = true;
                }
            }
            if (!isRepeat) {
                instance.discoveryDevices.add(device);
                isAddSucc = true;
            }
        }
        return isAddSucc;
    }
}
