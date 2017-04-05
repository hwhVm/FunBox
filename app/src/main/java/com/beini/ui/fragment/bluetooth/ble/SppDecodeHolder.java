package com.beini.ui.fragment.bluetooth.ble;

import com.beini.utils.BLog;

/**
 * Created by beini on 2017/3/17.
 */

public class SppDecodeHolder {
    public enum CMD_TYPE {
        SPP_COMMAND_CHECK(0x04), // 命令回应
        SPP_SET_BOX_MODE(0x45);

        private int _value;

        private CMD_TYPE(int value) {
            _value = value;
        }

        public int value() {
            return _value;
        }
    }

    public static synchronized byte[] encodeCmd(CMD_TYPE cmd_type, byte[] cmd_data) {

        //DLog.i(TAG, "初级数据 : " + cmd_type + " data: " + StringUtils.getHex(cmd_data));

        int cmd1 = cmd_type.value();
        boolean cmdData_f = true;
        byte[] cmd;
        if (cmd_data == null) {
            cmdData_f = false;
        }

        // 七个基本数据
        if (cmdData_f) {
            cmd = new byte[7 + cmd_data.length];
        } else {
            cmd = new byte[7];
        }
        cmd[0] = SppProc.PACK_PREFIX; // 前缀
        cmd[1] = (byte) ((cmd.length - 4) & 0xFF); // 数据位低位
        cmd[2] = (byte) ((cmd.length - 4) >>> 8 & 0xFF); // 数据位高位
        cmd[3] = (byte) (cmd1 & 0xFF); // 数据类型

        // 将命令数据放入包中, 从第 4 位开始放
        if (cmdData_f) {
            for (int i = 0; i < cmd_data.length; i++) {
                cmd[i + 4] = cmd_data[i];
            }
        }

        // 校验和 : 长度 (序号1.2) + 命令类型(序号3) + 命令数据(序号 4 ~ 长度 - 2) 累加结果
        // 注意是字节累加结果
        int CheckSum = 0;
        for (int i = 1; i < cmd.length - 2; i++) {
            CheckSum += (cmd[i] & 0xFF);
            CheckSum &= 0xFFFF;
        }
        cmd[cmd.length - 3] = (byte) (CheckSum & 0xFF); // 校验和低位
        cmd[cmd.length - 2] = (byte) ((CheckSum >>> 8) & 0xFF); // 校验和高位

        cmd[cmd.length - 1] = SppProc.PACK_SUFFIX; // 包尾

        //打印发送的数据
        //LogUtil.e("初始数据 : " + StringUtils.getHex(cmd));

        byte[] sendcmd = new byte[cmd.length * 2];
        int count = 1;
        sendcmd[0] = SppProc.PACK_PREFIX;
        // 转义 :
        // 命令类型及命令数据中出现0X01，则需经过转义变为两个字节：0X03和0x04；如果出现0X02，则须转义为0X03和0X05；如果数据中出现0X03，将需转义为0X03
        // 和0X06
        for (int i = 1; i < cmd.length - 1; i++) {
            if (cmd[i] == SppProc.PACK_PREFIX) {
                sendcmd[count++] = SppProc.PACK_REPLACE_PREFFIX;
                sendcmd[count++] = SppProc.PACK_REPLACE_A;
            } else if (cmd[i] == SppProc.PACK_SUFFIX) {
                sendcmd[count++] = SppProc.PACK_REPLACE_PREFFIX;
                sendcmd[count++] = SppProc.PACK_REPLACE_B;
            } else if (cmd[i] == SppProc.PACK_REPLACE_PREFFIX) {
                sendcmd[count++] = SppProc.PACK_REPLACE_PREFFIX;
                sendcmd[count++] = SppProc.PACK_REPLACE_C;
            } else {
                sendcmd[count++] = cmd[i];
            }
        }
        sendcmd[count++] = SppProc.PACK_SUFFIX;

		/*
         * DLog.d(TAG, "发送编码包:" + new String(Utils.hexTodecWithLength(sendcmd,
		 * count)));
		 */

        byte[] sendbuf = new byte[count];
        for (int index = 0; index < count; index++) {
            sendbuf[index] = sendcmd[index];
        }

        //DLog.i(TAG, "发送最终数据 : " + StringUtils.getHex(sendbuf));

        return sendbuf;
    }

