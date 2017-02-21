package com.example.administrator.baseapp.ui.fragment.home;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.adapter.HomeListAdapter;
import com.example.administrator.baseapp.base.BaseFragment;
import com.example.administrator.baseapp.bean.BaseBean;
import com.example.administrator.baseapp.bind.ContentView;
import com.example.administrator.baseapp.bind.ViewInject;
import com.example.administrator.baseapp.ui.fragment.music.MusicFragment;
import com.example.administrator.baseapp.ui.fragment.recording.VoiceFragment;
import com.example.administrator.baseapp.ui.shake.ShakeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beini on 2017/2/9.
 */
@ContentView(R.layout.fragment_home)
public class HomeFragment extends BaseFragment {
    @ViewInject(R.id.recycler_view_home)
    RecyclerView recycler_view_home;
    private List<String> functionList = new ArrayList<>();

    @Override
    public void initView() {
        functionList.add("dlna demo");
        functionList.add("recording");
        functionList.add("Shake");
        baseActivity.setBottom(View.VISIBLE);
        baseActivity.setTopBar(View.GONE);
        recycler_view_home.setLayoutManager(new LinearLayoutManager(baseActivity));
        HomeListAdapter homeListAdapter = new HomeListAdapter(new BaseBean(R.layout.item_home, functionList));
        recycler_view_home.setAdapter(homeListAdapter);
        homeListAdapter.setItemClick(onItemClickListener);
    }

    @Override
    protected void onInvisible() {

    }

    @Override
    protected void onVisible() {

    }

    @Override
    protected void lazyLoad() {

    }

    public HomeListAdapter.OnItemClickListener onItemClickListener = new HomeListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            switch (position) {
                case 0:
                    baseActivity.replaceFragment(MusicFragment.class);
                    break;
                case 1:
                    baseActivity.replaceFragment(VoiceFragment.class);
                    break;
                case 2:
                    baseActivity.replaceFragment(ShakeFragment.class);
                    break;
            }
        }
    };


}
