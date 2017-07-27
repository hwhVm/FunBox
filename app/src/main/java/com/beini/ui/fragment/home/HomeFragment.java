package com.beini.ui.fragment.home;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.beini.R;
import com.beini.adapter.HomeListAdapter;
import com.beini.app.BaseFragment;
import com.beini.bean.BaseBean;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;
import com.beini.ui.fragment.aidl.AIDLFragment;
import com.beini.ui.fragment.ani.AniFragment;
import com.beini.ui.fragment.bluetooth.SppFragment;
import com.beini.ui.fragment.broadcast.BroadcastFragment;
import com.beini.ui.fragment.control.SnapHeplerFragment;
import com.beini.ui.fragment.cpptest.CppFragment;
import com.beini.ui.fragment.multimedia.MediaFunctionListFragment;
import com.beini.ui.fragment.notification.NotificationFragment;
import com.beini.ui.fragment.popupwindow.PopupWindowFragment;
import com.beini.ui.fragment.service.ServiceFragment;
import com.beini.ui.fragment.shake.ShakeFragment;
import com.beini.ui.fragment.sideslip.SideslipFragment;
import com.beini.ui.fragment.sms.SmsFragment;
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

        baseActivity.setBottom(View.VISIBLE);
        baseActivity.setTopBar(View.GONE);
//        recycler_view_home.setLayoutManager(new LinearLayoutManager(baseActivity));
        recycler_view_home.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        HomeListAdapter homeListAdapter = new HomeListAdapter(new BaseBean<>(R.layout.item_home, functionList));
        recycler_view_home.addItemDecoration(new RecycleDecoration(getActivity()));
        recycler_view_home.setAdapter(homeListAdapter);
        homeListAdapter.setItemClick(onItemClickListener);

    }

    public HomeListAdapter.OnItemClickListener onItemClickListener = new HomeListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            switch (position) {
                case 0:
                    baseActivity.replaceFragment(CppFragment.class);
                    break;
                case 1:
                    baseActivity.replaceFragment(ShakeFragment.class);
                    break;
                case 2:
                    baseActivity.replaceFragment(SideslipFragment.class);
                    break;
                case 3:
                    baseActivity.replaceFragment(MediaFunctionListFragment.class);
                    break;
                case 4:
                    baseActivity.replaceFragment(SppFragment.class);
                    break;
                case 5:
                    baseActivity.replaceFragment(SmsFragment.class);
                    break;
                case 6:
                    baseActivity.replaceFragment(NotificationFragment.class);
                    break;
                case 7:
                    baseActivity.replaceFragment(BroadcastFragment.class);
                    break;
                case 8:
                    baseActivity.replaceFragment(AniFragment.class);
                    break;
                case 9:
                    baseActivity.replaceFragment(AIDLFragment.class);
                    break;
                case 10:
                    baseActivity.replaceFragment(ServiceFragment.class);
                    break;
                case 11:
                    baseActivity.replaceFragment(PopupWindowFragment.class);
                    break;
                case 12:
                    baseActivity.replaceFragment(SnapHeplerFragment.class);
                    break;
            }
        }
    };


}
