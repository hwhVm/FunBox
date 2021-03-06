package com.beini.util;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;

import com.beini.R;
import com.beini.app.BaseActivity;
import com.beini.app.BaseFragment;
import com.beini.app.GlobalApplication;
import com.beini.ui.fragment.home.HomeFragment;
import com.beini.ui.fragment.home.Rb2Fragment;
import com.beini.ui.fragment.home.Rb3Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beini on 2017/2/9.
 */
public class FragmentHelper {
    public static List<String> tags = new ArrayList<>();
    private static FragmentManager fm;


    /**
     * add fragment
     *
     * @param fragmentManager
     * @param fragment
     */
    public static Fragment addFragment(FragmentManager fragmentManager, Class fragment, String tag) {
        if (fragmentManager != null) {
            if (fm == null) {
                fm = fragmentManager;
            }
            hideAllFragment();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            //查找是否存在Fragment
            Fragment currentFragment = fragmentManager.findFragmentByTag(tag);

            if (currentFragment == null) {//创建
                currentFragment = (Fragment) ObjectUtil.createInstance(fragment);
                ft.add(R.id.content_frame, currentFragment, tag);
                tags.add(tag);
            } else {
                ft.show(currentFragment);
            }
            ft.commit();

            return currentFragment;
        }
        return null;
    }


    /**
     * show fragment
     *
     * @param fragment
     */
    @SuppressLint("ResourceType")
    public static void showFragment(Fragment fragment) {
        FragmentTransaction ft = fm.beginTransaction();
        hideAllFragment();
        if (fragment != null) {
//            ft.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit, R.anim.fragment_slide_left_enter,
//                    R.anim.fragment_slide_left_exit);//动画
            ft.show(fragment);
            ft.commit();
        }
    }

    public static void hideOrShow(Fragment fragment, boolean isHidden) {
        FragmentTransaction ft = fm.beginTransaction();
        FragmentHelper.hideAllFragment();
        if (isHidden) {
            ft.hide(fragment);
        } else {
            ft.show(fragment);
        }
        ft.commit();
    }

    /**
     * remove fragment
     *
     * @param fragment
     */
    @SuppressLint("ResourceType")
    public static void removeFragment(Fragment fragment) {
        FragmentTransaction ft = fm.beginTransaction();
        if (fragment != null) {
            if (tags != null && tags.size() >= 1) {
//                ft.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit, R.anim.fragment_slide_left_enter,
//                        R.anim.fragment_slide_left_exit);//动画
                ft.remove(fragment);
                ft.commit();
                tags.remove(tags.size() - 1);
            }
        }
    }

    /**
     * hide all fragment
     */
    public static void hideAllFragment() {
        FragmentTransaction ft = fm.beginTransaction();
        for (String str : tags) {
            Fragment fragment = fm.findFragmentByTag(str);
            if (fragment != null && !fragment.isHidden()) {
                ft.hide(fragment);
            }
        }
        ft.commit();
    }

    /**
     * remove all fragment
     */
    public static void removeAllFragment() {
        for (String str : tags) {
            tags.remove(tags.size() - 1);
            Fragment fragment = fm.findFragmentByTag(str);
            removeFragment(fragment);
        }
    }

    private static long mLastKeyDown = 0;

    public static void removePreFragment(BaseActivity baseActivity) {
        if (tags != null && tags.size() > 1 && !isHomeFragment(fm)) {//不在主页面
            String current = tags.get(tags.size() - 1);
            String old = tags.get(tags.size() - 2);

            removeFragment(fm.findFragmentByTag(current));
            BaseFragment oldFragment = (BaseFragment) fm.findFragmentByTag(old);

            if ((oldFragment instanceof HomeFragment || oldFragment instanceof Rb2Fragment || oldFragment instanceof Rb3Fragment)) {
                baseActivity.setBottom(View.VISIBLE);
            }
            if (oldFragment != null) {
                showFragment(oldFragment);
            }
        } else {
            ToastUtils.showShortToast(GlobalApplication.getInstance().getString(R.string.app_exit));
            long timeMillis = System.currentTimeMillis();
            if (timeMillis - mLastKeyDown >= 2000) {
                mLastKeyDown = timeMillis;
            } else {
                System.exit(0);
            }
        }
    }

    private static boolean isHomeFragment(FragmentManager fragmentManager) {
        return fragmentManager.findFragmentById(R.id.content_frame) instanceof HomeFragment || fragmentManager.findFragmentById(R.id.content_frame) instanceof Rb2Fragment || fragmentManager.findFragmentById(R.id.content_frame) instanceof Rb3Fragment;
    }


    public static FragmentManager getFm() {
        return fm;
    }

    public static void setFm(FragmentManager fm) {
        FragmentHelper.fm = fm;
    }

}
