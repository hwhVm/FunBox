package com.beini.ui.fragment.bluetooth.tdble;

import android.bluetooth.BluetoothDevice;


public interface IBlueHandle {

    void start(BluetoothDevice device);
    boolean connect();
    void close();
    int read(byte[] data);
    void write(byte[] buffer);
    void cancel();
    BluetoothDevice getDevice();
}
