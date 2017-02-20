package com.example.administrator.baseapp.ui.fragment;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.administrator.baseapp.constants.Constants;
import com.example.administrator.baseapp.event.DLNAActionEvent;
import com.example.administrator.baseapp.ui.fragment.music.Intents;
import com.example.administrator.baseapp.ui.fragment.music.JettyResourceServer;
import com.example.administrator.baseapp.ui.fragment.music.PlaybackCommand;
import com.example.administrator.baseapp.ui.fragment.music.SPUtils;
import com.example.administrator.baseapp.ui.fragment.music.SystemManager;

import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.controlpoint.SubscriptionCallback;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.state.StateVariableValue;
import org.fourthline.cling.model.types.UDN;
import org.fourthline.cling.support.avtransport.lastchange.AVTransportLastChangeParser;
import org.fourthline.cling.support.avtransport.lastchange.AVTransportVariable;
import org.fourthline.cling.support.contentdirectory.DIDLParser;
import org.fourthline.cling.support.contentdirectory.callback.Browse;
import org.fourthline.cling.support.lastchange.EventedValueString;
import org.fourthline.cling.support.lastchange.LastChange;
import org.fourthline.cling.support.model.BrowseFlag;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.Res;
import org.fourthline.cling.support.model.SortCriterion;
import org.fourthline.cling.support.model.TransportState;
import org.fourthline.cling.support.model.item.Item;
import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by beini on 2017/2/20.
 */

public class SystemService extends Service {
    private static final String TAG = "SystemService";

    private Binder binder = new LocalBinder();
    private Device mSelectedDevice;
    private int mDeviceVolume;
    private AVTransportSubscriptionCallback mAVTransportSubscriptionCallback;

    //Jetty DMS Server
    private ExecutorService mThreadPool = Executors.newCachedThreadPool();
    private JettyResourceServer mJettyResourceServer;
    private DIDLContent mDidl = null;
    private boolean is_stop = true;
    //    private Item mCurSong = null;
    private int mCurSongNum = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        //Start Local Server
        mJettyResourceServer = new JettyResourceServer();
        mThreadPool.execute(mJettyResourceServer);

