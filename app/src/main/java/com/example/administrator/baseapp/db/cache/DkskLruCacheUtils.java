package com.example.administrator.baseapp.db.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import com.example.administrator.baseapp.db.io.DiskLruCache;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by beini on 2017/3/15.
 */

public class DkskLruCacheUtils {
    DiskLruCache mDiskLruCache = null;
    Context context;

    public DkskLruCacheUtils(Context context) {
        this.context = context;
        File cacheDir = getDiskCacheDir(context, "bitmap");
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        //第一个参数指定的是数据的缓存地址，第二个参数指定当前应用程序的版本号，第三个参数指定同一个key可以对应多少个缓存文件，基本都是传1，第四个参数指定最多可以缓存多少字节的数据。
        try {
            mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 每当版本号改变，缓存路径下存储的所有数据都会被清除掉
     *
     * @param context
     * @return
     */
    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 获取到缓存地址的路径，然后判断一下该路径是否存在，如果不存在就创建一下
     *
     * @param context
     * @param uniqueName
     * @return
     */
    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }


    public void clearCache(String key) throws IOException {
        mDiskLruCache.remove(key);
    }

    public void setCache(String key) throws IOException {
        DiskLruCache.Editor editor = mDiskLruCache.edit(key);

    }

    public Bitmap getCache(String key) throws IOException {
        DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
        Bitmap bitmap = null;
        if (snapShot != null) {
            InputStream is = snapShot.getInputStream(0);
            bitmap = BitmapFactory.decodeStream(is);

        }
        return bitmap;
    }
}
