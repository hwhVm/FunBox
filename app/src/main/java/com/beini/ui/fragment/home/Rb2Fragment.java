package com.beini.ui.fragment.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beini.R;
import com.beini.adapter.HomeListAdapter;
import com.beini.app.AppRouter;
import com.beini.app.BaseFragment;
import com.beini.bean.BaseBean;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;
import com.beini.ui.view.decoration.RecycleDecoration;

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
        functionList.add(getString(R.string.rb2_item_title_swiperefresh));
        functionList.add(getString(R.string.rb2_item_title_x_recyclerview));


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
                    AppRouter.rb2FacereCongnitioonRouter();
                    break;
                case 1:
                    AppRouter.rb2WebViewRouter();
                    break;
                case 2:
                    AppRouter.rb2AnnotationsRouter();
                    break;
                case 3:
                    AppRouter.rb2ScreenRecordRouter();
                    break;
                case 4:
                    AppRouter.rb2NetFileRouter();
                    break;
                case 5:
                    AppRouter.rb2GreenDaoRouter();
                    break;
                case 6:
                    AppRouter.rb2WfiListRouter();
                    break;
                case 7:
                    AppRouter.rb2FingerprintRouter();
                    break;
                case 8:
                    AppRouter.rb2WebScoketRouter();
                    break;
                case 9:
                    AppRouter.rb2OkhttpWebSocketRouter();
                    break;
                case 10:
                    AppRouter.rb2SocketRouter();
                    break;
                case 11:
                    AppRouter.rb2PicPickeRouter();
                    break;
                case 12:
                    Bundle args = new Bundle();
                    args.putString("name", "beini");
                    AppRouter.rb2ArgsRouter(args);
                    break;
                case 13:
                    AppRouter.rb2ViewFlippeRouter();
                    break;
                case 14:
                    AppRouter.rb2Tx5Router();
                    break;
                case 15:
                    AppRouter.rb2RxRouter();
                    break;
                case 16:
                    AppRouter.rb2SwiperefreshRouter();
                    break;
                case 17:
                    AppRouter.rb2xRecyclerviewRouter();
                    break;
            }
        }
    };

}
