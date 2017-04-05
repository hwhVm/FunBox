package com.beini.utils;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;

import com.beini.R;
import com.beini.base.BaseActivity;
import com.beini.base.BaseApplication;
import com.beini.base.BaseFragment;
import com.beini.ui.fragment.home.HomeFragment;
import com.beini.ui.fragment.home.Rb2Fragment;
import com.beini.ui.fragment.home.Rb3Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beini on 2017/2/9.
 */
public class FragmentHelper {
    private static List<String> tags = new ArrayList<>();
    public static int homeTag = 0;

    /**
     * add fragment
     *
     * @param fragmentManager
     * @param fragment
     * @param fragmentTag
     */
    public static void addFragment(FragmentManager fragmentManager, Fragment fragment, String fragmentTag) {
        if (fragmentManager != null && fragment != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (!fragment.isAdded()) {
                ft.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit, R.anim.fragment_slide_left_enter,
                        R.anim.fragment_slide_left_exit);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            }
            ft.add(R.id.content_frame, fragment, fragmentTag);
            tags.add(fragmentTag);
            ft.commit();
        }
    }

    /**
     * show fragment
     *
     * @param fragmentManager
     * @param fragment
     */
    public static void showFragment(FragmentManager fragmentManager, Fragment fragment) {
        hideAllFragment(fragmentManager);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (fragment != null) {
            ft.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit, R.anim.fragment_slide_left_enter,
                    R.anim.fragment_slide_left_exit);
            ft.show(fragment);
            ft.commit();
        }
    }

    /**
     * remove fragment
     *
     * @param fragmentManager
     * @param fragment
     */
    public static void removeFragment(FragmentManager fragmentManager, Fragment fragment) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (fragment != null) {
            if (tags != null && tags.size() >= 1) {
                ft.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit, R.anim.fragment_slide_left_enter,
                        R.anim.fragment_slide_left_exit);
                ft.remove(fragment);
                ft.commit();
                tags.remove(tags.size() - 1);
            }
        }
    }


    /**
     * hide all fragment
     *
     * @param fragmentManager
     */
    public static void hideAllFragment(FragmentManager fragmentManager) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        for (String str : tags) {
            Fragment fragment = fragmentManager.findFragmentByTag(str);
            if (fragment != null && !fragment.isHidden()) {
                ft.hide(fragment);
            }
        }
        ft.commit();
    }

    /**
     * remove all fragment
     *
     * @param fragmentManager
     */
    public static void removeAllFragment(FragmentManager fragmentManager) {
        for (String str : tags) {
            tags.remove(tags.size() - 1);
            Fragment fragment = fragmentManager.findFragmentByTag(str);
            removeFragment(fragmentManager, fragment);
        }
    }

    static long mLastKeyDown = 0;

    public static void removePreFragment(View view, FragmentManager fragmentManager, BaseActivity baseActivity) {

        if (tags != null && tags.size() > 1 && isHomeFragment(fragmentManager)) {
            String current = tags.get(tags.size() - 1);
            String old = tags.get(tags.size() - 2);
            removeFragment(fragmentManager, fragmentManager.findFragmentByTag(current));

            BaseFragment oldFragment = (BaseFragment) fragmentManager.findFragmentByTag(old);
            if (oldFragment instanceof HomeFragment || oldFragment instanceof Rb2Fragment || oldFragment instanceof Rb3Fragment) {
                baseActivity.setBottom(View.VISIBLE);
                Fragment baseFragment = fragmentManager.findFragmentByTag(HomeFragment.class.getName());
                if (homeTag == 2) {
                    baseFragment = fragmentManager.findFragmentByTag(Rb3Fragment.class.getName());
                } else if (homeTag == 1) {
                    baseFragment = fragmentManager.findFragmentByTag(Rb2Fragment.class.getName());
                }
                showFragment(fragmentManager, baseFragment);
            } else {
                if (oldFragment != null) {
                    showFragment(fragmentManager, oldFragment);
                }
            }

        } else {
            SnackbarUtil.showShort(view, BaseApplication.getInstance().getString(R.string.app_exit));
            long timeMillis = System.currentTimeMillis();
            if (timeMillis - mLastKeyDown >= 2000) {
                mLastKeyDown = timeMillis;
            } else {
                System.exit(0);
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
}
