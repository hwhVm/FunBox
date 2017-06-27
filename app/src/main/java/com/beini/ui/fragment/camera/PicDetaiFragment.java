package com.beini.ui.fragment.camera;

import android.support.v4.view.ViewPager;

import com.beini.R;
import com.beini.adapter.ViewPagerAdapter;
import com.beini.base.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;
import com.beini.event.ImageEvent;
import com.beini.ui.fragment.camera.bean.ImageBean;
import com.beini.util.BLog;

import org.greenrobot.eventbus.EventBus;

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
