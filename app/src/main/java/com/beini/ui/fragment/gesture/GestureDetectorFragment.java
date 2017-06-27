package com.beini.ui.fragment.gesture;


import android.widget.ImageView;

import com.beini.R;
import com.beini.base.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;

/**
 * Create by beiini  2017/6/27
 */
@ContentView(R.layout.fragment_gesture_detector)
public class GestureDetectorFragment extends BaseFragment {
    @ViewInject(R.id.image_gesture_scale)
    ImageView image_gesture_scale;



    @Override
    public void initView() {
    }



}
