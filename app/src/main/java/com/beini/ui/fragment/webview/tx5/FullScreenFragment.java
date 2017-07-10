package com.beini.ui.fragment.webview.tx5;


import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;
import com.beini.ui.fragment.webview.tx5.utils.WebViewJavaScriptFunction;
import com.beini.ui.fragment.webview.tx5.utils.X5WebView;

/**
 * Create by beini 2017/6/3
 * 用于演示X5webview实现视频的全屏播放功能 其中注意 X5的默认全屏方式 与 android 系统的全屏方式
 */
@ContentView(R.layout.fragment_filechooser)
public class FullScreenFragment extends BaseFragment {
    @ViewInject(R.id.web_filechooser)
    X5WebView web_filechooser;

    @Override
    public void initView() {
        web_filechooser.loadUrl("file:///android_asset/fullscreenVideo.html");

        getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);

        web_filechooser.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        web_filechooser.addJavascriptInterface(new WebViewJavaScriptFunction() {

            @Override
            public void onJsFunctionCalled(String tag) {
                // TODO Auto-generated method stub

            }

            @JavascriptInterface
            public void onX5ButtonClicked() {
                enableX5FullscreenFunc();
            }

            @JavascriptInterface
            public void onCustomButtonClicked() {
                disableX5FullscreenFunc();
            }

            @JavascriptInterface
            public void onLiteWndButtonClicked() {
                enableLiteWndFunc();
            }

            @JavascriptInterface
            public void onPageVideoClicked() {
                enablePageVideoFunc();
            }
        }, "Android");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        try {
            super.onConfigurationChanged(newConfig);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // /////////////////////////////////////////
    // 向webview发出信息
    private void enableX5FullscreenFunc() {

        if (web_filechooser.getX5WebViewExtension() != null) {
            Toast.makeText(getActivity(), "开启X5全屏播放模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            web_filechooser.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void disableX5FullscreenFunc() {
        if (web_filechooser.getX5WebViewExtension() != null) {
            Toast.makeText(getActivity(), "恢复webkit初始状态", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", true);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            web_filechooser.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void enableLiteWndFunc() {
        if (web_filechooser.getX5WebViewExtension() != null) {
            Toast.makeText(getActivity(), "开启小窗模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", true);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            web_filechooser.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void enablePageVideoFunc() {
        if (web_filechooser.getX5WebViewExtension() != null) {
            Toast.makeText(getActivity(), "页面内全屏播放模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 1);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            web_filechooser.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }
}
