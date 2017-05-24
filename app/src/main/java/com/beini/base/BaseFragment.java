package com.beini.base;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beini.bind.ViewInjectorImpl;
import com.beini.ui.view.dialog.DialogUtil;
import com.beini.ui.view.dialog.UIDialog;


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
        this.returnLoad();
        return view;
    }

    public abstract void initView();


    @Override
    public void onAttach(Context context) {
        baseActivity = (BaseActivity) context;
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            returnLoad();
        }
        super.onHiddenChanged(hidden);
    }

    public void returnLoad() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        baseActivity.setKeyBackListener(null);
        baseActivity.setOnTouchEventListener(null);
        baseActivity.setActivityResultListener(null);
    }

    @Override
    public void onDestroyView() {
//        RefWatcher refWatcher = BaseApplication.getRefWatcher(getActivity());
//        refWatcher.watch(this);
        super.onDestroyView();
    }

    private UIDialog mProDialog;


    public void showProgressDialog() {
        mProDialog = DialogUtil.uploadDailog("");
        mProDialog.show();
    }

    public void hideProgressDialog() {
        if (mProDialog != null) {
            mProDialog.dismiss();
            mProDialog = null;
        }
    }
}
