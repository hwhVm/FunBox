package com.beini.utils;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;


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
	public void checkPermission(Activity activity) {
		ActivityCompat.requestPermissions(activity,
				new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
						Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECORD_AUDIO,
						Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,
						Manifest.permission.READ_EXTERNAL_STORAGE,
						Manifest.permission.CAMERA
				}, 1);
	}


	/**
	 * 检测是否具有以下全部权限
	 * @return
     */
	public Boolean resultBoolean() {
//		boolean permission0 = (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(BaseApplication.getInstance(), Manifest.permission.ACCESS_FINE_LOCATION));
//		boolean permission1 = (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(BaseApplication.getInstance(), Manifest.permission.READ_PHONE_STATE));
//		boolean permission2 = (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(BaseApplication.getInstance(), Manifest.permission.READ_EXTERNAL_STORAGE));
//		boolean permission3 = (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(BaseApplication.getInstance(), Manifest.permission.RECORD_AUDIO));
//		boolean permission4 = (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(BaseApplication.getInstance(), Manifest.permission.ACCESS_COARSE_LOCATION));
//		boolean permission5 = (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(BaseApplication.getInstance(), Manifest.permission.WRITE_EXTERNAL_STORAGE));
//		if (permission0 && permission1 && permission2 && permission3 && permission4 && permission5) {
//			return true;
//		}

		return false;
	}
}
