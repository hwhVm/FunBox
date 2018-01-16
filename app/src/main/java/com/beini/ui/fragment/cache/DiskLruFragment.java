package com.beini.ui.fragment.cache;


import android.view.View;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.db.cache.DiskLruCacheUtils;
import com.beini.ui.fragment.net.model.NetModel;
import com.beini.util.BLog;

import java.io.InputStream;

/**
 * Create by beini 2017/1/16
 */
@ContentView(R.layout.fragment_disk_lru)
public class DiskLruFragment extends BaseFragment {

    @Override
    public void initView() {
        NetModel.downLoadFileAndCache();
    }

    @Event(R.id.btn_get_cache)
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_get_cache:
                InputStream inputStream = DiskLruCacheUtils.getInstance().getCache("aaaaaa");
                BLog.e(" inputStream  " + (inputStream == null));
                break;
        }
    }
}
