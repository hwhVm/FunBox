package com.beini.ui.fragment.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beini.R;
import com.beini.adapter.HomeListAdapter;
import com.beini.app.BaseFragment;
import com.beini.bean.BaseBean;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;
import com.beini.ui.fragment.annotations.AnnotationsFragment;
import com.beini.ui.fragment.args.ArgsFragment;
import com.beini.ui.fragment.facerecognition.FacereCongnitioonFragment;
import com.beini.ui.fragment.fingerprint.FingerprintFragment;
import com.beini.ui.fragment.green.GreenDaoFragment;
import com.beini.ui.fragment.net.NetFileFragment;
import com.beini.ui.fragment.picPicker.PicPickeFragment;
import com.beini.ui.fragment.rx.RxRetryFragment;
import com.beini.ui.fragment.screenrecord.ScreenRecordFragment;
import com.beini.ui.fragment.video.VideoFragment;
import com.beini.ui.fragment.viewflippe.ViewFlippeFragment;
import com.beini.ui.fragment.websocketandsocket.OkhttpWebSocketFragment;
import com.beini.ui.fragment.websocketandsocket.SocketFragment;
import com.beini.ui.fragment.websocketandsocket.WebScoketFragment;
import com.beini.ui.fragment.webview.WebViewTestFragment;
import com.beini.ui.fragment.webview.tx5.Tx5Fragment;
import com.beini.ui.fragment.wifi.WfiListFragment;
import com.beini.ui.view.RecycleDecoration;

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
        functionList.add(getString(R.string.rb2_item_title_popup_face_recognition));
        functionList.add(getString(R.string.rb2_item_title_popup_video));
        functionList.add(getString(R.string.rb2_item_title_popup_webview));
        functionList.add(getString(R.string.rb2_item_title_popup_annotations));
        functionList.add(getString(R.string.rb2_item_title_popup_screen));
        functionList.add(getString(R.string.rb2_item_title_popup_retrofit));
        functionList.add(getString(R.string.rb2_item_title_popup_greendao));
        functionList.add(getString(R.string.rb2_item_title_popup_wifi));
        functionList.add(getString(R.string.rb2_item_title_popup_video_fingerprint));
        functionList.add(getString(R.string.rb2_item_title_popup_video_websocket));
        functionList.add(getString(R.string.rb2_item_title_popup_video_okhttp));
        functionList.add(getString(R.string.rb2_item_title_popup_video_socket));
        functionList.add(getString(R.string.rb2_item_title_popup_video_pic));
        functionList.add(getString(R.string.rb2_item_title_popup_video_args));
        functionList.add(getString(R.string.rb2_item_title_popup_video_view_flippe));
        functionList.add(getString(R.string.rb2_item_title_popup_video_tx));
        functionList.add(getString(R.string.rb2_item_title_popup_video_rx));
        recycle_rb2.setLayoutManager(new LinearLayoutManager(baseActivity));
        HomeListAdapter homeListAdapter = new HomeListAdapter(new BaseBean<>(R.layout.item_home, functionList));
        recycle_rb2.addItemDecoration(new RecycleDecoration(getActivity()));
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
                case 10:
                    baseActivity.replaceFragment(OkhttpWebSocketFragment.class);
                    break;
                case 11:
                    baseActivity.replaceFragment(SocketFragment.class);
                    break;
                case 12:
                    baseActivity.replaceFragment(PicPickeFragment.class);
                    break;
                case 13:
                    Bundle args = new Bundle();
                    args.putString("name", "beini");
                    baseActivity.replaceFragment(ArgsFragment.class, args);
                    break;
                case 14:
                    baseActivity.replaceFragment(ViewFlippeFragment.class);
                    break;
                case 15:
                    baseActivity.replaceFragment(Tx5Fragment.class);
                    break;
                case 16:
                    baseActivity.replaceFragment(RxRetryFragment.class);
                    break;
            }
        }
    };

}
