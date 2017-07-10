package com.beini.ui.fragment.webview.tx5;


import android.view.View;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.bind.ViewInject;
import com.beini.ui.fragment.webview.tx5.utils.X5WebView;

/**
 * Create by  beini  2017/6/3
 */
@ContentView(R.layout.fragment_tx5)
public class Tx5Fragment extends BaseFragment {
    @ViewInject(R.id.forum_context)
    X5WebView forum_context;

    @Override
    public void initView() {
        forum_context.loadUrl("http://www.baidu.com");
    }

    @Event(R.id.btn_webview_video)
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_webview_video:
                baseActivity.replaceFragment(FullScreenFragment.class);
                break;
        }
    }
}
