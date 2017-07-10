package com.beini.app;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by beini on 2017/2/9.
 */

public interface BaseImpl {
    void initView();

    void replaceFragment(Class<?> fragment);
    void replaceFragment(Class<?> fragment, Bundle args);

    void back();

    void remove(Fragment fragment);

    void setBottom(int isHide);

    void setTopBar(int isHide);

    void setTopBarTitle(String top_bar_title);
}
