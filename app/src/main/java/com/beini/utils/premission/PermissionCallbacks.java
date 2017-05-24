package com.beini.utils.premission;

import android.support.v4.app.ActivityCompat;

import java.util.List;

/**
 * Created by beini  2017/5/23
 */

public interface PermissionCallbacks extends ActivityCompat.OnRequestPermissionsResultCallback {

    void onPermissionsGranted(int requestCode, List<String> perms);

    void onPermissionsDenied(int requestCode, List<String> perms);

    void onPermissionsAllGranted();

}
