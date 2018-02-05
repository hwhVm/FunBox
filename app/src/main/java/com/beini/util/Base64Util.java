package com.beini.util;


import com.beini.util.base64.BASE64Decoder;
import com.beini.util.base64.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


/**
 * Created by beini on 2017/5/24.
 */
public class Base64Util {
    /**
     * 解密
     *
     * @param string
     * @return
     */
    public static String decode(final String string) {
        BASE64Decoder decoder = new BASE64Decoder();
        String decode = null;
        try {
            decode = new String(decoder.decodeBuffer(string));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return decode;
    }

    /**
     * 加密
     *
     * @param string
     * @return
     */
    public static String encode(final String string) {
        BASE64Encoder encoder = new BASE64Encoder();
        String result = null;
        try {
            result = encoder.encode(string.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
