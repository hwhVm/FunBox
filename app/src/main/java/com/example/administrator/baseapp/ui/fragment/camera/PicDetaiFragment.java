package com.example.administrator.baseapp.ui.fragment.camera;


import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.view.ViewPager;

import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.adapter.ViewPagerAdapter;
import com.example.administrator.baseapp.base.BaseFragment;
import com.example.administrator.baseapp.bind.ContentView;
import com.example.administrator.baseapp.bind.Event;
import com.example.administrator.baseapp.bind.ViewInject;
import com.example.administrator.baseapp.event.ImageEvent;
import com.example.administrator.baseapp.ui.fragment.camera.bean.ImageBean;
import com.example.administrator.baseapp.utils.BLog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * create by beini  2017/8/10
 */
@ContentView(R.layout.fragment_pic_detai)
public class PicDetaiFragment extends BaseFragment {
    @ViewInject(R.id.photo_view)
    PhotoView photoView;
    @ViewInject(R.id.view_page)
    ViewPager view_page;

    ViewPagerAdapter vpAdapter;
    List<ImageBean> imageBeanList;
    int postion = 0;

    @Override
    public void initView() {
        ImageEvent imageEvent = EventBus.getDefault().getStickyEvent(ImageEvent.class);
        if (imageEvent != null) {
            imageBeanList = imageEvent.imageBeens;
            postion = imageEvent.postiton;
        }
        EventBus.getDefault().removeStickyEvent(ImageEvent.class);

//        photoView.setImageURI(Uri.parse("file://" + EventBus.getDefault().getStickyEvent(ImageBean.class).getUrl()));

        vpAdapter = new ViewPagerAdapter(imageBeanList, getPhotoView(), getActivity());
        view_page.setAdapter(vpAdapter);
        view_page.setCurrentItem(postion);

        view_page.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BLog.d("    onPageSelected  position=  " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * get set
     */
    public PhotoView getPhotoView() {
        return photoView;
    }

    public void setPhotoView(PhotoView photoView) {
        this.photoView = photoView;
    }

}
