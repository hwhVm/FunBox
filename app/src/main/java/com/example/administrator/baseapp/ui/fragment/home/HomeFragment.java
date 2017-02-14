package com.example.administrator.baseapp.ui.fragment.home;


import android.view.View;

import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.base.BaseFragment;
import com.example.administrator.baseapp.bind.ContentView;
import com.example.administrator.baseapp.bind.Event;
import com.example.administrator.baseapp.ui.fragment.music.MusicFragment;
import com.example.administrator.baseapp.utils.FragmentHelper;

/**
 * Created by beini on 2017/2/9.
 */
@ContentView(R.layout.fragment_home)
public class HomeFragment extends BaseFragment {

    @Override
    public void initView() {
        baseActivity.setBottom(View.VISIBLE);
        FragmentHelper.logPrint();
    }


    @Event({R.id.btn_next_page})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_next_page:
                baseActivity.replaceFragment(MusicFragment.class);
                break;
        }
    }

}
