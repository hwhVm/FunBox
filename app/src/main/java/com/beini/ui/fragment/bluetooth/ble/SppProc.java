package com.beini.ui.fragment.bluetooth.ble;

/**
 * Created by beini on 2017/3/17.
 */

public class SppProc {
    public static final int PACK_PREFIX = 0x01;
    public static final int PACK_SUFFIX = 0x02;
    public static final int PACK_REPLACE_PREFFIX = 0x03;
    public static final int PACK_REPLACE_A = 0x04;
    public static final int PACK_REPLACE_B = 0x05;
    public static final int PACK_REPLACE_C = 0x06;


    public static final int PACK_ACCEPT = 0x55;

    public enum LIGHT_MODE {
        TEMPRETURE(new byte[]{0x1});
        private byte[] cmd_data;

        private LIGHT_MODE(byte[] data) {
            this.cmd_data = data;
        }

        public byte[] getCmd_data() {
            return cmd_data;
        }
    }
}
