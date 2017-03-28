package com.example.administrator.baseapp.ui.fragment.home;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebViewFragment;

import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.adapter.HomeListAdapter;
import com.example.administrator.baseapp.base.BaseFragment;
import com.example.administrator.baseapp.bean.BaseBean;
import com.example.administrator.baseapp.bind.ContentView;
import com.example.administrator.baseapp.bind.ViewInject;
import com.example.administrator.baseapp.ui.fragment.annotations.AnnotationsFragment;
import com.example.administrator.baseapp.ui.fragment.screenrecord.ScreenRecordFragment;
import com.example.administrator.baseapp.ui.fragment.video.VideoFragment;
import com.example.administrator.baseapp.ui.fragment.facerecognition.FacereCongnitioonFragment;
import com.example.administrator.baseapp.ui.fragment.webview.WebViewTestFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beini on 2017/2/9.
 */
@ContentView(R.layout.fragment_rb2)
public class Rb2Fragment extends BaseFragment {
    @ViewInject(R.id.recycle_rb2)
    RecyclerView recycle_rb2;
    private List<String> functionList = new ArrayList<>();

    @Override
    public void initView() {
        baseActivity.setBottom(View.VISIBLE);
        baseActivity.setTopBar(View.GONE);
        functionList.add(" face recognition");
        functionList.add("video ");
        functionList.add("WebView");
        functionList.add("annotations");
        functionList.add("screen record");
        recycle_rb2.setLayoutManager(new LinearLayoutManager(baseActivity));
        HomeListAdapter homeListAdapter = new HomeListAdapter(new BaseBean<>(R.layout.item_home, functionList));
        recycle_rb2.setAdapter(homeListAdapter);
        homeListAdapter.setItemClick(onItemClickListener);
    }

    public HomeListAdapter.OnItemClickListener onItemClickListener = new HomeListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            switch (position) {
                case 0:
                    baseActivity.replaceFragment(FacereCongnitioonFragment.class);
                    break;
                case 1:
                    baseActivity.replaceFragment(VideoFragment.class);
                    break;
                case 2:
                    baseActivity.replaceFragment(WebViewTestFragment.class);
                    break;
                case 3:
                    baseActivity.replaceFragment(AnnotationsFragment.class);
                    break;
                case 4:
                    baseActivity.replaceFragment(ScreenRecordFragment.class);
                    break;
            }
        }
    };

}
