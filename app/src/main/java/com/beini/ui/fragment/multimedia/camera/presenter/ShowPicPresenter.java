package com.beini.ui.fragment.multimedia.camera.presenter;

import android.graphics.Bitmap;

import com.beini.ui.fragment.multimedia.camera.Contant;
import com.beini.ui.fragment.multimedia.camera.bean.ImageBean;
import com.beini.ui.fragment.multimedia.camera.utils.Util;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by beini on 2016/7/14.
 */
public class ShowPicPresenter {

	public Observable<List<ImageBean>> _getImagURL() {
		return Observable.just(true)
				.map(new Function<Boolean, List<ImageBean>>() {
					@Override
					public List<ImageBean> apply(Boolean aBoolean) throws Exception {
						List<String> urls = Util.allImageUrl();
						List<ImageBean> bitmaps = new ArrayList<>();
						for (int i = 0; i < urls.size(); i++) {
							ImageBean imageBean = new ImageBean();
							String url = urls.get(i);
							Bitmap bitmap = Util.getImageThumbnail(urls.get(i), Contant.HEIGHT, Contant.WIDTH);
							imageBean.setBitmaps(bitmap);
							imageBean.setUrl(url);
							bitmaps.add(imageBean);
						}
						return bitmaps;
					}
				}).subscribeOn(Schedulers.io());
	}


}
