package com.beini.util;

import com.beini.app.GlobalApplication;

/**
 * Created by beini on 2017/3/15.
 */

public class StringUtils {
    public static String getString(int stringId) {
        return GlobalApplication.getInstance().getString(stringId);
    }
}
