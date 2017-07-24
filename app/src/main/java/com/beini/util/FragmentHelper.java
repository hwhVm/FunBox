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
    public static int homeTag = 0;

    /**
     * add fragment
     *
     * @param fragmentManager
     * @param fragment
     */
    public static void addFragment(FragmentManager fragmentManager, Fragment fragment) {
        if (fragmentManager != null && fragment != null) {
            if (fm == null) {
                fm = fragmentManager;
            }
            hideAllFragment();
            FragmentTransaction ft = fragmentManager.beginTransaction();
//            if (!fragment.isAdded()) {
//                ft.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit, R.anim.fragment_slide_left_enter,
//                        R.anim.fragment_slide_left_exit);//动画
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//            }
            ft.add(R.id.content_frame, fragment, fragment.getClass().getName());
            tags.add(fragment.getClass().getName());
            ft.commit();
        }
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
        BLog.e("          tags.size()=" + tags.size());
        for (int i = 0; i < tags.size(); i++) {
            BLog.e("     " + tags.get(i));
        }
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
        if (tags != null && tags.size() > 1 && isHomeFragment(fm)) {//不在主页面
            String current = tags.get(tags.size() - 1);
            String old = tags.get(tags.size() - 2);

            removeFragment(fm.findFragmentByTag(current));
            BaseFragment oldFragment = (BaseFragment) fm.findFragmentByTag(old);

            if ((oldFragment instanceof HomeFragment || oldFragment instanceof Rb2Fragment || oldFragment instanceof Rb3Fragment)) {
                baseActivity.setBottom(View.VISIBLE);
                Fragment baseFragment = fm.findFragmentByTag(HomeFragment.class.getName());
                if (homeTag == 2) {
                    baseFragment = fm.findFragmentByTag(Rb3Fragment.class.getName());
                } else if (homeTag == 1) {
                    baseFragment = fm.findFragmentByTag(Rb2Fragment.class.getName());
                }
                showFragment(baseFragment);
            } else {
                if (oldFragment != null) {
                    showFragment(oldFragment);
                }
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

    /**
     * print  all   save  frgment
     */
    public static void logPrint() {
        if (tags != null) {
            BLog.d("  logPrint=" + tags.size());
            for (int i = 0; i < tags.size(); i++) {
                BLog.d("  d    tags===" + tags.get(i));
            }
        }
    }

    private static boolean isHomeFragment(FragmentManager fragmentManager) {
        if (fragmentManager.findFragmentById(R.id.content_frame) instanceof HomeFragment || fragmentManager.findFragmentById(R.id.content_frame) instanceof Rb2Fragment || fragmentManager.findFragmentById(R.id.content_frame) instanceof Rb3Fragment) {
            return false;
        } else {
            return true;
        }
    }


    public static FragmentManager getFm() {
        return fm;
    }

    public static void setFm(FragmentManager fm) {
        FragmentHelper.fm = fm;
    }

}