    /**
     * 数据解码
     */
    public static synchronized void decodeData(byte[] bytes, int length) {

        //BLog.d("收到的最初原始数据 : " + StringUtils.getHex(bytes));
        BLog.d("   数据解码   decodeData");
        if (bytes == null) {
            return;
        }

        if (length == 0) {
            return;
        }

        byte[] readrx = (byte[]) bytes;

        // 讲读取到的数据 复制到 readBufk 中
        byte[] readBufk = new byte[length];
        for (int i = 0; i < length; i++) {
            readBufk[i] = readrx[i];
        }
        // 打印收到数据 讲字节数组转为 String 字符串
        //	LogUtil.e( "收到的数据 : " + StringUtils.getHex(readBufk));

        if (readBufk == null || readBufk.length < 5) {
            return;
        }

        int count = 0;
        int dataStr = -1;
        byte[] packet = null;
        for (count = 0; count < readBufk.length; count++) {
            if (readBufk[count] == SppProc.PACK_PREFIX) { // 查找包头
                dataStr = count;
            } else if (readBufk[count] == SppProc.PACK_SUFFIX) { // 查找包尾
                if ((dataStr >= 0) && (count > 0) && (count > dataStr)) {
                    // 创建新数组, 获取整包数据
                    packet = new byte[count - dataStr + 1];
                    for (int i = 0; i < packet.length; i++) {
                        packet[i] = readBufk[dataStr++];
                    }
                }
            }
        }

        if (packet == null) {
            return;
        }

        readBufk = ReceivePro(packet); // 处理转义符

        if (readBufk == null || readBufk.length < 6) {
            return;
        }

        // 打印解码后的数据
//       BLog.d("接收的原始数据 : " + StringUtils.getHex(readBufk));

        if (readBufk[0] != SppProc.PACK_PREFIX
                || readBufk[readBufk.length - 1] != SppProc.PACK_SUFFIX) {
            return;
        }

        int packetlength = getIntInByteArray(readBufk, 1);
        if (packetlength == -1) {
            return;
        }

        // 获取命令类型
        int Cmd = getByteInByteArray(readBufk, 3);

        BLog.d("cmd " + Cmd);
        // 命令回应确认包 0x04
        if (Cmd == CMD_TYPE.SPP_COMMAND_CHECK.value()) {
            boolean isSuccess = (getByteInByteArray(readBufk, 5) == SppProc.PACK_ACCEPT);
            int cmd_type = getByteInByteArray(readBufk, 4);

            {
                byte[] cmd_type_array = new byte[1];
                cmd_type_array[0] = (byte) cmd_type;


            }
            BLog.d("  isSuccess   ");
            if (isSuccess) {
                switch (cmd_type) {
//                    case 0x31:
//                        int lightLevel = getByteInByteArray(readBufk, 6);
//                        break;
                }
            }
        }
    }

    /**
     * 获取 point 及 point + 1 两个字节的数据, point 低位, point + 1 高位
     *
     * @param Data
     * @param point
     *
     * @return
     */
    private static int getIntInByteArray(byte[] Data, int point) {
        // 注意多字节数据小端对齐, 低位在前(左), 高位在后(右)
        int numl = getByteInByteArray(Data, point); // 低位
        int numh = getByteInByteArray(Data, point + 1);// 高位
        if (numl == -1 || numh == -1) {
            return -1;
        }
        return (numh * 256 + numl);
    }


    /**
     * 获取字节数组
     *
     * @param Data
     * @param point
     *
     * @return
     */
    private static int getByteInByteArray(byte[] Data, int point) {
        if (Data == null) {
            return -1;
        }
        if (Data.length <= point) {
            return -1;
        }
        return (Data[point] & 0xFF);
    }

    /**
     * 处理转义字符
     *
     * @param buffer
     *
     * @return
     */
    private static byte[] ReceivePro(byte[] buffer) {

        byte[] resould = null;

        // 包头, 包尾, 包长度不符合, 直接返回
        if (buffer[0] != SppProc.PACK_PREFIX
                || buffer[buffer.length - 1] != SppProc.PACK_SUFFIX
                || buffer.length < 3) {
            return resould;
        }
        resould = new byte[buffer.length];
        int count = 0;

        // 遍历整个数组, 处理转义问题
        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i] == SppProc.PACK_REPLACE_PREFFIX) {
                // 如果遇到 替换位
                // i 下标自增, 看下一位是什么
                i++;
                if (i < buffer.length) {
                    if (buffer[i] == SppProc.PACK_REPLACE_A) {
                        resould[count++] = SppProc.PACK_PREFIX;
                    } else if (buffer[i] == SppProc.PACK_REPLACE_B) {
                        resould[count++] = SppProc.PACK_SUFFIX;
                    } else if (buffer[i] == SppProc.PACK_REPLACE_C) {
                        resould[count++] = SppProc.PACK_REPLACE_PREFFIX;
                    }
                } else {
                    break;
                }
            } else {
                // 如果没有遇到替换位
                resould[count++] = buffer[i];
            }
        }
        // 经过上面的遍历, 数组的大小为 count, 将数组放到一个大小为 count 数组中
        byte[] resould1 = new byte[count];
        for (int i = 0; i < resould1.length; i++) {
            resould1[i] = resould[i];
        }
        return resould1;
    }
}
