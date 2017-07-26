package com.beini.app;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beini.R;
import com.beini.bind.ViewInjectorImpl;
import com.beini.ui.view.dialog.DialogUtil;
import com.beini.ui.view.dialog.UIDialog;
import com.beini.util.FragmentHelper;
import com.beini.util.premission.EasyPermissions;
import com.beini.util.premission.PermissionCallbacks;
import com.beini.util.premission.PermissionUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by beini on 2017/2/8.
 */


public abstract class BaseFragment extends Fragment implements PermissionCallbacks {

    public BaseActivity baseActivity;
    private String remindTxt;//解释为什么申请权限
    private int requestCode;
    private String[] permissions;
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    private static final String SAVE_FRAGMENTS_SIZE = "SAVE_FRAGMENTS_SIZE";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = ViewInjectorImpl.registerInstance(this, inflater, container);
        this.initView();
        this.returnLoad();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            FragmentHelper.tags = savedInstanceState.getStringArrayList(SAVE_FRAGMENTS_SIZE);
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentHelper.setFm(baseActivity.getFragmentManager());
            FragmentHelper.hideOrShow(this, isSupportHidden);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
        outState.putStringArrayList(SAVE_FRAGMENTS_SIZE, (ArrayList<String>) FragmentHelper.tags);
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
        super.onDestroyView();
    }

    /**
     * 权限回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 权限回调接口
     */
    private CheckPermListener mListener;

    public interface CheckPermListener {
        //权限通过后的回调方法
        void superPermission();
    }

    /**
     * 检查权限。支持多个权限一起
     *
     * @param listener
     * @param requestCode
     * @param mPerms
     */
    public void checkPermission(CheckPermListener listener, int requestCode, String[] mPerms) {

        String msg = null;
        mListener = listener;
        this.requestCode = requestCode;
        this.permissions = mPerms;
        for (int i = 0; i < mPerms.length; i++) {
            msg = PermissionUtils.getSingleton().getPermissionString(mPerms[i]) + " ";
        }
        remindTxt = getString(R.string.string_help_text1) + msg + "\n" + getString(R.string.string_help_text2) + "\n" + getString(R.string.string_help_text3);

        EasyPermissions.setCode(requestCode);
        if (EasyPermissions.hasPermissions(this.getActivity(), mPerms)) {
            if (mListener != null)
                mListener.superPermission();
        } else {

            EasyPermissions.requestPermissions(this, remindTxt, requestCode, mPerms);
        }
    }

    /**
     * 返回值回调方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.requestCode == requestCode) {
            checkPermission(mListener, requestCode, permissions);
        }
    }

    /**
     * 同意了某些权限没有全部同意
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

        String[] permStr = new String[perms.size()];
        for (int i = 0; i < permStr.length; i++) {
            permStr[i] = perms.get(i);
        }
        checkPermission(mListener, requestCode, permStr);
    }

    /**
     * 同意了全部权限的回调
     */
    @Override
    public void onPermissionsAllGranted() {

        if (mListener != null)
            mListener.superPermission();//同意了全部权限的回调
    }

    /**
     * 拒绝申请权限 ，默认返回上一页
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        EasyPermissions.checkDeniedPermissionsNeverAskAgain(this,
                remindTxt,
                R.string.dialog_ok, R.string.dialog_cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentHelper.removePreFragment(baseActivity);
                    }
                }, perms);
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    /**
     * 对话框
     */
    public void showProgressDialog() {
        mProDialog = DialogUtil.uploadDailog("");
        mProDialog.show();
    }

    private UIDialog mProDialog;

    public void hideProgressDialog() {
        if (mProDialog != null) {
            mProDialog.dismiss();
            mProDialog = null;
        }
    }
}
