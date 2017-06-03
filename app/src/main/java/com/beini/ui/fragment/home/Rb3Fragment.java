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
import com.beini.ui.fragment.facerecognition.FacereCongnitioonFragment;

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
        functionList.add(" 1");
        functionList.add(" 2");
        functionList.add(" 3");
        functionList.add(" 4");
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
                    baseActivity.replaceFragment(FacereCongnitioonFragment.class);
                    break;
            }
        }
    };

}
