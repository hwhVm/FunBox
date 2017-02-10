package com.example.administrator.baseapp.base;


import android.app.Fragment;

/**
 * Created by beini on 2017/2/9.
 */

public interface BaseImpl {
    void initView();

    void replaceFragment(Class<?> fragment);

    void back();

    void remove(Fragment fragment);
}
