package com.example.administrator.baseapp.utils;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
    public static void addFragment(FragmentManager fragmentManager, Fragment fragment, String fragmentTag,int id) {

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
     * show fragment
     *
     * @param fragmentManager
     * @param fragment
     */
    public static void removeFragment(FragmentManager fragmentManager, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.remove(fragment);
            ft.commit();
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
            Fragment fragment = fragmentManager.findFragmentByTag(str);
            removeFragment(fragmentManager, fragment);
        }
    }

    public static void removePreFragment(FragmentManager fragmentManager) {
        if (tags!=null&&tags.size() > 1) {
            String old = tags.get(tags.size() - 2);
//            BaseFragment oldFragment = (BaseFragment) getFragmentByTag(old);
            tags.remove(tags.size() - 1);

//            if (oldFragment != null) {
//                add(oldFragment, old, null, enterAnimation, exitAnimation);
//            }
        }
    }

}
