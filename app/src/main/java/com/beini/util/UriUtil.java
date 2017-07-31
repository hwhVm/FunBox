package com.beini.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by beini on 2017/7/31.
 */

public class UriUtil {
    /**
     * 从URI获取本地路径
     * content://那么这种就要去数据库读取path
     * file:///那么这种是 Uri.fromFile(File file)得到的
     */
    public static String getSDPath(Activity activity, Uri contentUri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        String urlpath;
        CursorLoader loader = new CursorLoader(activity, contentUri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        try {
            int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            urlpath = cursor.getString(column_index);
            return urlpath;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        urlpath = contentUri.getPath();
        return urlpath;
    }

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 真实路径转换Uri
     */
    public static Uri fileToUri(File file) {
        return Uri.parse(file.getAbsolutePath());
    }

    /**
     * 从真实路径转换为媒体库URI
     */
    private Uri pathToMediaUri(String path, Activity activity) {
        Uri mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = activity.getContentResolver().query(mediaUri,
                null,
                MediaStore.Images.Media.DISPLAY_NAME + "=" + path.substring(path.lastIndexOf("/") + 1),
                null,
                null);
        cursor.moveToFirst();

        return ContentUris.withAppendedId(mediaUri, cursor.getLong(0));
    }

}
