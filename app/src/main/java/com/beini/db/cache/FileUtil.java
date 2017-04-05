package com.beini.db.cache;

import android.annotation.SuppressLint;
import android.os.Environment;

import com.beini.base.BaseApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;

/**
 * Created by beini on 2016/5/5.
 */
public class FileUtil {
	/**
	 * 把数据库文件从内部存储复制到外部存储，多线程.
	 */
	@SuppressLint("SdCardPath")
	public static void copyDBFile() {

		FileInputStream fis = null;
		FileOutputStream fos = null;
		//使用通道，操作更快捷.
		FileChannel in = null;
		FileChannel out = null;

		// 包路径, 从这里复制数据库文件
		StringBuilder path = new StringBuilder();
		path.append("/data/data/");
		path.append(BaseApplication.getInstance().getPackageName());//Application
		path.append("/databases");
		// 测试阶段， 指定要复制的文件名， 这个为数据库文件
		File sourceFile = new File(path.toString(), "要复制的数据库文件名");

		// 外部存储路径, 目标路径.
		String AuraBoxDirPath = Environment.getExternalStorageDirectory() + "/xxx/";

		File AuraBoxDir = null;
		if (AuraBoxDirPath != null) {
			AuraBoxDir = new File(AuraBoxDirPath);
		}
		if (AuraBoxDir != null) {
			if (!AuraBoxDir.exists()) {
				AuraBoxDir.mkdir();
				//Log.i("创建 AuraBox 目录");
			}
		}
		//复制后的数据库名称
		File outFile = new File(AuraBoxDir, "" + ".db");
		try {
			if (!outFile.exists()) {
				outFile.createNewFile();
			}
			// 初始化文件流
			fis = new FileInputStream(sourceFile);
			in = fis.getChannel();
			fos = new FileOutputStream(outFile);
			out = fos.getChannel();

			//复制文件
			in.transferTo(0, in.size(), out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭流
			try {
				if (fis != null) {
					fis.close();
				}
				if (in != null) {
					in.close();
				}
				if (fos != null) {
					fos.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 格式化单位
	 *
	 * @param size
	 *
	 * @return
	 */
	public static String getFormatSize(double size) {
		double kiloByte = size / 1024;
		if (kiloByte < 1) {
//            return size + "Byte";
			return "0K";
		}

		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "K";
		}

		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "M";
		}

		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
				+ "TB";
	}

	// 获取文件大小
	//Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
	//Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
	public long getFolderSize(File file) throws Exception {
		long size = 0;
		try {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				// 如果下面还有文件
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}
}
