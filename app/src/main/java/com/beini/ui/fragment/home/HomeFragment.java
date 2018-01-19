package com.beini.ui.fragment.home;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.beini.R;
import com.beini.adapter.HomeListAdapter;
import com.beini.app.AppRouter;
import com.beini.app.BaseFragment;
import com.beini.bean.BaseBean;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;
import com.beini.ui.view.RecycleDecoration;

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
        functionList.add(getString(R.string.home_item_title_cpp));
        functionList.add(getString(R.string.home_item_title_shake));
        functionList.add(getString(R.string.home_item_title_sideslip));
        functionList.add(getString(R.string.home_item_title_carmra));
        functionList.add(getString(R.string.home_item_title_spp_ble));
        functionList.add(getString(R.string.home_item_title_sms));
        functionList.add(getString(R.string.home_item_title_notification));
        functionList.add(getString(R.string.home_item_title_broadcast));
        functionList.add(getString(R.string.home_item_title_ani));
        functionList.add(getString(R.string.home_item_title_aidl));
        functionList.add(getString(R.string.home_item_title_service));
        functionList.add(getString(R.string.home_item_title_popup_window));
        functionList.add(getString(R.string.home_item_title_snap_helper));
        functionList.add(getString(R.string.home_item_title_label));
        functionList.add(getString(R.string.home_item_title_canvas));

        baseActivity.setBottom(View.VISIBLE);
        baseActivity.setTopBar(View.GONE);
//      recycler_view_home.setLayoutManager(new LinearLayoutManager(baseActivity));
        recycler_view_home.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        HomeListAdapter homeListAdapter = new HomeListAdapter(new BaseBean<>(R.layout.item_home, functionList));
        recycler_view_home.addItemDecoration(new RecycleDecoration(getActivity()));
        recycler_view_home.setAdapter(homeListAdapter);
        homeListAdapter.setItemClick(onItemClickListener);

    }

    @Override
    public void returnLoad() {
        super.returnLoad();
        baseActivity.setTopBar(View.GONE);
    }

    public HomeListAdapter.OnItemClickListener onItemClickListener = new HomeListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            switch (position) {
                case 0:
                    AppRouter.homeCppRouter();
                    break;
                case 1:
                    AppRouter.homeShakeRouter();
                    break;
                case 2:
                    AppRouter.homeSideslipRouter();
                    break;
                case 3:
                    AppRouter.homeMediaFunctionRouter();
                    break;
                case 4:
                    AppRouter.homeSppRouter();
                    break;
                case 5:
                    AppRouter.homeSmsRouter();
                    break;
                case 6:
                    AppRouter.homeNotificationRouter();
                    break;
                case 7:
                    AppRouter.homeBroadcastRouter();
                    break;
                case 8:
                    AppRouter.homeAniRouter();
                    break;
                case 9:
                    AppRouter.homeAIDLRouter();
                    break;
                case 10:
                    AppRouter.homeServiceRouter();
                    break;
                case 11:
                    AppRouter.homePopupWindowRouter();
                    break;
                case 12:
                    AppRouter.homeSnapHeplerRouter();
                    break;
                case 13:
                    break;
                case 14:
                    AppRouter.homeCanvasRouter();
                    break;
            }
        }
    };


}
