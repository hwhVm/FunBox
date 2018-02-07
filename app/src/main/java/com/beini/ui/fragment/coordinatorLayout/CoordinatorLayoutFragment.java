package com.beini.ui.fragment.coordinatorLayout;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.beini.R;
import com.beini.adapter.HomeListAdapter;
import com.beini.app.BaseFragment;
import com.beini.bean.BaseBean;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by beini 2018/2/7
 */
@ContentView(R.layout.fragment_coordinator_layout)
public class CoordinatorLayoutFragment extends BaseFragment {
    @ViewInject(R.id.recycler)
    RecyclerView recycler;
    private List<String> functionList;

    @Override
    public void initView() {
        functionList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            functionList.add(i + "");
        }
        HomeListAdapter homeListAdapter = new HomeListAdapter(new BaseBean<>(R.layout.item_home, functionList));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseActivity);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(homeListAdapter);
    }
}
