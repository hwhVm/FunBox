package com.example.administrator.baseapp.base;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.baseapp.bind.ViewInjectorImpl;


/**
 * Created by beini on 2017/2/8.
 */


public abstract class BaseFragment extends Fragment {
    public BaseActivity baseActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = ViewInjectorImpl.registerInstance(this, inflater, container);
        this.initView();
        return view;
    }

    public abstract void initView();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onAttach(Context context) {
        baseActivity = (BaseActivity) context;
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
