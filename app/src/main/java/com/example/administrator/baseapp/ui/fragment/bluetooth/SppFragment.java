package com.example.administrator.baseapp.ui.fragment.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.base.BaseFragment;
import com.example.administrator.baseapp.bind.ContentView;
import com.example.administrator.baseapp.bind.Event;
import com.example.administrator.baseapp.ui.fragment.bluetooth.ble.Band;
import com.example.administrator.baseapp.ui.fragment.bluetooth.ble.Global;
import com.example.administrator.baseapp.ui.fragment.bluetooth.ble.SmartBLEService;
import com.example.administrator.baseapp.ui.fragment.bluetooth.ble.SppDecodeHolder;
import com.example.administrator.baseapp.ui.fragment.bluetooth.ble.SppProc;
import com.example.administrator.baseapp.ui.fragment.bluetooth.ble.SyncHelper;
import com.example.administrator.baseapp.utils.BLog;
import com.example.administrator.baseapp.utils.listener.ActivityResultListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by beini on 2017/3/15.
 */
@ContentView(R.layout.fragment_spp)
public class SppFragment extends BaseFragment implements ActivityResultListener {
    private SmartBLEService mService = null;
    private Map<Band, Integer> deviceMap = new HashMap<>();

    @Override
    public void initView() {
        baseActivity.setActivityResultListener(this);
        initBLEBroadcastReceiver();
        initServiceConnection();
    }

    /**
     * 初始化 BroadcastReceiver
     */
    private void initBLEBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(Global.ACTION_DEVICE_FOUND);

        intentFilter.addAction(Global.ACTION_GATT_CONNECTED);
        intentFilter.addAction(Global.ACTION_GATT_CONNECTED_FAIL);
        intentFilter.addAction(Global.ACTION_GATT_DISCONNECTED);

        intentFilter.addAction(Global.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(Global.ACTION_GATT_SERVICES_DISCOVER_FAIL);

        intentFilter.addAction(Global.ACTION_WRITE_DESCRIPTOR_SUCCESS);
        intentFilter.addAction(Global.ACTION_WRITE_DESCRIPTOR_FAIL);

        intentFilter.addAction(Global.ACTION_WRITE_CHARACTERISTIC_SUCCESS);
        intentFilter.addAction(Global.ACTION_WRITE_CHARACTERISTIC_FAIL);

        intentFilter.addAction(Global.ACTION_READ_CHARACTERISTIC_BIND_SUCCESS);
        intentFilter.addAction(Global.ACTION_READ_CHARACTERISTIC_BIND_FAIL);

        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(Global.ACTION_BLUETOOTH_ENABLE_CONFORM);

        intentFilter.addAction(Global.ACTION_DATA_AVAILABLE);

        baseActivity.registerReceiver(myBLEBroadcastReceiver, intentFilter);

    }

    @Event({R.id.btn_sysc, R.id.btn_light})
    private void mEvnet(View view) {
        switch (view.getId()) {
            case R.id.btn_sysc:
                beginScanDevice();
                break;
            case R.id.btn_light:
                mService.writeCharacteristic(BleModel.setLightCmd(SppProc.LIGHT_MODE.TEMPRETURE, (byte) -94, (byte) -94, (byte) 73, (byte) 0));
                break;
        }
    }

