package com.beini.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by beini on 2017/7/24.
 */

public class FragmentManagerBean implements Parcelable {
    private String temp;

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    //    private FragmentManager fragmentManager;
    public FragmentManagerBean(String name) {
        this.temp = name;
    }

    protected FragmentManagerBean(Parcel in) {
    }


//    public FragmentManager getFragmentManager() {
//        return fragmentManager;
//    }
//
//    public void setFragmentManager(FragmentManager fragmentManager) {
//        this.fragmentManager = fragmentManager;
//    }
//
//    public FragmentManagerBean(FragmentManager fragmentManager) {
//        this.fragmentManager = fragmentManager;
//    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public static final Creator<FragmentManagerBean> CREATOR = new Creator<FragmentManagerBean>() {
        @Override
        public FragmentManagerBean createFromParcel(Parcel in) {
            return new FragmentManagerBean(in);
        }

        @Override
        public FragmentManagerBean[] newArray(int size) {
            return new FragmentManagerBean[size];
        }
    };
}
