package com.beini.ui.fragment.bluetooth.ble;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import com.beini.util.BLog;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SmartBLEService extends Service {

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;

    private String mBluetoothDeviceAddress;
    private BluetoothGattCharacteristic mCharWrite;
    private BluetoothGattCharacteristic mCharRead;

    private int mConnectionState = STATE_DISCONNECTED;

    public int getConnectionState() {
        return mConnectionState;
    }

    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;


    public class LocalBinder extends Binder {
        public SmartBLEService getService() {
            return SmartBLEService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    @Override
    public void onCreate() {
        initialize();
    }


    /**
     * 蓝牙回调函数
     */
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {

                if (status == BluetoothGatt.GATT_SUCCESS) {
                    BLog.d("Connected to GATT server.");
                    mConnectionState = STATE_CONNECTED;
                    broadcastUpdate(Global.ACTION_GATT_CONNECTED);

                    // Attempts to discover services after successful connection.
                    if (mBluetoothGatt != null) {
                        BLog.d("Attempting to start service discovery:" + mBluetoothGatt.discoverServices());
                    }
                } else {
                    mConnectionState = STATE_DISCONNECTED;
                    disconnect();
                    broadcastUpdate(Global.ACTION_GATT_CONNECTED_FAIL);
                    close();
                }


            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                BLog.d("Disconnected from GATT server.");
                mConnectionState = STATE_DISCONNECTED;
                mBluetoothDeviceAddress = null;
                broadcastUpdate(Global.ACTION_GATT_DISCONNECTED);
                close();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            BLog.d("onServicesDiscovered received: " + status);

            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(Global.ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                BLog.d("onServicesDiscovered received: " + status);
                broadcastUpdate(Global.ACTION_GATT_SERVICES_DISCOVER_FAIL);
            }
        }


        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            UUID uuid = characteristic.getUuid();
            BLog.d("       onCharacteristicRead ------------------>uuid==" + uuid);
//            if (uuid.equals(Global.UUID_CHARACTERISTIC_BIND_DEVICE)) {
//
//                if (status == BluetoothGatt.GATT_SUCCESS) {
//                    broadcastUpdate(ACTION_READ_CHARACTERISTIC_BIND_SUCCESS, KEY_BOUND_STATE, characteristic.getValue());
//
//                } else {
//                    broadcastUpdate(ACTION_READ_CHARACTERISTIC_BIND_FAIL);
//                }
//            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            UUID uuid = characteristic.getUuid();
            BLog.d("       onCharacteristicWrite ------------------>uuid==" + uuid);
//            if (uuid.equals(Global.UUID_CHARACTERISTIC_COMMUNICATION)) {
//
//                if (status == BluetoothGatt.GATT_SUCCESS) {
//                    broadcastUpdate(ACTION_WRITE_CHARACTERISTIC_SUCCESS);
//
//                } else {
//                    broadcastUpdate(ACTION_WRITE_CHARACTERISTIC_FAIL);
//                }
//
//            } else if (uuid.equals(Global.UUID_CHARACTERISTIC_BIND_DEVICE)) {
//                if (status == BluetoothGatt.GATT_SUCCESS) {
//                    broadcastUpdate(ACTION_WRITE_CHARACTERISTIC_BIND_SUCCESS);
//
//                } else {
//                    broadcastUpdate(ACTION_WRITE_CHARACTERISTIC_BIND_FAIL);
//                }
//            }

        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            BLog.d("返回数据" + Arrays.toString(characteristic.getValue()));
            SppDecodeHolder.decodeData(characteristic.getValue(), characteristic.getValue().length);
            broadcastUpdate(Global.ACTION_DATA_AVAILABLE, Global.KEY_HISTORY_HOUR_DATA, characteristic.getValue());
//			}

        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            BLog.d("    onDescriptorWrite  status      =" + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(Global.ACTION_WRITE_DESCRIPTOR_SUCCESS);

            } else {
                broadcastUpdate(Global.ACTION_WRITE_DESCRIPTOR_FAIL);
            }
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            BLog.d("rssi value: " + rssi);
        }
    };


    /**
     * 发送广播
     *
     * @param action
     */
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }


    private void broadcastUpdate(String action, String key, byte[] value) {

        Intent intent = new Intent(action);
        intent.putExtra(key, value);
        sendBroadcast(intent);
    }


    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                BLog.d("Unable to initialize BluetoothManager.");
                return false;
            }
        }
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            BLog.d("Unable to obtain a BluetoothAdapter.");
            return false;
        }
        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The
     * connection result is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public boolean connect(final String address, boolean is) {
        if (mBluetoothAdapter == null || address == null) {
            BLog.d("BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            BLog.d("Device not found.  Unable to connect.");
            return false;
        }
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        BLog.d("Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The
     * disconnection result is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            BLog.d("BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure
     * resources are released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }


    /**
     * 写特征值
     *
     * @param characteristic
     */
    public void wirteCharacteristic(BluetoothGattCharacteristic characteristic) {

        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            BLog.d("BluetoothAdapter not initialized");
            return;
        }

        mBluetoothGatt.writeCharacteristic(characteristic);

    }


    public void writeCharacteristic(byte[] value) {
        BLog.d("     writeCharacteristic  begin   ");
        List<BluetoothGattService> supportedGattServices = mBluetoothGatt.getServices();
        if (mBluetoothGatt.getServices() == null) {
            return;
        }
        for (int i = 0; i < supportedGattServices.size(); i++) {
            BLog.d("1:BluetoothGattService UUID=:" + supportedGattServices.get(i).getUuid());
            List<BluetoothGattCharacteristic> listGattCharacteristic = supportedGattServices.get(i).getCharacteristics();
            for (int j = 0; j < listGattCharacteristic.size(); j++) {
                int charaProp = listGattCharacteristic.get(i).getProperties();
                BLog.d("2:   BluetoothGattCharacteristic UUID=:" + listGattCharacteristic.get(j).getUuid() + "Prop " + charaProp);
            }
        }

        for (int i = 0; i < supportedGattServices.size(); i++) {
            List<BluetoothGattCharacteristic> listGattCharacteristic = supportedGattServices.get(i).getCharacteristics();
            for (int j = 0; j < listGattCharacteristic.size(); j++) {
                int charaProp = listGattCharacteristic.get(i).getProperties();
                UUID uuid = listGattCharacteristic.get(j).getUuid();
                uuid.toString();

                BLog.d("gattCharacteristic的UUID为:" + listGattCharacteristic.get(j).getUuid() + " charaProp " + charaProp);
                if ((charaProp == (BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE
                        | BluetoothGattCharacteristic.PROPERTY_NOTIFY) &&
                        //只有这个UUID可用
                        uuid.equals(UUID.fromString("49535343-1e4d-4bd9-ba61-23c647249616")))) {

                    BLog.d("gattCharacteristic的属性为:  可写通知 uuid " + uuid);
                    mCharWrite = listGattCharacteristic.get(j);
                    mCharRead = listGattCharacteristic.get(j);
                    {
                        mBluetoothGatt.setCharacteristicNotification(mCharRead, true);
                        //将指令放置进来
                        mCharRead.setValue(value);
                        //设置回复形式
                        mCharRead.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
                        //开始写数据
                        mBluetoothGatt.writeCharacteristic(mCharRead);
                    }
                    break;
                } else if ((charaProp == BluetoothGattCharacteristic.PROPERTY_READ)) {
                    BLog.d("gattCharacteristic的属性为:  可读通知");
//                            readUUID = supportedGattServices.get(i).getUuid();
                    //  mCharRead = listGattCharacteristic.get(i);
//                            connectFlag = 1;
                    break;
                }
            }
        }
    }

    /**
     * 扫描设备
     *
     * @param start
     */
    public void scan(boolean start) {
        if (mBluetoothAdapter != null) {
            if (start) {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        } else {
            BLog.d("bluetoothadapter is null");
        }
    }

    /**
     * 扫描设备的回调方法
     */
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi,
                             byte[] scanRecord) {
            Bundle mBundle = new Bundle();
            mBundle.putParcelable(BluetoothDevice.EXTRA_DEVICE, device);
            mBundle.putInt(BluetoothDevice.EXTRA_RSSI, rssi);
            if (device.getName() == null) {
                return;
            }
            BLog.d("   device.getName()=" + device.getName() + "   device.getAddress()=" + device.getAddress());
            Intent intent = new Intent();
            intent.setAction(Global.ACTION_DEVICE_FOUND);
            intent.putExtras(mBundle);
            sendBroadcast(intent);
        }
    };


    // get set
    public String getDeviceAddress() {
        return mBluetoothDeviceAddress;
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This
     * should be invoked only after {@code BluetoothGatt#discoverServices()}
     * completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null)
            return null;

        return mBluetoothGatt.getServices();
    }

    /**
     * Read the RSSI for a connected remote device.
     */
    public boolean getRssiVal() {
        if (mBluetoothGatt == null)
            return false;

        return mBluetoothGatt.readRemoteRssi();
    }

}
