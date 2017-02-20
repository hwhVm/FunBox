package com.example.administrator.baseapp.ui.fragment.music;

import com.example.administrator.baseapp.ui.fragment.SystemService;

import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.item.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by beini on 2017/2/20.
 */

public class SystemManager {
    private static final String TAG = SystemManager.class.getSimpleName();
    public static final ServiceType CONTENT_DIRECTORY_SERVICE = new UDAServiceType("ContentDirectory");
    public static final ServiceType AV_TRANSPORT_SERVICE = new UDAServiceType("AVTransport");
    public static final ServiceType RENDERING_CONTROL_SERVICE = new UDAServiceType("RenderingControl");
    private DeviceType dmrDeviceType = new UDADeviceType("MediaRenderer");

    private static SystemManager INSTANCE = null;
    //Service
    private DivoomUpnpService mUpnpService;
    private SystemService mSystemService;

    private SystemManager() {
    }

    public static SystemManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SystemManager();
        }
        return INSTANCE;
    }

    public void setUpnpService(DivoomUpnpService upnpService) {
        mUpnpService = upnpService;
    }

    public void setSystemService(SystemService systemService) {
        mSystemService = systemService;
    }

    public DIDLContent getPlayListItems() {
        if(mSystemService!=null){
            return mSystemService.getPlayListItems();
        }
        return null;
    }

    public void searchAllDevices() {
        mUpnpService.getControlPoint().search();
    }

    public Collection<Device> getDmrDevices() {
        return mUpnpService.getRegistry().getDevices(dmrDeviceType);
    }

    public ControlPoint getControlPoint() {
        return mUpnpService.getControlPoint();
    }

    public Registry getRegistry() {
        return mUpnpService.getRegistry();
    }

    public Collection<Device> getDmcDevices() {
        if (mUpnpService == null) return Collections.EMPTY_LIST;

        List<Device> devices = new ArrayList<>();
        devices.addAll(mUpnpService.getRegistry().getDevices(CONTENT_DIRECTORY_SERVICE));
        return devices;
    }

    public Device getSelectedDevice() {
        if(mSystemService!=null) {
            return mSystemService.getSelectedDevice();
        }
        else
            return null;
    }

    public void setSelectedDevice(Device selectedDevice) {
        if(mSystemService!=null) {
            mSystemService.setSelectedDevice(selectedDevice, mUpnpService.getControlPoint());
        }
    }

    public int getDeviceVolume() {

        if(mSystemService!=null) {
            return mSystemService.getDeviceVolume();
        }
        else
            return 0;
    }

    public void setDeviceVolume(int currentVolume) {
        if(mSystemService!=null) {
            mSystemService.setDeviceVolume(currentVolume);
            PlaybackCommand.setVolume(currentVolume);
        }
    }

    public void destroy(){

    }

    public Item getCurSong(){
        if(mSystemService!=null) {
            return mSystemService.getCurSong();
        }
        else
            return null;
    }
    public boolean playSong(boolean is_play){
        if(mSystemService!=null) {
            return mSystemService.playSong(is_play);
        }
        else
            return false;
    }
    public void stopSong(){
        if(mSystemService!=null) {
            mSystemService.stopSong();
        }
    }
    //    public void playItem(Item item){
//        if(mSystemService!=null) {
//            mSystemService.playItem(item);
//        }
//    }
    public void playItemByNum(int num){
        if(mSystemService!=null) {
            mSystemService.playItemByNum(num);
        }
    }

    public void playNext(){
        if(mSystemService!=null) {
            mSystemService.playNext();
        }
    }

    public void playPrev(){
        if(mSystemService!=null) {
            mSystemService.playPrev();
        }
    }
}
