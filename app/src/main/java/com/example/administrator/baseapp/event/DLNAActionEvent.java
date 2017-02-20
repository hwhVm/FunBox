package com.example.administrator.baseapp.event;

import org.fourthline.cling.support.model.MediaInfo;
import org.fourthline.cling.support.model.PositionInfo;

/**
 * Created by beini on 2017/2/20.
 */

public class DLNAActionEvent {
    public static final int PLAY_ACTION = 0xa1;
    public static final int PAUSE_ACTION = 0xa2;
    public static final int STOP_ACTION = 0xa3;
    public static final int GET_MEDIA_INFO_ACTION = 0xa4;
    public static final int GET_POSITION_INFO_ACTION = 0xa5;
    public static final int RESUME_SEEKBAR_ACTION = 0xa6;
    public static final int GET_VOLUME_ACTION = 0xa7;
    public static final int SET_VOLUME_ACTION = 0xa8;
    public static final int GET_PLAY_STATUS = 0xa9;
    public static final int REFRESH_OBJECTS = 0xaa;

    public DLNAActionEvent(int action){
        this.action = action;
    }
    public int action;
    public MediaInfo mediaInfo;
    public PositionInfo positionInfo;
    public int volValue;
    public boolean isPlay;
}
