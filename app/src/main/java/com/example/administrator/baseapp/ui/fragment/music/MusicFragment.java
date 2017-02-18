package com.example.administrator.baseapp.ui.fragment.music;


import android.view.View;

import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.base.BaseFragment;
import com.example.administrator.baseapp.bind.ContentView;
import com.example.administrator.baseapp.utils.BLog;
import com.example.administrator.baseapp.utils.FragmentHelper;

/**
 * Created by beini on 2017/2/14.
 */
@ContentView(R.layout.fragment_music)
public class MusicFragment extends BaseFragment {


    @Override
    public void initView() {
        baseActivity.setBottom(View.GONE);
    }

    @Override
    protected void onInvisible() {
        BLog.d(" MusicFragment onInvisible");
    }

    @Override
    protected void onVisible() {
        BLog.d(" MusicFragment onVisible");
    }

    @Override
    protected void lazyLoad() {

    }

}
