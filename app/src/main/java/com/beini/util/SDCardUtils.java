package com.beini.util;

import android.os.Environment;
import android.os.StatFs;

import com.beini.app.GlobalApplication;

import java.io.File;

/**
 * SD卡相关的辅助类
 * Create by beini 2017/7/27
 */
public class SDCardUtils {
    private SDCardUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * getDataDirectory()                           返回 File ，获取 Android 数据目录。
     * getDownloadCacheDirectory()                    返回 File ，获取 Android 下载/缓存内容目录。
     * getExternalStorageDirectory()                  返回 File ，获取外部存储目录即 SDCard
     * getExternalStoragePublicDirectory(String type) 返回 File ，取一个高端的公用的外部存储器目录来摆放某些类型的文件
     * getExternalStorageState()                      返回 File ，获取外部存储设备的当前状态
     * getRootDirectory()                             返回 File ，获取 Android 的根目录
     */
    public static void printlnEventment() {

        BLog.e("         获取 Android 数据目录   " +                    Environment.getDataDirectory().getAbsolutePath());//  /data
        BLog.e("         获取 Android 下载/缓存内容目录    " +                 Environment.getDownloadCacheDirectory().getAbsolutePath());// /cache
        BLog.e("         获取外部存储目录即 SDCard   " +      Environment.getExternalStorageDirectory().getAbsolutePath());//  /storage/emulated/0
        BLog.e("         取一个高端的公用的外部存储器目录来摆放某些类型的文件       " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath());// /storage/emulated/0/Music
        BLog.e("         获取外部存储设备的当前状态        " +                     Environment.getExternalStorageState());//  mounted
        BLog.e("         获取 Android 的根目录          " +                    Environment.getRootDirectory().getAbsolutePath());// /system
        BLog.e("         sd卡 android目录         " + GlobalApplication.getInstance().getExternalFilesDir(null).getAbsolutePath());// /storage/emulated/0/Android/data/com.beini/files
        BLog.e("         手机内存                " +                      GlobalApplication.getInstance().getCacheDir().getAbsolutePath());//  /data/user/0/com.beini/cache
        BLog.e("         sd           " + GlobalApplication.getInstance().getExternalCacheDir().getAbsolutePath());//    /storage/emulated/0/Android/data/com.beini/cache

    }

    /**
     *StatFs 类
     *StatFs 一个模拟linux的df命令的一个类,获得SD卡和手机内存的使用情况
     *StatFs 常用方法:
     *getAvailableBlocks()   返回 Int ，获取当前可用的存储空间
     *getBlockCount()        返回 Int ，获取该区域可用的文件系统数
     *getBlockSize()         返回 Int ，大小，以字节为单位，一个文件系统
     *getFreeBlocks()        返回 Int ，该块区域剩余的空间
     *restat(String path)    执行一个由该对象所引用的文件系统
     */

    /**
     * 判断SDCard是否可用
     * MEDIA_BAD_REMOVAL，表明SDCard 被卸载前己被移除
     * MEDIA_CHECKING，表明对象正在磁盘检查。
     * MEDIA_MOUNTED，表明对象是否存在并具有读/写权限
     * MEDIA_MOUNTED_READ_ONLY，表明对象权限为只读
     * MEDIA_NOFS，表明对象为空白或正在使用不受支持的文件系统。
     * MEDIA_REMOVED，如果不存在 SDCard 返回
     * MEDIA_SHARED，如果 SDCard 未安装 ，并通过 USB 大容量存储共享 返回
     * MEDIA_UNMOUNTABLE，返回 SDCard 不可被安装 如果 SDCard 是存在但不可以被安装
     * MEDIA_UNMOUNTED，返回 SDCard 已卸掉如果 SDCard   是存在但是没有被安装
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     *
     * @return
     */
    public static long getSDCardAllSize() {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            // 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     *
     * @param filePath
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    public static long getFreeBytes(String filePath) {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }

    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }


}
