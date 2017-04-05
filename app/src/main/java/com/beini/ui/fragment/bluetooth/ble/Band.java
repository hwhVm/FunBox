package com.beini.ui.fragment.bluetooth.ble;

/**
 * Created by beini on 2017/3/17.
 */

public class Band {
    private int deviceID;
    private String address;
    private String name;
    public int getDeviceID() {
        return deviceID;
    }
    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
