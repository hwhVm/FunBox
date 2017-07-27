package com.beini.ui.fragment.multimedia.camera.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.text.TextUtils;

import com.beini.util.BLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by beini on 2016/7/14.
 */
public class Util {

	//从sd卡上加载图片
	public static Bitmap decodeSampledBitmapFromFd(String pathName,
                                                   int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false;
		Bitmap src = BitmapFactory.decodeFile(pathName, options);
		return createScaleBitmap(src, reqWidth, reqHeight);
	}

	private static int calculateInSampleSize(BitmapFactory.Options options,
											 int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	// 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响
	private static Bitmap createScaleBitmap(Bitmap src, int dstWidth,
                                            int dstHeight) {
		Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
		if (src != dst) { // 如果没有缩放，那么不回收
			src.recycle(); // 释放Bitmap的native像素数组
		}
		return dst;
	}

	/**
	 * 将拍下来的照片存放在SD卡中
	 */
	public static void saveToSDCard(byte[] data) throws IOException {
		BLog.d("   saveToSDCard");
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
		String filename = format.format(date) + ".jpg";
		File fileFolder = new File(Environment.getExternalStorageDirectory()
				+ "/APIC/");
		if (!fileFolder.exists()) { // 如果目录不存在，则创建一个名为"finger"的目录
			fileFolder.mkdir();
		}
		File jpgFile = new File(fileFolder, filename);
		FileOutputStream outputStream = new FileOutputStream(jpgFile); // 文件输出流
		outputStream.write(data); // 写入sd卡中
		outputStream.close(); // 关闭输出流
	}
	/**
	 * 将拍下来的照片存放在SD卡中
	 */
	public static void saveToSDCard(byte[] data,String   filename) throws IOException {
		BLog.d("   saveToSDCard");
		File fileFolder = new File(Environment.getExternalStorageDirectory()
				+ "/APIC/");
		if (!fileFolder.exists()) { // 如果目录不存在，则创建一个名为"finger"的目录
			fileFolder.mkdir();
		}
		File jpgFile = new File(fileFolder, filename);
		FileOutputStream outputStream = new FileOutputStream(jpgFile); // 文件输出流
		outputStream.write(data); // 写入sd卡中
		outputStream.close(); // 关闭输出流
	}
	/**
	 * 遍历文件夹下拍摄图片的SD卡地址
	 */

	public static List<String> allImageUrl() {
		List<String> strUrl = new ArrayList<>();
		File fileFolder = new File(Environment.getExternalStorageDirectory()
				+ "/APIC/");
		if (fileFolder.exists()) {
			File[] files = fileFolder.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					strUrl.add(files[i].getAbsolutePath());
				}
			}
		}
		return strUrl;
	}
	public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}


	//通过路径获取Bitmap的缩略图

	public Bitmap getBitmapByWidth(String localImagePath, int width, int addedScaling) {
		if (TextUtils.isEmpty(localImagePath)) {
			return null;
		}

		Bitmap temBitmap = null;

		try {
			BitmapFactory.Options outOptions = new BitmapFactory.Options();

			// 设置该属性为true，不加载图片到内存，只返回图片的宽高到options中。
			outOptions.inJustDecodeBounds = true;

			// 加载获取图片的宽高
			BitmapFactory.decodeFile(localImagePath, outOptions);

			int height = outOptions.outHeight;

			if (outOptions.outWidth > width) {
				// 根据宽设置缩放比例
				outOptions.inSampleSize = outOptions.outWidth / width + 1 + addedScaling;
				outOptions.outWidth = width;

				// 计算缩放后的高度
				height = outOptions.outHeight / outOptions.inSampleSize;
				outOptions.outHeight = height;
			}

			// 重新设置该属性为false，加载图片返回
			outOptions.inJustDecodeBounds = false;
			temBitmap = BitmapFactory.decodeFile(localImagePath, outOptions);
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return temBitmap;
	}
}
