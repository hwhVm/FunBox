package com.beini.ui.fragment.music.utils;

import android.content.Intent;
import android.os.IBinder;

import com.beini.constants.Constants;
import com.beini.ui.fragment.music.service.BeyondContentDirectoryService;

import org.fourthline.cling.UpnpServiceConfiguration;
import org.fourthline.cling.android.AndroidUpnpServiceConfiguration;
import org.fourthline.cling.android.AndroidUpnpServiceImpl;
import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.model.DefaultServiceManager;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.DeviceDetails;
import org.fourthline.cling.model.meta.DeviceIdentity;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDN;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.transport.impl.AsyncServletStreamServerConfigurationImpl;
import org.fourthline.cling.transport.impl.AsyncServletStreamServerImpl;
import org.fourthline.cling.transport.spi.NetworkAddressFactory;
import org.fourthline.cling.transport.spi.StreamServer;

import java.util.UUID;

/**
 * Created by bieni on 2017/2/20.
 */

public class DivoomUpnpService extends AndroidUpnpServiceImpl {

    private static final String TAG = DivoomUpnpService.class.getSimpleName();
    private LocalDevice mLocalDevice = null;

    @Override
    public void onCreate() {
        super.onCreate();

        //Create LocalDevice
        LocalService localService = new AnnotationLocalServiceBinder().read(BeyondContentDirectoryService.class);
        localService.setManager(new DefaultServiceManager<>(
                localService, BeyondContentDirectoryService.class));

        String macAddress = Utils.getMACAddress(Utils.WLAN0);
        //Generate UUID by MAC address
        UDN udn = UDN.valueOf(UUID.nameUUIDFromBytes(macAddress.getBytes()).toString());

        try {
            mLocalDevice = new LocalDevice(new DeviceIdentity(udn), new UDADeviceType("MediaServer"),
                    new DeviceDetails(Constants.Media_SERVER), new LocalService[]{localService});
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        upnpService.getRegistry().addDevice(mLocalDevice);

        //LocalBinder instead of binder
        binder = new LocalBinder();


        SystemManager systemManager = SystemManager.getInstance();

        systemManager.setUpnpService(this);
        //Search on service created.
        systemManager.searchAllDevices();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected UpnpServiceConfiguration createConfiguration() {
//        return new FixedAndroidUpnpServiceConfiguration();
        return new AndroidUpnpServiceConfiguration(){
            @Override
            public StreamServer createStreamServer(NetworkAddressFactory networkAddressFactory) {
                // Use Jetty, start/stop a new shared instance of JettyServletContainer
                return new AsyncServletStreamServerImpl(
                        new AsyncServletStreamServerConfigurationImpl(
                                AndroidJettyServletContainer.INSTANCE,
                                networkAddressFactory.getStreamListenPort()
                        )
                );
            }
        };
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public LocalDevice getLocalDevice() {
        return mLocalDevice;
    }

//    class FixedAndroidUpnpServiceConfiguration extends AndroidUpnpServiceConfiguration {
//        @Override
//        public StreamServer createStreamServer(NetworkAddressFactory networkAddressFactory) {
//            // Use Jetty, start/stop a new shared instance of JettyServletContainer
//			return null;
//            /*return new AsyncServletStreamServerImpl(
//                    new AsyncServletStreamServerConfigurationImpl(
//                            AndroidJettyServletContainer.INSTANCE,
//                            networkAddressFactory.getStreamListenPort()
//                    )
//            );*/
//        }
//    }

    public UpnpServiceConfiguration getConfiguration() {
        return upnpService.getConfiguration();
    }

    public Registry getRegistry() {
        return upnpService.getRegistry();
    }

    public ControlPoint getControlPoint() {
        return upnpService.getControlPoint();
    }

    public class LocalBinder extends Binder {
        public DivoomUpnpService getService() {
            return DivoomUpnpService.this;
        }
    }
}
