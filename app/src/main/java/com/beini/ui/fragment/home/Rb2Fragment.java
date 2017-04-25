package com.beini.ui.fragment.home;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beini.R;
import com.beini.adapter.HomeListAdapter;
import com.beini.base.BaseFragment;
import com.beini.bean.BaseBean;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;
import com.beini.ui.fragment.annotations.AnnotationsFragment;
import com.beini.ui.fragment.facerecognition.FacereCongnitioonFragment;
import com.beini.ui.fragment.fingerprint.FingerprintFragment;
import com.beini.ui.fragment.green.GreenDaoFragment;
import com.beini.ui.fragment.net.NetFileFragment;
import com.beini.ui.fragment.screenrecord.ScreenRecordFragment;
import com.beini.ui.fragment.video.VideoFragment;
import com.beini.ui.fragment.websocket.WebScoketFragment;
import com.beini.ui.fragment.webview.WebViewTestFragment;
import com.beini.ui.fragment.wifi.WfiListFragment;

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
        functionList.add("retrofit 2.0 ");
        functionList.add("GreenDaoFragment");
        functionList.add("WfiListFragment");
        functionList.add("FingerprintFragment");
        functionList.add("webSocket");
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
                case 5:
                    baseActivity.replaceFragment(NetFileFragment.class);
                    break;
                case 6:
                    baseActivity.replaceFragment(GreenDaoFragment.class);
                    break;
                case 7:
                    baseActivity.replaceFragment(WfiListFragment.class);
                    break;
                case 8:
                    baseActivity.replaceFragment(FingerprintFragment.class);
                    break;
                case 9:
                    baseActivity.replaceFragment(WebScoketFragment.class);
                    break;
            }
        }
    };

}
