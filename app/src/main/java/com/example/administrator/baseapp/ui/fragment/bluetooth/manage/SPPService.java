package com.example.administrator.baseapp.ui.fragment.bluetooth.manage;

/**
 * Created by beini on 2017/3/16.
 */

public class SPPService {
    private static SPPService instance;

    public static SPPService getInstance() {
        if (instance == null) {
            instance = new SPPService();
        }
        return instance;
    }
}
