package com.beini.ui.fragment.bluetooth.tdble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;

import com.beini.base.BaseApplication;
import com.beini.utils.BLog;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


public class BLEHandle implements IBlueHandle {
    private final String TAG = "Blue.BLEHandle ";
    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattCharacteristic mCharWrite;
//    private BluetoothGattCharacteristic mCharNotify;

    private BluetoothGattCharacteristic mCharRead;
    private int connectFlag = 0; //0,未连接， 1连接成功， 2连接失败
    private LinkedBlockingQueue<byte[]> dataQueue=new LinkedBlockingQueue<>();

    //write status队列
    private LinkedBlockingQueue<Boolean> writeStatusQueue=new LinkedBlockingQueue<>(1);

    private void putDataQueue(byte[] data) {
        try {
            dataQueue.put(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private byte[] takeDataQueue() {
        try {
            return dataQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean write_ok = false;
    private BluetoothDevice mDevice;

    public BluetoothDevice getDevice(){
        return mDevice;
    }


    @Override
    public void start(BluetoothDevice device) {
        mDevice = device;
       BLog.d( "mBluetoothGatt start");
        mBluetoothGatt = device.connectGatt(BaseApplication.getInstance().getBaseContext(), true, mGattCallback);
       BLog.d( "mBluetoothGatt start ok ");
    }
    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        //连接状态改变的回调
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState) {
           BLog.d( "status " + status + "newState " + newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // 连接成功后启动服务发现
               BLog.d( "启动服务发现:" + mBluetoothGatt.discoverServices());
            }
            else{
                connectFlag = 2;
            }
        };

        //发现服务的回调
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
               BLog.d( "成功发现服务");

                UUID writeUUID = null;
                UUID readUUID = null;
                List<BluetoothGattService> supportedGattServices =mBluetoothGatt.getServices();
                for(int i=0;i<supportedGattServices.size();i++){
                   BLog.d( "1:BluetoothGattService UUID=:"+supportedGattServices.get(i).getUuid());
                    List<BluetoothGattCharacteristic> listGattCharacteristic=supportedGattServices.get(i).getCharacteristics();
                    for(int j=0;j<listGattCharacteristic.size();j++){
                        int charaProp = listGattCharacteristic.get(i).getProperties();

                       BLog.d( "2:   BluetoothGattCharacteristic UUID=:"+listGattCharacteristic.get(j).getUuid() + "Prop " + charaProp);

//                       BLog.d( "2:   " + supportedGattServices.get(i).getUuid() + " charaProp " + charaProp);

                    }
                }
                for(int i=0;i<supportedGattServices.size();i++){
                    List<BluetoothGattCharacteristic> listGattCharacteristic=supportedGattServices.get(i).getCharacteristics();
                    for(int j=0;j<listGattCharacteristic.size();j++) {
                        int charaProp = listGattCharacteristic.get(i).getProperties();
                        UUID uuid = listGattCharacteristic.get(j).getUuid();
                        uuid.toString();

                       BLog.d( "gattCharacteristic的UUID为:" + listGattCharacteristic.get(j).getUuid() + " charaProp " + charaProp);
                        if ((charaProp == (BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE
                                | BluetoothGattCharacteristic.PROPERTY_NOTIFY ) &&
                                //只有这个UUID可用
                                uuid.equals(UUID.fromString("49535343-1e4d-4bd9-ba61-23c647249616")))) {

                           BLog.d( "gattCharacteristic的属性为:  可写通知 uuid " + uuid);
                            mCharWrite = listGattCharacteristic.get(j);
                            mCharRead = listGattCharacteristic.get(j);
                            connectFlag = 1;
                            {
                                mBluetoothGatt.setCharacteristicNotification(mCharRead, true);
                                //将指令放置进来
                                mCharRead.setValue(new byte[]{0x7e, 0x14, 0x00, 0x00, 0x00, (byte) 0xaa});
                                //设置回复形式
                                mCharRead.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
                                //开始写数据
                                mBluetoothGatt.writeCharacteristic(mCharRead);
                            }
                            break;
                        }
                        else if ((charaProp == BluetoothGattCharacteristic.PROPERTY_READ)) {
                           BLog.d( "gattCharacteristic的属性为:  可读通知");
//                            readUUID = supportedGattServices.get(i).getUuid();
                            //  mCharRead = listGattCharacteristic.get(i);
//                            connectFlag = 1;
                            break;
                        }
                    }
                }
                if(connectFlag==1){
                    return;
                }
               BLog.d( "连接失败");
                connectFlag = 2;
            }else{
               BLog.d( "服务发现失败，错误码为:" + status);
                connectFlag = 2;
            }
        };

        //写操作的回调
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
//               BLog.d( "写入成功" + StringUtils.getHex(characteristic.getValue()));
               BLog.d( "写入成功");
                write_ok = true;
                try {
                    writeStatusQueue.put(true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        //读操作的回调
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
//               BLog.d( "读取成功" + StringUtils.getHex(characteristic.getValue()));
               BLog.d( "读取成功" );
            }
        }

        //数据返回的回调（此处接收BLE设备返回数据）
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
//           BLog.d( "数据返回" + StringUtils.getHex(characteristic.getValue()));
            BLog.d( "数据返回" );
            putDataQueue(characteristic.getValue());
        }

    };


