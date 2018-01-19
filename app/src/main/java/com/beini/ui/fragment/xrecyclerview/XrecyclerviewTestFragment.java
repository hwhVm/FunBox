package com.beini.ui.fragment.xrecyclerview;


import android.support.v7.widget.LinearLayoutManager;

import com.beini.R;
import com.beini.adapter.HomeListAdapter;
import com.beini.app.BaseFragment;
import com.beini.bean.BaseBean;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;
import com.beini.util.BLog;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by beini 2018/1/19
 * https://github.com/XRecyclerView/XRecyclerView
 */
@ContentView(R.layout.fragment_xrecyclerview_test)
public class XrecyclerviewTestFragment extends BaseFragment {
    @ViewInject(R.id.x_recycler_view)
    XRecyclerView mRecyclerView;
    private List<String> datas;

    @Override
    public void initView() {
        datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            datas.add("i=" + i);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        BLog.d("--------->mRecyclerView=" + (mRecyclerView == null));
        mRecyclerView.setLayoutManager(layoutManager);
        HomeListAdapter homeListAdapter = new HomeListAdapter(new BaseBean<>(R.layout.item_home, datas));
        mRecyclerView.setAdapter(homeListAdapter);
//        mRecyclerView
//                .getDefaultRefreshHeaderView() // get default refresh header view
//                .setRefreshTimeVisible(true);  // make refresh time visible,false means hiding

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                BLog.d("--------->onRefresh");
                mRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                BLog.d("--------->onLoadMore");
//                mRecyclerView.loadMoreComplete();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRecyclerView != null) {
            mRecyclerView.destroy();
            mRecyclerView = null;
        }
    }
}