    /**
     * 开始搜索设备
     */
    private void beginScanDevice() {
        int sdk = Build.VERSION.SDK_INT;
        BLog.d("sdk int : " + sdk);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            // 检测是否存在蓝牙
            BluetoothAdapter mBtAdapter = SyncHelper.initBluetooth_manual(baseActivity);
            if (mBtAdapter != null) {
                // 蓝牙是否打开
                if (mBtAdapter.isEnabled() && mService != null) {
                    BLog.d("connnectionState:" + mService.getConnectionState());
                    if (mService.getConnectionState() != SmartBLEService.STATE_CONNECTED) {
                        BLog.d("no connected device, begin to scan");
                        deviceMap.clear(); // 清空map
                        mService.scan(true);

                    }
                }

            } else {
                Toast.makeText(baseActivity, "no_bluetooth", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(baseActivity, "low_android_version", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 初始化 serviceConnection
     */
    private void initServiceConnection() {
        Intent bindIntent = new Intent(baseActivity, SmartBLEService.class);
        baseActivity.bindService(bindIntent, myServiceConnection, Context.BIND_AUTO_CREATE);
    }


    /**
     * my BLE BroadcastReceiver
     */
    private BroadcastReceiver myBLEBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Global.ACTION_DEVICE_FOUND)) {
                receiveDeviceFound(intent);

            } else if (action.equals(Global.ACTION_GATT_CONNECTED)) {
                BLog.e("  ACTION_GATT_CONNECTED    Bluetooth connection is successful!");
                mService.scan(false);
            } else if (action.equals(Global.ACTION_GATT_CONNECTED_FAIL)) {
                BLog.e("      ACTION_GATT_CONNECTED_FAIL");
                if (mService != null) {
                    mService.scan(false);
                    mService.disconnect();
                }
            } else if (action.equals(Global.ACTION_GATT_DISCONNECTED)) {
                BLog.e("      ACTION_GATT_DISCONNECTED");
                beginScanDevice();
            } else if (action.equals(Global.ACTION_GATT_SERVICES_DISCOVERED)) {
                BLog.e("      ACTION_GATT_SERVICES_DISCOVERED");
            } else if (action.equals(Global.ACTION_GATT_SERVICES_DISCOVER_FAIL)) {
                BLog.e("      ACTION_GATT_SERVICES_DISCOVER_FAIL");
                beginScanDevice();
            } else if (action.equals(Global.ACTION_WRITE_DESCRIPTOR_SUCCESS)) {
                BLog.e("      ACTION_WRITE_DESCRIPTOR_SUCCESS  发送数据成功");
            } else if (action.equals(Global.ACTION_WRITE_DESCRIPTOR_FAIL)) {
                BLog.e("      ACTION_WRITE_DESCRIPTOR_FAIL");
            } else if (action.equals(Global.ACTION_WRITE_CHARACTERISTIC_SUCCESS)) {
                BLog.e("      ACTION_WRITE_CHARACTERISTIC_SUCCESS");
            } else if (action.equals(Global.ACTION_WRITE_CHARACTERISTIC_FAIL)) {
                BLog.e("      ACTION_WRITE_CHARACTERISTIC_FAIL");
            } else if (action.equals(Global.ACTION_READ_CHARACTERISTIC_BIND_SUCCESS)) {
                //               receiveReadCharacteristicBindSuccess(intent);
                BLog.e("      ACTION_READ_CHARACTERISTIC_BIND_SUCCESS");
            } else if (action.equals(Global.ACTION_READ_CHARACTERISTIC_BIND_FAIL)) {
                //           receiveReadCharacteristicBindFail();
                BLog.e("      ACTION_READ_CHARACTERISTIC_BIND_FAIL");
            } else if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                //          receiveBluetoothDisable(intent);
                BLog.e("      ACTION_STATE_CHANGED");
            } else if (action.equals(Global.ACTION_BLUETOOTH_ENABLE_CONFORM)) {
                //            receiveBluetoothEnable();
                BLog.e("      ACTION_BLUETOOTH_ENABLE_CONFORM");
            } else if (action.equals(Global.ACTION_DATA_AVAILABLE)) {
                //       receiveData(intent);
                BLog.e("      ACTION_DATA_AVAILABLE");
            }
        }
    };

    private void receiveDeviceFound(Intent intent) {
        if (intent != null) {
            Bundle data = intent.getExtras();
            BluetoothDevice device = data.getParcelable(BluetoothDevice.EXTRA_DEVICE);
            int rssi = data.getInt(BluetoothDevice.EXTRA_RSSI);
            String nam = device.getName();
            if (nam == null)
                return;
            if ("TimeBox-mini-light-xiaohuang".equals(device.getName())) {
                mService.connect(device.getAddress(), true);
            }
        }
    }

    /**
     * my ServiceConnection
     */
    private ServiceConnection myServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((SmartBLEService.LocalBinder) service).getService();
        }
    };


    @Override
    public void resultCallback(int requestCode, int resultCode, Intent data) {

        if (requestCode == Global.REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                BLog.d("enable bluetooth");
                Intent intent = new Intent();
                intent.setAction(Global.ACTION_BLUETOOTH_ENABLE_CONFORM);
                baseActivity.sendBroadcast(intent);
                BLog.d("send " + Global.ACTION_BLUETOOTH_ENABLE_CONFORM);

            } else {
                BLog.d("disable bluetooth");
                Toast.makeText(baseActivity, "Please_enable_bluetooth", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == Global.REQUEST_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                BLog.d("receive settings back");
//                profileID = SyncHelper.initProfileID(HomeActivity.this);
//                initBand();
//                goalStep = SyncHelper.initGoalStepValue(HomeActivity.this, profileID);
//
//                queryDataAndUpdateUI(date_query, date_today);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        baseActivity.unregisterReceiver(myBLEBroadcastReceiver);
        baseActivity.unbindService(myServiceConnection);
    }
}
