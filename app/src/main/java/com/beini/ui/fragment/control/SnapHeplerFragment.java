package com.beini.ui.fragment.control;


import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;

import com.beini.R;
import com.beini.adapter.SnapPicAdapter;
import com.beini.app.BaseFragment;
import com.beini.bean.BaseBean;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by beini 2017/7/25
 */
@ContentView(R.layout.fragment_snap_hepler)
public class SnapHeplerFragment extends BaseFragment {
    @ViewInject(R.id.recyler_snap_hepler)
    RecyclerView recyler_snap_hepler;
    List<Drawable> drawables;

    @Override
    public void initView() {
        drawables = new ArrayList<>();
        drawables.add(getResources().getDrawable(R.mipmap.a));
        drawables.add(getResources().getDrawable(R.mipmap.b));
        drawables.add(getResources().getDrawable(R.mipmap.c));
        drawables.add(getResources().getDrawable(R.mipmap.d));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyler_snap_hepler.setLayoutManager(linearLayoutManager);
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyler_snap_hepler);
        SnapPicAdapter homeListAdapter = new SnapPicAdapter(new BaseBean<>(R.layout.item_snap_pic, drawables));
        recyler_snap_hepler.setAdapter(homeListAdapter);
    }

}