        SystemManager systemManager = SystemManager.getInstance();
        systemManager.setSystemService(this);
        LoadLocalMusic();
    }

    @Override
    public void onDestroy() {
        //End all subscriptions
        if (mAVTransportSubscriptionCallback != null)
            mAVTransportSubscriptionCallback.end();

        //Stop Jetty
        mJettyResourceServer.stopIfRunning();

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        public SystemService getService() {
            return SystemService.this;
        }
    }

    public Device getSelectedDevice() {
        return mSelectedDevice;
    }

    public void setSelectedDevice(Device selectedDevice, ControlPoint controlPoint) {
//        if (selectedDevice == mSelectedDevice) return;

        Log.i(TAG, "Change selected device.");
        mSelectedDevice = selectedDevice;
        //End last device's subscriptions
        if (mAVTransportSubscriptionCallback != null) {
            mAVTransportSubscriptionCallback.end();
        }
        //Init Subscriptions
        mAVTransportSubscriptionCallback = new AVTransportSubscriptionCallback(mSelectedDevice.findService(SystemManager.AV_TRANSPORT_SERVICE));
        controlPoint.execute(mAVTransportSubscriptionCallback);

        EventBus.getDefault().post(new DLNAActionEvent(DLNAActionEvent.REFRESH_OBJECTS));
    }

    public int getDeviceVolume() {
        return mDeviceVolume;
    }

    public void setDeviceVolume(int currentVolume) {
        mDeviceVolume = currentVolume;
    }

    private class AVTransportSubscriptionCallback extends SubscriptionCallback {

        protected AVTransportSubscriptionCallback(org.fourthline.cling.model.meta.Service service) {
            super(service);
        }

        @Override
        protected void failed(GENASubscription subscription, UpnpResponse responseStatus, Exception exception, String defaultMsg) {
            Log.e(TAG, "AVTransportSubscriptionCallback failed.");
        }

        @Override
        protected void established(GENASubscription subscription) {
        }

        @Override
        protected void ended(GENASubscription subscription, CancelReason reason, UpnpResponse responseStatus) {
            Log.i(TAG, "AVTransportSubscriptionCallback ended.");
        }

        @Override
        protected void eventReceived(GENASubscription subscription) {
            Map<String, StateVariableValue> values = subscription.getCurrentValues();
            if (values != null && values.containsKey("LastChange")) {
                String lastChangeValue = values.get("LastChange").toString();
                Log.i(TAG, "LastChange:" + lastChangeValue);
                LastChange lastChange;
                try {
                    lastChange = new LastChange(new AVTransportLastChangeParser(), lastChangeValue);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                //Parse TransportState value.
                AVTransportVariable.TransportState transportState = lastChange.getEventedValue(0, AVTransportVariable.TransportState.class);
                if (transportState != null) {
                    TransportState ts = transportState.getValue();
                    if (ts == TransportState.PLAYING) {
                        DLNAActionEvent event = new DLNAActionEvent(DLNAActionEvent.PLAY_ACTION);
                        EventBus.getDefault().post(event);
                    } else if (ts == TransportState.PAUSED_PLAYBACK) {
                        DLNAActionEvent event = new DLNAActionEvent(DLNAActionEvent.PAUSE_ACTION);
                        EventBus.getDefault().post(event);
                    } else if (ts == TransportState.STOPPED) {
                        DLNAActionEvent event = new DLNAActionEvent(DLNAActionEvent.STOP_ACTION);
                        EventBus.getDefault().post(event);
                    }
                }

                //Parse CurrentTrackMetaData value.
                EventedValueString currentTrackMetaData = lastChange.getEventedValue(0, AVTransportVariable.CurrentTrackMetaData.class);
                if (currentTrackMetaData != null && currentTrackMetaData.getValue() != null) {
                    DIDLParser didlParser = new DIDLParser();
                    Intent lastChangeIntent;
                    try {
                        DIDLContent content = didlParser.parse(currentTrackMetaData.getValue());
                        Item item = content.getItems().get(0);
                        String creator = item.getCreator();
                        String title = item.getTitle();

                        lastChangeIntent = new Intent(Intents.ACTION_UPDATE_LAST_CHANGE);
                        lastChangeIntent.putExtra("creator", creator);
                        lastChangeIntent.putExtra("title", title);
                    } catch (Exception e) {
                        Log.e(TAG, "Parse CurrentTrackMetaData error.");
                        lastChangeIntent = null;
                    }

                    if (lastChangeIntent != null)
                        sendBroadcast(lastChangeIntent);
                }
            }
        }

        @Override
        protected void eventsMissed(GENASubscription subscription, int numberOfMissedEvents) {
        }
    }


    private void LoadLocalMusic(){
        Device device = getLocalDevice();
        if(device != null){
            String IdentifierString = device.getIdentity().getUdn().getIdentifierString();
            String objectId = "0";
            loadContent(IdentifierString, objectId);
        }
    }

    private Device getLocalDevice(){
        List<Device> devices = (List<Device>)SystemManager.getInstance().getDmcDevices();
        for(int i=0;i<devices.size();i++) {
            Device device = devices.get(i);
            String name = device.getDetails().getFriendlyName();
            Log.i(TAG, "name " + name);
            Log.i(TAG, "get " + Constants.Media_SERVER);
//            if(name.equals("Divoom媒体服务器"))
            if( Constants.Media_SERVER.equals(name))
            {
                Log.i(TAG, "return device");
                return device;
            }
        }
        return null;
    }

    /**
     * 在这里读取数据，完成后通过handler发送消息，然后刷新界面。
     */
    private void loadContent(String IdentifierString, String objectId) {
        SystemManager systemManager = SystemManager.getInstance();
        Device device = null;
        try {
            device = systemManager.getRegistry().getDevice(new UDN(IdentifierString), false);
        } catch (NullPointerException e) {
            Log.e(TAG, "Get device error.");
        }

        if (device != null) {
            //Get cds to browse children directories.
            org.fourthline.cling.model.meta.Service contentDeviceService = device.findService(SystemManager.CONTENT_DIRECTORY_SERVICE);
            //Execute Browse action and init list view
            systemManager.getControlPoint().execute(new Browse(contentDeviceService, objectId, BrowseFlag.DIRECT_CHILDREN, "*", 0,
                    null, new SortCriterion(true, "dc:title")) {
                @Override
                public void received(ActionInvocation actionInvocation, DIDLContent didl) {
                    // TODO 自动判断音频服务
                    //didl
                    mDidl = didl;
                    initCurSong();

                    Log.i(TAG, "send EventBus REFRESH");
                    EventBus.getDefault().post(new DLNAActionEvent(DLNAActionEvent.REFRESH_OBJECTS));
                }

                @Override
                public void updateStatus(Status status) {
                }

                @Override
                public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                }
            });
        }
    }

    public DIDLContent getPlayListItems(){
        return mDidl;
    }


