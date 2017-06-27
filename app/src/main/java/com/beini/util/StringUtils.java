package com.beini.util;

import com.beini.base.BaseApplication;

/**
 * Created by beini on 2017/3/15.
 */

public class StringUtils {
    public static String getString(int stringId) {
        return BaseApplication.getInstance().getString(stringId);
    }
}
