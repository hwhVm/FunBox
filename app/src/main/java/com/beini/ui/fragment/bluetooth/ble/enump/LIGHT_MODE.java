package com.beini.ui.fragment.bluetooth.ble.enump;

/**
 * Created by beini on 2017/3/17.
 */

public enum LIGHT_MODE {
    CLOCK(new byte[]{0x0}),
    TEMPRETURE(new byte[]{0x1}),
    COLOR_LIGHT(new byte[]{0x2}),
    SPECIAL_LIGHT(new byte[]{0x3}),
    SOUND_LIGHT(new byte[]{0x4}),
    SOUND_USER(new byte[]{0x5});
//		WATCH_MODE(new byte[]{0x6}),
//		SCORE_MODE(new byte[]{0x7});

    private byte[] cmd_data;

    LIGHT_MODE(byte[] data) {
        this.cmd_data = data;
    }

    public byte[] getCmd_data() {
        return cmd_data;
    }
}