    @Override
    public boolean connect(){
       BLog.d( "connect");
        mBluetoothGatt.connect();
        int count = 0;
        while(true){
            //10秒超时
            if(count++>200){
                return false;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(connectFlag==1){
                connectFlag = 0;
                return true;
            }
            else if(connectFlag == 2){
                connectFlag = 0;
                return false;
            }
        }
    }

    @Override
    public void close(){

        if(mBluetoothGatt!=null)
            mBluetoothGatt.close();
        mBluetoothGatt = null;
    }




    @Override
    public int read(byte[] data){
        while (true) {
            mBluetoothGatt.readCharacteristic(mCharRead);
           BLog.d( "read");
            byte[] tmp_data = takeDataQueue();
            if(tmp_data!=null){
                System.arraycopy(tmp_data, 0, data, 0, tmp_data.length);
                return tmp_data.length;
            }else{
                return 0;
            }
        }
    }

    //最多112字节
    @Override
    public void write(byte[] buffer) {
        final int MAX_BUFFER_LEN = 112;
        int buffer_offset = 0;
        int len = 0;
        if(mBluetoothGatt!=null && mCharWrite != null) {
            while (true) {
                if((buffer.length-buffer_offset)>MAX_BUFFER_LEN){
                    len = MAX_BUFFER_LEN;
                }
                else{
                    len = buffer.length-buffer_offset;
                }
                byte[] tmp_buffer = new byte[len];
                System.arraycopy(buffer, buffer_offset, tmp_buffer, 0, len);
                buffer_offset+=len;

                write_ok = false;
                mCharWrite.setValue(tmp_buffer);
//               BLog.d( "write " + StringUtils.getHex(buffer));
                //设置回复形式
                mCharWrite.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
                //开始写数据
                mBluetoothGatt.writeCharacteristic(mCharWrite);

                long beginTime= System.currentTimeMillis();
                try {
                    writeStatusQueue.poll(200, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
               BLog.d( "获取queue的时间为： " + (System.currentTimeMillis() - beginTime));

//                for(int i=0;i<20;i++) {
//                    try {
//                        Thread.sleep(10);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    if(write_ok) {
//                       BLog.d( "Because write_ok, break");
//                        break;
//                    }
//                    if(i == 19){
//                       BLog.d( "Timeout, break");
//                    }
//                }

                if(buffer_offset>=buffer.length){
                    break;
                }
            }
        }
    }

    @Override
    public void cancel() {

        if(mBluetoothGatt!=null){
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }
    }
}
