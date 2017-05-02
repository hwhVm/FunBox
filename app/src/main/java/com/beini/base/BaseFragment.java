package com.beini.base;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beini.bind.ViewInjectorImpl;
import com.beini.ui.view.dialog.DialogUtil;
import com.beini.ui.view.dialog.UIDialog;
import com.beini.utils.BLog;
import com.beini.utils.permission.EasyPermissions;
import com.squareup.leakcanary.RefWatcher;

import java.util.List;


/**
 * Created by beini on 2017/2/8.
 */


public abstract class BaseFragment extends Fragment implements EasyPermissions.PermissionCallbacks {
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

    public void checkPermissionMethod(String[] perms, String tipStr, int code) {
        BLog.d("       EasyPermissions.hasPermissions(baseActivity, perms)="+EasyPermissions.hasPermissions(baseActivity, perms));
        if (!EasyPermissions.hasPermissions(baseActivity, perms)) {
            EasyPermissions.requestPermissions(this, tipStr, code, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BLog.d("  onRequestPermissionsResult   "+ requestCode );
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        BLog.d("    onPermissionsGranted  " + requestCode);

    }


    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            BLog.d("   onPermissionsDenied  " + requestCode + "   " + (EasyPermissions.somePermissionPermanentlyDenied(this, perms)));
            if (!EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//                            new AppSettingsDialog.Builder(this).build().show();

            }
        }
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
        RefWatcher refWatcher = BaseApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
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
