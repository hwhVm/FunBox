package com.beini.app;

import android.os.Bundle;

import com.beini.ui.fragment.aidl.AIDLFragment;
import com.beini.ui.fragment.ani.AniFragment;
import com.beini.ui.fragment.ani.AnnotationsFragment;
import com.beini.ui.fragment.ani.BeatFragment;
import com.beini.ui.fragment.args.ArgsFragment;
import com.beini.ui.fragment.bluetooth.SppFragment;
import com.beini.ui.fragment.broadcast.BroadcastFragment;
import com.beini.ui.fragment.control.SnapHeplerFragment;
import com.beini.ui.fragment.cpptest.CppFragment;
import com.beini.ui.fragment.customview.CanvasFragment;
import com.beini.ui.fragment.facerecognition.FacereCongnitioonFragment;
import com.beini.ui.fragment.fingerprint.FingerprintFragment;
import com.beini.ui.fragment.green.GreenDaoFragment;
import com.beini.ui.fragment.home.HomeFragment;
import com.beini.ui.fragment.multimedia.MediaFunctionListFragment;
import com.beini.ui.fragment.net.NetFileFragment;
import com.beini.ui.fragment.picPicker.PicPickeFragment;
import com.beini.ui.fragment.popupwindow.PopupWindowFragment;
import com.beini.ui.fragment.rx.RxAllFragment;
import com.beini.ui.fragment.screenrecord.ScreenRecordFragment;
import com.beini.ui.fragment.service.ServiceFragment;
import com.beini.ui.fragment.shake.ShakeFragment;
import com.beini.ui.fragment.sideslip.SideslipFragment;
import com.beini.ui.fragment.sms.SmsFragment;
import com.beini.ui.fragment.viewflippe.ViewFlippeFragment;
import com.beini.ui.fragment.websocketandsocket.OkhttpWebSocketFragment;
import com.beini.ui.fragment.websocketandsocket.SocketFragment;
import com.beini.ui.fragment.websocketandsocket.WebScoketFragment;
import com.beini.ui.fragment.webview.WebViewTestFragment;
import com.beini.ui.fragment.webview.tx5.Tx5Fragment;
import com.beini.ui.fragment.wifi.WfiListFragment;
import com.beini.util.ObjectUtil;

/**
 * Created by beini on 2017/8/16.
 */

public class AppRouter {

    public static BaseActivity baseActivity;

    /**
     * 入口初始化baseActivity
     */
    public static void indexArouter() {
        baseActivity.replaceFragment(BeatFragment.class);
    }

    public static void loginRouter() {
        baseActivity.replaceFragment(HomeFragment.class);
    }

    /**
     * home
     */
    public static void homeCppRouter() {
        baseActivity.replaceFragment(CppFragment.class);
    }

    public static void homeShakeRouter() {
        baseActivity.replaceFragment(ShakeFragment.class);
    }

    public static void homeSideslipRouter() {
        baseActivity.replaceFragment(SideslipFragment.class);
    }

    public static void homeMediaFunctionRouter() {
        baseActivity.replaceFragment(MediaFunctionListFragment.class);
    }

    public static void homeSppRouter() {
        baseActivity.replaceFragment(SppFragment.class);
    }

    public static void homeSmsRouter() {
        baseActivity.replaceFragment(SmsFragment.class);
    }

    public static void homeNotificationRouter() {
        baseActivity.replaceFragment(BroadcastFragment.class);
    }

    public static void homeBroadcastRouter() {
        baseActivity.replaceFragment(BroadcastFragment.class);
    }

    public static void homeAniRouter() {
        baseActivity.replaceFragment(AniFragment.class);
    }

    public static void homeAIDLRouter() {
        baseActivity.replaceFragment(AIDLFragment.class);
    }

    public static void homeServiceRouter() {
        baseActivity.replaceFragment(ServiceFragment.class);
    }


    public static void homePopupWindowRouter() {
        baseActivity.replaceFragment(PopupWindowFragment.class);
    }

    public static void homeSnapHeplerRouter() {
        baseActivity.replaceFragment(SnapHeplerFragment.class);
    }

    public static void homeCanvasRouter() {
        baseActivity.replaceFragment(CanvasFragment.class);
    }

    /**
     * rb2
     */
    public static void rb2FacereCongnitioonRouter() {
        baseActivity.replaceFragment(FacereCongnitioonFragment.class);
    }

    public static void rb2WebViewRouter() {
        baseActivity.replaceFragment(WebViewTestFragment.class);
    }

    public static void rb2AnnotationsRouter() {
        baseActivity.replaceFragment(AnnotationsFragment.class);
    }

    public static void rb2ScreenRecordRouter() {
        baseActivity.replaceFragment(ScreenRecordFragment.class);
    }

    public static void rb2NetFileRouter() {
        baseActivity.replaceFragment(NetFileFragment.class);
    }

    public static void rb2GreenDaoRouter() {
        baseActivity.replaceFragment(GreenDaoFragment.class);
    }

    public static void rb2WfiListRouter() {
        baseActivity.replaceFragment(WfiListFragment.class);
    }

    public static void rb2FingerprintRouter() {
        baseActivity.replaceFragment(FingerprintFragment.class);
    }

    public static void rb2WebScoketRouter() {
        baseActivity.replaceFragment(WebScoketFragment.class);
    }

    public static void rb2OkhttpWebSocketRouter() {
        baseActivity.replaceFragment(OkhttpWebSocketFragment.class);
    }

    public static void rb2SocketRouter() {
        baseActivity.replaceFragment(SocketFragment.class);
    }

    public static void rb2PicPickeRouter() {
        baseActivity.replaceFragment(PicPickeFragment.class);
    }

    public static void rb2ArgsRouter(Bundle args) {
        baseActivity.replaceFragment(ArgsFragment.class, args);
    }

    public static void rb2ViewFlippeRouter() {
        baseActivity.replaceFragment(ViewFlippeFragment.class);
    }

    public static void rb2Tx5Router() {
        baseActivity.replaceFragment(Tx5Fragment.class);
    }

    public static void rb2RxRouter() {
        baseActivity.replaceFragment(RxAllFragment.class);
    }

    /**
     * rb3
     */
    public static BaseFragment rb3Fragment(Class<?> fragment) {
        BaseFragment baseFragment = (BaseFragment) ObjectUtil.createInstance(fragment);
        return baseFragment;
    }

}
