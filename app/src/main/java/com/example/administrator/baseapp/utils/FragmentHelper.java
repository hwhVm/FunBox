package com.example.administrator.baseapp.utils;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;

import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.base.BaseApplication;
import com.example.administrator.baseapp.base.BaseFragment;
import com.example.administrator.baseapp.ui.fragment.home.HomeFragment;
import com.example.administrator.baseapp.ui.fragment.home.Rb2Fragment;
import com.example.administrator.baseapp.ui.fragment.home.Rb3Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beini on 2017/2/9.
 */
public class FragmentHelper {
    private static List<String> tags = new ArrayList<>();

    /**
     * add fragment
     *
     * @param fragmentManager
     * @param fragment
     * @param fragmentTag
     */
    public static void addFragment(FragmentManager fragmentManager, Fragment fragment, String fragmentTag, int id) {

        if (fragmentManager != null && fragment != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(id, fragment, fragmentTag);
            tags.add(fragmentTag);
            ft.commit();
        }
        fragmentManager.popBackStack();
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
        if (fragment != null) {
            if (tags != null && tags.size() > 1) {
                if (fragment != null) {
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.remove(fragment);
                    ft.commit();
                    tags.remove(tags.size() - 1);
                }
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

    public static void removePreFragment(View view, FragmentManager fragmentManager) {

        if (tags != null && tags.size() > 1 && isHomeFragment(fragmentManager)) {
            String old = tags.get(tags.size() - 2);
            BaseFragment oldFragment = (BaseFragment) fragmentManager.findFragmentByTag(old);
            tags.remove(tags.size() - 1);
            if (oldFragment != null) {
                showFragment(fragmentManager, oldFragment);
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
}
