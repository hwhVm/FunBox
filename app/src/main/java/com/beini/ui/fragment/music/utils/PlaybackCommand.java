package com.beini.ui.fragment.music.utils;

import android.util.Log;

import com.beini.event.DLNAActionEvent;

import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.avtransport.callback.GetMediaInfo;
import org.fourthline.cling.support.avtransport.callback.GetPositionInfo;
import org.fourthline.cling.support.avtransport.callback.GetTransportInfo;
import org.fourthline.cling.support.avtransport.callback.Pause;
import org.fourthline.cling.support.avtransport.callback.Play;
import org.fourthline.cling.support.avtransport.callback.Seek;
import org.fourthline.cling.support.avtransport.callback.SetAVTransportURI;
import org.fourthline.cling.support.avtransport.callback.Stop;
import org.fourthline.cling.support.model.MediaInfo;
import org.fourthline.cling.support.model.PositionInfo;
import org.fourthline.cling.support.model.TransportInfo;
import org.fourthline.cling.support.model.TransportState;
import org.fourthline.cling.support.renderingcontrol.callback.GetVolume;
import org.fourthline.cling.support.renderingcontrol.callback.SetVolume;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by beini on 2017/2/20.
 */

public class PlaybackCommand {
    private static final String TAG = PlaybackCommand.class.getSimpleName();

