package com.beini.ui.fragment.home;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beini.R;
import com.beini.adapter.BaseAdapter;
import com.beini.adapter.HomeListAdapter;
import com.beini.base.BaseFragment;
import com.beini.bean.BaseBean;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;
import com.beini.ui.fragment.gesture.GestureDetectorFragment;
import com.beini.ui.fragment.rgb.ColorPickerFragment;
import com.beini.ui.fragment.rgb.ColorPickerVFragment;
import com.beini.ui.fragment.rgb.RGBFragment;
import com.beini.ui.fragment.webtest.ShiroFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beini on 2017/2/9.
 */
@ContentView(R.layout.fragment_rb3)
public class Rb3Fragment extends BaseFragment {
    @ViewInject(R.id.recycle_rb3)
    RecyclerView recycle_rb3;
    private List<String> functionList = new ArrayList<>();

    @Override
    public void initView() {
        baseActivity.setBottom(View.VISIBLE);
        baseActivity.setTopBar(View.GONE);
        functionList.add(" shiro权限框架测试");
        functionList.add(" rgb");
        functionList.add(" 拾色器h");
        functionList.add(" 拾色器v");
        functionList.add(" 手势");
        functionList.add(" 5");
        functionList.add(" 6");
        functionList.add(" 7");
        recycle_rb3.setLayoutManager(new LinearLayoutManager(baseActivity));
        HomeListAdapter homeListAdapter = new HomeListAdapter(new BaseBean<>(R.layout.item_home, functionList));
        recycle_rb3.setAdapter(homeListAdapter);
        homeListAdapter.setItemClick(onItemClickListener);
    }

    public HomeListAdapter.OnItemClickListener onItemClickListener = new BaseAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            switch (position) {
                case 0:
                    baseActivity.replaceFragment(ShiroFragment.class);
                    break;
                case 1:
                    baseActivity.replaceFragment(RGBFragment.class);
                    break;
                case 2:
                    baseActivity.replaceFragment(ColorPickerFragment.class);
                    break;
                case 3:
                    baseActivity.replaceFragment(GestureDetectorFragment.class);
                    break;
                case 4:
                    baseActivity.replaceFragment(ColorPickerVFragment.class);
                    break;
            }
        }
    };

}
