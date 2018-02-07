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
import com.beini.constants.Constants;

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
        functionList.add(getString(R.string.home_item_title_fmod));
        functionList.add(getString(R.string.home_item_title_coord));
        functionList.add(getString(R.string.home_item_title_beat));
        functionList.add(getString(R.string.home_item_title_circle_ani));
        functionList.add(getString(R.string.home_item_title_fade_ani));
        functionList.add(getString(R.string.home_item_title_disklru));
        functionList.add(getString(R.string.home_item_title_cut));
        functionList.add(getString(R.string.home_item_title_other));
        functionList.add(getString(R.string.home_item_title_sensor));
        functionList.add(getString(R.string.home_item_title_viewpager));
        functionList.add(getString(R.string.home_item_title_wifi));

        baseActivity.setBottom(View.VISIBLE);
        baseActivity.setTopBar(View.GONE);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseActivity);
//        recycler_view_home.setLayoutManager(linearLayoutManager);
        recycler_view_home.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        HomeListAdapter homeListAdapter = new HomeListAdapter(new BaseBean<>(R.layout.item_home, functionList));
//        recycler_view_home.addItemDecoration(new HomeDecoration());
        recycler_view_home.setAdapter(homeListAdapter);
        homeListAdapter.setItemClick(onItemClickListener);
        homeListAdapter.notifyDataSetChanged();

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
                case Constants.HOME_CPP:
                    AppRouter.homeCppRouter();
                    break;
                case Constants.HOME_SHAKE:
                    AppRouter.homeShakeRouter();
                    break;
                case Constants.HOME_SIDESLIP:
                    AppRouter.homeSideslipRouter();
                    break;
                case Constants.HOME_MEDIAFUNCTION:
                    AppRouter.homeMediaFunctionRouter();
                    break;
                case Constants.HOME_SPP:
                    AppRouter.homeSppRouter();
                    break;
                case Constants.HOME_SMS:
                    AppRouter.homeSmsRouter();
                    break;
                case Constants.HOME_NOTIFICATION:
                    AppRouter.homeNotificationRouter();
                    break;
                case Constants.HOME_BROADCAST:
                    AppRouter.homeBroadcastRouter();
                    break;
                case Constants.HOME_ANI:
                    AppRouter.homeAniRouter();
                    break;
                case Constants.HOME_AIDL:
                    AppRouter.homeAIDLRouter();
                    break;
                case Constants.HOME_SERVICE:
                    AppRouter.homeServiceRouter();
                    break;
                case Constants.HOME_POPUPWINDOW:
                    AppRouter.homePopupWindowRouter();
                    break;
                case Constants.HOME_SNAP:
                    AppRouter.homeSnapHeplerRouter();
                    break;
                case Constants.HOME_TAG:
                    break;
                case Constants.HOME_CANVAS:
                    AppRouter.homeCanvasRouter();
                    break;
                case Constants.HOME_FMOD:
                    AppRouter.homeFmod();
                    break;
                case Constants.HOME_COORDINATOR:
                    AppRouter.homeCoordinatorLayout();
                    break;
                case Constants.HOME_BEAT:
                    AppRouter.homeBeatRouter();
                    break;
                case Constants.HOME_CIRCLEANI:
                    AppRouter.homeAniCircleRouter();
                    break;
                case Constants.HOME_ANI_FADE:
                    AppRouter.homeAniFadeRouter();
                    break;
                case Constants.HOME_DISKLRU:
                    AppRouter.homeDiskLruRouter();
                    break;
                case Constants.HOME_CUT:
                    AppRouter.homeCutRouter();
                    break;
                case Constants.HOME_OTHER:
                    AppRouter.homeOtherRouter();
                    break;
                case Constants.HOME_SENSOR:
                    AppRouter.homeSensorRouter();
                    break;
                case Constants.HOME_VIEWPAGE:
                    AppRouter.homeViewPager();
                    break;
                case Constants.HOME_NET:
                    AppRouter.homeNetRouter();
                    break;
            }
        }
    };


}
