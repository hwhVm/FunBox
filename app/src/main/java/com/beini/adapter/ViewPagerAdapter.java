package com.beini.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.beini.R;
import com.beini.ui.fragment.camera.bean.ImageBean;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by beini on 2017/3/13.
 */

public class ViewPagerAdapter extends PagerAdapter {
    List<ImageBean> imageBeanList;
    PhotoView photoView;
    private PhotoViewAttacher mAttacher;
    private Context context;

    public ViewPagerAdapter(List<ImageBean> imageBeanList, PhotoView photoView, Context context) {
        this.imageBeanList = imageBeanList;
        this.photoView = photoView;
        this.context = context;
    }


    @Override

    public Object instantiateItem(ViewGroup container, int position) {
        View view = container.inflate(context,
                R.layout.fragment_pic_detai, null);
        //使用ImageLoader加载图片
//        CSApplication.getInstance().imageLoader.displayImage(
//                mImgUrls.get(position), photoView, DisplayImageOptionsUtil.defaultOptions);
        photoView.setImageURI(Uri.parse("file://" + imageBeanList.get(position).getUrl()));
        //给图片增加点击事件
//        mAttacher = new PhotoViewAttacher(mPhotoView);
//        mAttacher.setOnViewTapListener(PhotoviewActivity.this);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mAttacher = null;
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageBeanList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
