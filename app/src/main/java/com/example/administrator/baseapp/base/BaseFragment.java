package com.example.administrator.baseapp.base;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.baseapp.bind.ViewInjectorImpl;
import com.example.administrator.baseapp.utils.BLog;
import com.example.administrator.baseapp.utils.permission.EasyPermissions;

import java.util.List;


/**
 * Created by beini on 2017/2/8.
 */


public abstract class BaseFragment extends Fragment implements EasyPermissions.PermissionCallbacks{
    public BaseActivity baseActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = ViewInjectorImpl.registerInstance(this, inflater, container);
        this.initView();
        BLog.d("  onCreateView ");
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

    public void checkPermissionMethod(String[] perms, String tipStr, int code) {
        BLog.d("  EasyPermissions.hasPermissions(this, perms)="+EasyPermissions.hasPermissions(baseActivity, perms));
        if (!EasyPermissions.hasPermissions(baseActivity, perms)) {
            EasyPermissions.requestPermissions(this, tipStr, code, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

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
    //            new AppSettingsDialog.Builder(this).build().show();

            }
        }
    }
}