    public static void playNewItem(final String uri, final String metadata) {
        Device device = SystemManager.getInstance().getSelectedDevice();
        //Check selected device
        if (device == null) return;

        final Service avtService = device.findService(SystemManager.AV_TRANSPORT_SERVICE);
        if (avtService != null) {
            final ControlPoint cp = SystemManager.getInstance().getControlPoint();
            cp.execute(new Stop(avtService) {
                @Override
                public void success(ActionInvocation invocation) {
                    cp.execute(new SetAVTransportURI(avtService, uri, metadata) {
                        @Override
                        public void success(ActionInvocation invocation) {
                            //Second,Set Play command.
                            cp.execute(new Play(avtService) {
                                @Override
                                public void success(ActionInvocation invocation) {
                                    Log.i(TAG, "PlayNewItem success:" + uri);
                                }

                                @Override
                                public void failure(ActionInvocation arg0, UpnpResponse arg1, String arg2) {
                                    Log.e(TAG, "playNewItem failed");
                                }
                            });
                        }

                        @Override
                        public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                        }
                    });
                }

                @Override
                public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                }
            });

        }
    }

    public static void play() {
        Device device = SystemManager.getInstance().getSelectedDevice();
        //Check selected device
        if (device == null) return;

        Service avtService = device.findService(SystemManager.AV_TRANSPORT_SERVICE);
        if (avtService != null) {
            ControlPoint cp = SystemManager.getInstance().getControlPoint();
            cp.execute(new Play(avtService) {
                @Override
                public void success(ActionInvocation invocation) {
                    Log.i(TAG, "Play success.");
                }

                @Override
                public void failure(ActionInvocation arg0, UpnpResponse arg1, String arg2) {
                    Log.e(TAG, "Play failed");
                }
            });
        }
    }

    public static void pause() {
        Device device = SystemManager.getInstance().getSelectedDevice();
        //Check selected device
        if (device == null) return;

        Service avtService = device.findService(SystemManager.AV_TRANSPORT_SERVICE);
        if (avtService != null) {
            ControlPoint cp = SystemManager.getInstance().getControlPoint();
            cp.execute(new Pause(avtService) {
                @Override
                public void success(ActionInvocation invocation) {
                    Log.i(TAG, "Pause success.");
                }

                @Override
                public void failure(ActionInvocation arg0, UpnpResponse arg1, String arg2) {
                    Log.e(TAG, "Pause failed");
                }
            });
        }
    }

    public static void stop() {
        Device device = SystemManager.getInstance().getSelectedDevice();
        //Check selected device
        if (device == null) return;

        Service avtService = device.findService(SystemManager.AV_TRANSPORT_SERVICE);
        if (avtService != null) {
            ControlPoint cp = SystemManager.getInstance().getControlPoint();
            cp.execute(new Stop(avtService) {
                @Override
                public void success(ActionInvocation invocation) {
                    Log.i(TAG, "Stop success.");
                }

                @Override
                public void failure(ActionInvocation arg0, UpnpResponse arg1, String arg2) {
                    Log.e(TAG, "Stop failed");
                }
            });
        }
    }

    /**
     * Seek
     * seek完成后通过handler重新启动position同步线程
     * @param relativeTimeTarget 要seek到的值,该值为已播放的相对时间如：01:15:03
     */
    public static void seek(String relativeTimeTarget) {
        Device device = SystemManager.getInstance().getSelectedDevice();
        //Check selected device
        if (device == null) return;

        Service avtService = device.findService(SystemManager.AV_TRANSPORT_SERVICE);
        if (avtService != null) {
            ControlPoint cp = SystemManager.getInstance().getControlPoint();
            cp.execute(new Seek(avtService, relativeTimeTarget) {
                @Override
                public void success(ActionInvocation invocation) {
                    Log.i(TAG, "Seek success.");
//                    //Delay 1 second to synchronize remote device rel_time and SeekBar progress value.
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
////                            handler.sendEmptyMessage(NowplayingFragment.RESUME_SEEKBAR_ACTION);
//                        }
//                    }, 1000);
                }

                @Override
                public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                    Log.e(TAG, "Seek failed");
                }
            });
        }
    }

    public static void getMediaInfo() {
        Device device = SystemManager.getInstance().getSelectedDevice();
        //Check selected device
        if (device == null) return;

        Service avtService = device.findService(SystemManager.AV_TRANSPORT_SERVICE);
        if (avtService != null) {
            ControlPoint cp = SystemManager.getInstance().getControlPoint();
            cp.execute(new GetMediaInfo(avtService) {

                @Override
                public void received(ActionInvocation invocation, MediaInfo mediaInfo) {
                    DLNAActionEvent event = new DLNAActionEvent(DLNAActionEvent.GET_MEDIA_INFO_ACTION);
                    EventBus.getDefault().postSticky(event);
                }

                @Override
                public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                    Log.e(TAG, "GetMediaInfo failed");
                }
            });
        }
    }

    public static void getPositionInfo() {
        Device device = SystemManager.getInstance().getSelectedDevice();
        //Check selected device
        if (device == null) return;

        Service avtService = device.findService(SystemManager.AV_TRANSPORT_SERVICE);
        if (avtService != null) {
            ControlPoint cp = SystemManager.getInstance().getControlPoint();
            cp.execute(new GetPositionInfo(avtService) {
                @Override
                public void received(ActionInvocation invocation, PositionInfo positionInfo) {
//                    Message msg = Message.obtain(handler, DLNAMainFragment.GET_POSITION_INFO_ACTION);
//                    msg.obj = positionInfo;
//                    msg.sendToTarget();
                    EventBus.getDefault().post(positionInfo);

                    String relTime = positionInfo.getRelTime();
                    String trackDuration = positionInfo.getTrackDuration();
                    Log.i(TAG, "relTime " + relTime + " trackDuration " + trackDuration);
                    if(relTime!=null && !relTime.equals("00:00:00") && relTime.equals(trackDuration)){
                        SystemManager.getInstance().playNext();

                        DLNAActionEvent event = new DLNAActionEvent(DLNAActionEvent.GET_POSITION_INFO_ACTION);
                        event.positionInfo = positionInfo;
                        EventBus.getDefault().post(event);
//                        Message msg2 = Message.obtain(handler, DLNAMainFragment.REFRESH_OBJECTS);
//                        msg2.obj = positionInfo;
//                        msg2.sendToTarget();
                    }
                }

                @Override
                public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                    Log.e(TAG, "GetPositionInfo failed");
                }
            });
        }
    }

    public static void getTransportInfo() {
        Device device = SystemManager.getInstance().getSelectedDevice();
        //Check selected device
        if (device == null) return;

        Service avtService = device.findService(SystemManager.AV_TRANSPORT_SERVICE);
        if (avtService != null) {
            ControlPoint cp = SystemManager.getInstance().getControlPoint();
            cp.execute(new GetTransportInfo(avtService) {

                @Override
                public void received(ActionInvocation invocation, TransportInfo transportInfo) {
                    TransportState ts = transportInfo.getCurrentTransportState();
                    Log.i(TAG, "TransportState:" + ts.getValue());

                    if (TransportState.PLAYING == ts) {
                        DLNAActionEvent event = new DLNAActionEvent(DLNAActionEvent.PLAY_ACTION);
                        EventBus.getDefault().post(event);
                    } else if (TransportState.PAUSED_PLAYBACK == ts) {
                        DLNAActionEvent event = new DLNAActionEvent(DLNAActionEvent.PAUSE_ACTION);
                        EventBus.getDefault().post(event);
                    } else if (TransportState.STOPPED == ts) {
                        DLNAActionEvent event = new DLNAActionEvent(DLNAActionEvent.STOP_ACTION);
                        EventBus.getDefault().post(event);
                    }
                }

                @Override
                public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                    Log.e(TAG, "GetTransportInfo failed");
                }
            });
        }
    }




    public static void getVolume() {
        Device device = SystemManager.getInstance().getSelectedDevice();
        //Check selected device
        if (device == null) return;

        Service rcService = device.findService(SystemManager.RENDERING_CONTROL_SERVICE);
        if (rcService != null) {
            ControlPoint cp = SystemManager.getInstance().getControlPoint();
            cp.execute(new GetVolume(rcService) {

                @Override
                public void received(ActionInvocation actionInvocation, int currentVolume) {
                    //Send currentVolume to handler.
//                    Log.i(TAG, "GetVolume:" + currentVolume);
//                    Message msg = Message.obtain(handler, DLNAMainFragment.GET_VOLUME_ACTION, currentVolume, 0);
//                    msg.sendToTarget();
//                    DLNAActionEvent event = new DLNAActionEvent(DLNAActionEvent.GET_VOLUME_ACTION);
//                    event.volValue = currentVolume;
//                    EventBus.getDefault().postSticky(event);
                    SystemManager.getInstance().setDeviceVolume(currentVolume);
                }

                @Override
                public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                    Log.e(TAG, "GetVolume failed");
                }
            });
        }
    }

