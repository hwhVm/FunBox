package com.example.administrator.baseapp.ui.fragment.bluetooth;

import com.example.administrator.baseapp.ui.fragment.bluetooth.ble.SppDecodeHolder;
import com.example.administrator.baseapp.ui.fragment.bluetooth.ble.SppProc;

/**
 * Created by beini on 2017/3/17.
 */

public class BleModel {

    public static byte[] setLightCmd(SppProc.LIGHT_MODE mode, byte type, byte time_r, byte time_g, byte time_b) {
        byte[] cmd_data = new byte[6];

        cmd_data[0] = mode.getCmd_data()[0];
        cmd_data[1] = type;
        cmd_data[2] = time_r;
        cmd_data[3] = time_g;
        cmd_data[4] = time_b;
        byte[] cmd = SppDecodeHolder.encodeCmd(SppDecodeHolder.CMD_TYPE.SPP_SET_BOX_MODE, cmd_data);
        return cmd;
    }
}
