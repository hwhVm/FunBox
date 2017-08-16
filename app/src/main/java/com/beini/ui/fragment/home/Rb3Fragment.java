package com.beini.ui.fragment.home;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.beini.R;
import com.beini.adapter.MViewPagerAdapter;
import com.beini.app.AppRouter;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;
import com.beini.ui.fragment.gesture.GestureDetectorFragment;
import com.beini.ui.fragment.rgb.ColorPickerFragment;
import com.beini.ui.fragment.rgb.ColorPickerVFragment;
import com.beini.ui.fragment.rgb.RGBFragment;
import com.beini.ui.fragment.webtest.ShiroFragment;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beini on 2017/2/9.
 */
@ContentView(R.layout.fragment_rb3)
public class Rb3Fragment extends BaseFragment {

    @ViewInject(R.id.view_pager)
    private ViewPager view_pager;
    @ViewInject(R.id.magic_indicator)
    private MagicIndicator magicIndicator;

    private List<String> functionList = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private int currentPosition;

    @Override
    public void initView() {
        baseActivity.setBottom(View.VISIBLE);
        baseActivity.setTopBar(View.GONE);
        functionList.add(getString(R.string.rb3_item_title_shiro));
        functionList.add(getString(R.string.rb3_item_title_rgb));
        functionList.add(getString(R.string.rb3_item_title_color_picker_h));
        functionList.add(getString(R.string.rb3_item_title_color_picker_v));
        functionList.add(getString(R.string.rb3_item_title_popup_video_gesture));
        fragments.add(AppRouter.rb3Fragment(ShiroFragment.class));
        fragments.add(AppRouter.rb3Fragment(RGBFragment.class));
        fragments.add(AppRouter.rb3Fragment(ColorPickerFragment.class));
        fragments.add(AppRouter.rb3Fragment(ColorPickerVFragment.class));
        fragments.add(AppRouter.rb3Fragment(GestureDetectorFragment.class));

        magicIndicator.setBackgroundColor(Color.parseColor("#d43d3d"));

        final CommonNavigator commonNavigator = new CommonNavigator(getActivity());


        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return functionList == null ? 0 : functionList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);

                clipPagerTitleView.setText(functionList.get(index));
                clipPagerTitleView.setTextColor(Color.parseColor("#f2c4c4"));
                clipPagerTitleView.setClipColor(Color.WHITE);

                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view_pager.setCurrentItem(index);
                    }
                });
                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;    // 没有指示器，因为title的指示作用已经很明显了
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        MViewPagerAdapter mViewPagerAdapter = new MViewPagerAdapter(getChildFragmentManager(), fragments);
        view_pager.setAdapter(mViewPagerAdapter);
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ViewPagerHelper.bind(magicIndicator, view_pager);

    }

}
