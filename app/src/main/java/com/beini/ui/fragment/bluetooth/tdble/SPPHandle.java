package com.beini.ui.fragment.bluetooth.tdble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.beini.utils.BLog;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class SPPHandle implements IBlueHandle {

    // 本应用的唯一 UUID
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothSocket myBtSocket=null;
    private InputStream mInStream=null;
    private final String TAG = "Blue.SPPHandle";
    private OutputStream mOutStream=null;
    private BluetoothDevice mDevice;

    @Override
    public void start(BluetoothDevice device) {
        mDevice = device;
        // 通过正在连接的设备获取BluetoothSocket
        try {
            //device.createRfcommSocketToServiceRecord(MY_UUID);//2.3系统以下
            myBtSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);//2.3系统以上使用
            // 获取BluetoothSocket的输入输出流
            try {
                mInStream  = myBtSocket.getInputStream();
                mOutStream = myBtSocket.getOutputStream();
                //BLog.d( "socket.getMaxReceivePacketSize()" + socket.getMaxReceivePacketSize());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**尝试3次
     * @throws IOException
     */
    @Override
    public boolean connect(){
        if(myBtSocket!=null) {
            BLog.d( "Start connect");
            try {
                myBtSocket.connect();// 尝试连接
                BLog.d( "connect ok");
            } catch (IOException e) {
                e.printStackTrace();
                BLog.d( "connect fail");
                return false;
            }
        }
        return true;
    }

    @Override
    public void close(){
        if(myBtSocket!=null) {
            try {
                myBtSocket.close();
                myBtSocket=null;
            } catch (IOException e) {
                e.printStackTrace();
                myBtSocket=null;
            }
        }
    }

    @Override
    public int read(byte[] data){
        if(myBtSocket!=null) {
            try {
                return mInStream.read(data);// 从输入流中读入数据
            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            }
        }
        else{
            return 0;
        }
    }

	@Override
	public void write(byte[] buffer) {
		try {
			mOutStream.write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void cancel() {
		try {
			myBtSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    @Override
    public BluetoothDevice getDevice() {
        return mDevice;
    }

}
