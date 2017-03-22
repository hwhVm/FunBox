package com.example.administrator.baseapp.ui.fragment.webview;

import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.base.BaseFragment;
import com.example.administrator.baseapp.bind.ContentView;
import com.example.administrator.baseapp.bind.ViewInject;

/**
 * Create by beini 2017/3/22
 */
@ContentView(R.layout.fragment_web_viewk)
public class WebViewkFragment extends BaseFragment {
    private String url = "http://music.163.com/#/topic?id=194001&type=android";
    @ViewInject(R.id.web_view)
    WebView web_view;

    @Override
    public void initView() {
        web_view.getSettings().setJavaScriptEnabled(true);////设置WebView属性，能够执行Javascript脚本
        web_view.setWebViewClient(new WebViewClient() {//避免打开系统浏览器

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
    }
//    web_view.goBack(); //goBack()表示返回WebView的上一页
}
