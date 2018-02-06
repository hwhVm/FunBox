package com.beini.ui.fragment.webview;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Handler;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.bind.ViewInject;
import com.beini.util.BLog;

import java.io.IOException;
import java.io.InputStream;

/**
 * Create by beini 2017/3/22
 * http://blog.csdn.net/carson_ho/article/details/52693322
 * http://www.cnblogs.com/whoislcj/p/5645025.html
 * webview 的坑：https://www.zhihu.com/question/31316646
 */
@ContentView(R.layout.fragment_web_viewk)
public class WebViewTestFragment extends BaseFragment {
    //    private String url = "http://music.163.com/#/topic?id=194001&type=android";//网易云
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
     * WebView本身也是会记录html缓存的，webview本身就提供了清理缓存的方法，其中参数true是指是否包括磁盘文件也一并清除：
     * webview.clearCache(true);
     * webview.clearHistory();
     */
    @JavascriptInterface
    @SuppressLint({"SetJavaScriptEnabled"})
    @Override
    public void initView() {
        WebSettings webSettings = web_view.getSettings();//浏览器的web设置信息
        webSettings.setJavaScriptEnabled(true);//设置WebView属性，能够执行Javascript脚本
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
//      webSettings.setUseWideViewPort(true);// 当容器超过页面大小时，是否放大页面大小到容器宽度
//      webSettings.setLoadWithOverviewMode(true);//当页面超过容器大小时，是否缩小页面尺寸到页面宽度

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
        webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能 设置是否启用app缓存
        webSettings.setDatabaseEnabled(true);   //开启 database storage API 功能
        webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能
//        webSettings.setAllowFileAccess(true);//设置是否允许访问文件，例如WebView访问sd卡的文件。不过assets与res文件不受此限制，仍然可以通过“file:///android_asset”和“file:///android_res”访问。
//        webSettings.setDatabaseEnabled(true); // 设置是否启用数据库
//        String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
//        webSettings.setAppCachePath(cacheDirPath); //设置  Application Caches 缓存目录


        web_view.setWebChromeClient(new MWebChromCLient());//设置浏览器的交互事件
        web_view.setWebViewClient(new MWebViewClient());//设置浏览器的加载事件
        web_view.addJavascriptInterface(this, "beini");//添加本地的js代码接口
//      web_view.setDownloadListener(downloadListener);//设置文件下载监听
        web_view.loadUrl(url);
//      web_view.loadData();// 加载文本数据。第二个参数表示媒体类型，如"text/html"；第三个参数表示数据的编码格式，"base64"表示采用base64编码，其余值（包括null）表示url编码。
//        web_view.canGoBack();// 表示返回WebView的上一页
//        web_view.goBack();//返回到上一个页面
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
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载。一般在此弹出进度对话框ProgressFialog
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {//页面加载结束。一般在此关闭进度对话框
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {//收到ssl错误
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {//收到错误信息
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

        /**
         * shouldInterceptRequest这个回调可以通知主程序WebView处理的资源（css,js,image等）请求，并允许主程序进行处理后返回数据。如果主程序返回的数据为null，WebView会自行请求网络加载资源，否则使用主程序提供的数据。注意这个回调发生在非UI线程中,所以进行UI系统相关的操作是不可以的。
         *
         * @param view
         * @param request
         * @return
         */
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            WebResourceResponse response = null;
            if (url.contains("logo")) {
                try {
                    InputStream localCopy = baseActivity.getAssets().open("droidyue.png");
                    response = new WebResourceResponse("image/png", "UTF-8", localCopy);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return response;
        }
    }

    class MWebChromCLient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {// 收到页面标题
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {//页面加载进度发生变化
            super.onProgressChanged(view, newProgress);
            BLog.d("                onProgressChanged  newProgress=" + newProgress);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {//弹出js警告框
            BLog.d("                onJsAlert  ");
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {// 弹出js提示框
            BLog.d("                onJsPrompt  ");
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {//弹出js确认框
            BLog.d("                onJsConfirm  ");
            return super.onJsConfirm(view, url, message, result);
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {// 网页请求定位权限。通常重写该方法弹出一个确认对话框，提示用户是否允许网页获得定位权限。下面代码表示允许定位权限：
            super.onGeolocationPermissionsShowPrompt(origin, callback);
//            callback.invoke(origin, true, false);//表示运行定位
        }
    }


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

    /**
     * 文件下载监听
     */
    DownloadListener downloadListener = new DownloadListener() {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            //文件开始下载，可在此设置文件下载的方式，以及文件的保存路径

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        baseActivity.setKeyBackListener(null);
    }
}
