package com.beini.adapter;

import android.app.Fragment;
import android.app.FragmentManager;

import java.util.List;

/**
 * Created by beini on 2017/7/18.
 */

public class MViewPagerAdapter extends android.support.v13.app.FragmentPagerAdapter {
    private List<Fragment> views;

    public MViewPagerAdapter(FragmentManager fm, List<Fragment> views) {
        super(fm);
        this.views = views;
    }

    @Override
    public Fragment getItem(int position) {
        return views.get(position);
    }

    @Override
    public int getCount() {
        return views.size();
    }

}
