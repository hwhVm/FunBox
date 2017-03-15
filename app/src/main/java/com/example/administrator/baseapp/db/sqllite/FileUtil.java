package com.example.administrator.baseapp.db.sqllite;

import android.annotation.SuppressLint;
import android.os.Environment;

import com.example.administrator.baseapp.base.BaseApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

}
