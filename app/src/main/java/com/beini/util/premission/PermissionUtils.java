package com.beini.util.premission;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.beini.app.GlobalApplication;

/**
 * Create by beini 2017/7/11
 */
public class PermissionUtils {

    private volatile static PermissionUtils instance;

    private PermissionUtils() {
    }

    public static PermissionUtils getSingleton() {
        if (instance == null) {
            synchronized (PermissionUtils.class) {
                if (instance == null) {
                    instance = new PermissionUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 检查App有没定位权限，
     * 没有的话会弹出授系统权对话框。
     */
    public void checkPermission() {
//		RxPermissions.getInstance(activity)
//				.request(Manifest.permission.CAMERA,
//						Manifest.permission.READ_PHONE_STATE)
//				.subscribe(new Action1<Boolean>() {
//					@Override
//					public void call(Boolean aBoolean) {
//						if (aBoolean){
//							LogUtil.e("拥有权限-------------》");
//						}
//					}
//				});
        ActivityCompat.requestPermissions(GlobalApplication.getInstance().getCurActivity(),
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                }, 1);
    }

    public String getPermissionString(String permission) {
        if (Manifest.permission.RECORD_AUDIO.equals(permission)) {
//            return getString(R.string.permission_record);
            return "permission_record";
        } else if (Manifest.permission.RECORD_AUDIO.equals(permission)) {
//            return getString(R.string.permission_camera);
            return "permission_camera";
        } else if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permission)) {
//            return getString(R.string.permission_location);
            return "permission_location";
        } else if (Manifest.permission.ACCESS_COARSE_LOCATION.equals(permission)) {
//            return getString(R.string.permission_location);
            return "permission_location";
        } else if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(permission)) {
//            return getString(R.string.permission_memory);
            return "permission_memory";
        } else if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission)) {
//            return getString(R.string.permission_memory);
            return "permission_memory";
        } else
            return "";
    }


    /**
     * 检测是否具有以下全部权限
     *
     * @return
     */
    public Boolean resultBoolean() {
        boolean permission0 = (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(GlobalApplication.getInstance(), Manifest.permission.ACCESS_FINE_LOCATION));
        boolean permission1 = (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(GlobalApplication.getInstance(), Manifest.permission.ACCESS_COARSE_LOCATION));
        boolean permission2 = (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(GlobalApplication.getInstance(), Manifest.permission.READ_EXTERNAL_STORAGE));
        boolean permission3 = (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(GlobalApplication.getInstance(), Manifest.permission.WRITE_EXTERNAL_STORAGE));
        if (permission0 && permission1 && permission2 && permission3) {
            return true;
        }

        return false;
    }


    // 启动应用的设置
    private static final String PACKAGE_URL_SCHEME = "package:"; // 方案

    public void startAppSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + activity.getPackageName()));
        activity.startActivity(intent);
    }
}
