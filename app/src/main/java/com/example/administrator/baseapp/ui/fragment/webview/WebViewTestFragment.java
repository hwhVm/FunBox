package com.example.administrator.baseapp.ui.fragment.webview;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.base.BaseFragment;
import com.example.administrator.baseapp.bind.ContentView;
import com.example.administrator.baseapp.bind.Event;
import com.example.administrator.baseapp.bind.ViewInject;
import com.example.administrator.baseapp.utils.BLog;

/**
 * Create by beini 2017/3/22
 * http://blog.csdn.net/carson_ho/article/details/52693322
 * http://www.cnblogs.com/whoislcj/p/5645025.html
 * webview 的坑：https://www.zhihu.com/question/31316646
 */
@ContentView(R.layout.fragment_web_viewk)
public class WebViewTestFragment extends BaseFragment {
    //    private String url = "http://music.163.com/#/topic?id=194001&type=android";
    private String url = "file:///android_asset/demo.html";
    @ViewInject(R.id.web_view)
    WebView web_view;

    /**
     * 方式1. 加载一个网页：
     * webView.loadUrl("http://www.google.com/");
     * 方式2：加载apk包中的html页面
     * webView.loadUrl("file:///android_asset/test.html");
     * 方式3：加载手机本地的html页面
     * webView.loadUrl("content://com.android.htmlfileprovider/sdcard/test.html")
     * <p>
     * <p>
     * 添加请求头信息
     * Map<String,String> map=new HashMap<String,String>();
     * map.put("User-Agent","Android");
     * webView.loadUrl("www.xxx.com/index.html",map);
     * <p>
     *  WebView本身也是会记录html缓存的，webview本身就提供了清理缓存的方法，其中参数true是指是否包括磁盘文件也一并清除：
     * webview.clearCache(true);
     * webview.clearHistory();
     */
    @JavascriptInterface
    @SuppressLint({"SetJavaScriptEnabled"})
    @Override
    public void initView() {
        WebSettings webSettings = web_view.getSettings();
        webSettings.setJavaScriptEnabled(true);//设置WebView属性，能够执行Javascript脚本
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        //设置WebView缓存
        //优先使用缓存:
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //缓存模式如下：
        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
//        if (NetStatusUtil.isConnected(getApplicationContext())) {
//            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
//        } else {
//            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没网，则从本地获取，即离线加载
//        }
        //每个 Application 只调用一次 WebSettings.setAppCachePath()，WebSettings.setAppCacheMaxSize()
        webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
        webSettings.setDatabaseEnabled(true);   //开启 database storage API 功能
        webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能

//        String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
//        webSettings.setAppCachePath(cacheDirPath); //设置  Application Caches 缓存目录

        web_view.setWebChromeClient(new MWebChromCLient());
        web_view.setWebViewClient(new MWebViewClient());
        web_view.addJavascriptInterface(this, "beini");
        web_view.loadUrl(url);
    }

    private Handler mHandler = new Handler();

    @Event(R.id.btn_use_js)
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_use_js:
                BLog.d("      JavascriptInterface   ");
                web_view.loadUrl("javascript:alertMethod()");
                break;
        }
    }

    class MWebViewClient extends WebViewClient {//避免打开系统浏览器

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            BLog.d("         onReceivedError    ");
            super.onReceivedError(view, request, error);
        }

        /**
         * 当按下某个连接时WebViewClient会调用这个方法，并传递参数
         *
         * @param view
         * @param request
         * @return
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            BLog.d("        shouldOverrideUrlLoading ");
            view.loadUrl(url);
            return true;
        }

    }

    class MWebChromCLient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            BLog.d("                onProgressChanged  newProgress="+newProgress);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            BLog.d("                onJsAlert  ");
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            BLog.d("                onJsPrompt  ");
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            BLog.d("                onJsConfirm  ");
            return super.onJsConfirm(view, url, message, result);
        }
    }


//    web_view.goBack(); //goBack()表示返回WebView的上一页

    @android.webkit.JavascriptInterface
    public void actionFromJs() {
        baseActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(baseActivity, "js调用了Native函数", Toast.LENGTH_SHORT).show();
//                String text = logTextView.getText() + "\njs调用了Native函数";
//                logTextView.setText(text);
            }
        });
    }

    @android.webkit.JavascriptInterface
    public void actionFromJsWithParam(final String str) {
        baseActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(baseActivity, "js调用了Native函数传递参数：" + str, Toast.LENGTH_SHORT).show();
//                String text = logTextView.getText() +  "\njs调用了Native函数传递参数：" + str;
//                logTextView.setText(text);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        baseActivity.setKeyBackListener(null);
    }
}
