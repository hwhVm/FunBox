package com.example.administrator.baseapp.ui.fragment.camera;


import android.graphics.drawable.Drawable;

import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.base.BaseFragment;
import com.example.administrator.baseapp.bind.ContentView;
import com.example.administrator.baseapp.bind.ViewInject;
import com.example.administrator.baseapp.ui.fragment.camera.bean.ImageBean;
import com.facebook.drawee.view.SimpleDraweeView;
import org.greenrobot.eventbus.EventBus;

import java.net.URL;

import uk.co.senab.photoview.PhotoView;

/**
 * create by beini  2017/8/10
 */
@ContentView(R.layout.fragment_pic_detai)
public class PicDetaiFragment extends BaseFragment {
    @ViewInject(R.id.picDetail_simpleView)
    SimpleDraweeView picDetail_simpleView;
    @ViewInject(R.id.photo_view)
    PhotoView photoView;

    @Override
    public void initView() {
//        picDetail_simpleView.setImageURI("file://" + EventBus.getDefault().getStickyEvent(ImageBean.class).getUrl());
//
//        Drawable bitmap = getResources().getDrawable(R.mipmap.ic_launcher);
//        photoView.setImageDrawable(bitmap);
//        URL  url=new URL("file://" + EventBus.getDefault().getStickyEvent(ImageBean.class).getUrl())
//        photoView.setImageURI();
    }

}
