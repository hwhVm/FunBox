package com.beini.ui.fragment.camera.bean;

import android.graphics.Bitmap;

/**
 * Created by beini on 2016/7/15.
 */
public class ImageBean {

	private Bitmap bitmaps;
	private String url;

	public Bitmap getBitmaps() {
		return bitmaps;
	}

	public ImageBean setBitmaps(Bitmap bitmaps) {
		this.bitmaps = bitmaps;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public ImageBean setUrl(String url) {
		this.url = url;
		return this;
	}
}
