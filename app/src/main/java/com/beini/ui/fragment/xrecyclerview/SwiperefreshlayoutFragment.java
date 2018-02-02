package com.beini.ui.fragment.xrecyclerview;


import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;
import com.beini.util.BLog;

/**
 * Create by beini 2018/2/2
 */
@ContentView(R.layout.fragment_swiperefreshlayout)
public class SwiperefreshlayoutFragment extends BaseFragment {
    @ViewInject(R.id.recycle_swipe)
    RecyclerView recycle_swipe;
    @ViewInject(R.id.layout_swipe_refresh)
    SwipeRefreshLayout layout_swipe_refresh;

    @Override
    public void initView() {
        baseActivity.setTopBar(View.GONE);
        baseActivity.setBottom(View.GONE);
        ViewGroup.LayoutParams layoutParams = recycle_swipe.getLayoutParams();
        BLog.e(" 锁屏 layoutParams.width=" + layoutParams.width + "   layoutParams.height=" + layoutParams.height);
    }
}