//    public void playItem(Item item){
//        if (item == null) return;
//        is_stop = false;
//        mCurSong = item;
//        Res res = item.getFirstResource();
//        String uri = res.getValue();
//
//        DIDLContent content = new DIDLContent();
//        content.addItem(item);
//        DIDLParser didlParser = new DIDLParser();
//        String metadata = null;
//        try {
//            metadata = didlParser.generate(content);
//        } catch (Exception e) {
//            //ignore
//        }
//        //Log.d(TAG,"Item metadata:" + metadata);
//        //Play on the selected device.
//        PlaybackCommand.playNewItem(uri, metadata);
//    }

    public void playItemByNum(int num){
        DIDLContent didlContent = getPlayListItems();
        List<Item> items = didlContent.getItems();
        Item item = items.get(num);
        mCurSongNum = num;

        if (item == null) return;
        is_stop = false;
//        mCurSong = item;
        Res res = item.getFirstResource();
        String uri = res.getValue();

        DIDLContent content = new DIDLContent();
        content.addItem(item);
        DIDLParser didlParser = new DIDLParser();
        String metadata = null;
        try {
            metadata = didlParser.generate(content);
        } catch (Exception e) {
            //ignore
        }
        //Log.d(TAG,"Item metadata:" + metadata);
        //Play on the selected device.
        PlaybackCommand.playNewItem(uri, metadata);
    }

    public Item getCurSong() {
//        return mCurSong;
        DIDLContent didlContent = getPlayListItems();
        if(didlContent!=null) {
            List<Item> items = didlContent.getItems();
            Item item = items.get(mCurSongNum);
            return item;
        }
        else
            return null;
    }

    public void initCurSong() {
        DIDLContent didlContent = getPlayListItems();
        if (didlContent != null) {
            List<Item> items = didlContent.getItems();

            String song = SPUtils.getPlaySong();
            if (!song.equals("")) {
                Item item = items.get(0);
                SPUtils.SavePlaySong(item.getTitle());
//                DLog.i(TAG, "SaveSong " + item.getTitle());
//                mCurSong = item;
                mCurSongNum = 0;
            } else {
                for (int i = 0; i < items.size(); i++) {
                    Item item = items.get(i);
                    if (song.equals(item.getTitle())) {
//                        mCurSong = item;
                        mCurSongNum = i;
                        break;
                    }
                }
//                if (mCurSong == null) {
//                    mCurSong = items.get(0);
//                    mCurSongNum = 0;
//                }
            }
        }
    }
    public boolean playSong(boolean is_play){
//        if(mCurSong != null){
        if(is_play) {
            if(is_stop) {
                playItemByNum(mCurSongNum);
            }
            PlaybackCommand.play();
        }
        else {
            PlaybackCommand.pause();
        }
        return true;
//        }else{
//
//            return false;
//        }
    }
    public void stopSong(){
        is_stop = true;
    }

    public void playNext() {
        DIDLContent didlContent = getPlayListItems();
        List<Item> items = didlContent.getItems();
        if(items.size() != 0) {
            if (mCurSongNum + 1 >= items.size()) {
                mCurSongNum = 0;
            } else {
                mCurSongNum = mCurSongNum + 1;
            }
            playItemByNum(mCurSongNum);
        }
    }
    public void playPrev() {
        DIDLContent didlContent = getPlayListItems();
        List<Item> items = didlContent.getItems();
        if(items.size() != 0) {
            if (mCurSongNum == 0){
                mCurSongNum = items.size()-1;
            } else {
                mCurSongNum = mCurSongNum - 1;
            }
            playItemByNum(mCurSongNum);
        }
    }
}
