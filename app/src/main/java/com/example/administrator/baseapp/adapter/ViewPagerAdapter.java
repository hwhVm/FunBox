package com.example.administrator.baseapp.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

/**
 * Created by beini on 2017/3/13.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private List<View> views;
    private Context context;

    public ViewPagerAdapter(List<View> views, Context context) {
        this.views = views;
        this.context = context;
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }
    @Override
    public void destroyItem(View container, int position, Object object) {

        ((ViewPager) container).removeView(views.get(position));
    }
    @Override
    public Object instantiateItem(View container, int position) {

        ((ViewPager) container).addView(views.get(position));

        return views.get(position);

    }
}