//    public static void getPlayStatus(final Handler handler) {
//        Device device = SystemManager.getInstance().getSelectedDevice();
//        //Check selected device
//        if (device == null) return;
//
//        Service rcService = device.findService(SystemManager.RENDERING_CONTROL_SERVICE);
//        if (rcService != null) {
//            ControlPoint cp = SystemManager.getInstance().getControlPoint();
//            cp.execute(new GetMute(rcService) {
//                @Override
//                public void received(ActionInvocation actionInvocation, boolean b) {
//                    Log.i(TAG, "is_mute :" + b);
//                    int isPlay;
//                    if(b){
//                        isPlay = 0;
//                    }else {
//                        isPlay = 1;
//                    }
//
//                    Message msg = Message.obtain(handler, DLNAMainFragment.GET_PLAY_STATUS, isPlay, 0);
//                    msg.sendToTarget();
//                }
//
//                @Override
//                public void failure(ActionInvocation actionInvocation, UpnpResponse upnpResponse, String s) {
//
//                }
//            });
//
//        }
//    }

    public static void setVolume(int newVolume) {
        Device device = SystemManager.getInstance().getSelectedDevice();
        //Check selected device
        if (device == null) return;

        Service rcService = device.findService(SystemManager.RENDERING_CONTROL_SERVICE);
        if (rcService != null) {
            ControlPoint cp = SystemManager.getInstance().getControlPoint();
            cp.execute(new SetVolume(rcService, newVolume) {

                @Override
                public void success(ActionInvocation invocation) {
                    Log.i(TAG, "SetVolume success.");
                }

                @Override
                public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                    Log.e(TAG, "SetVolume failure.");
                }
            });
        }
    }
}
