package com.beini.ui.fragment.camera;


import android.view.View;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;

/**
 * Create by beini 2017/7/26
 */
@ContentView(R.layout.fragment_camera_function_list)
public class CameraFunctionListFragment extends BaseFragment {


    @Override
    public void initView() {

    }

    @Event({R.id.btn_camera_customer, R.id.btn_camera_system,R.id.btn_camera2_system})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_camera_customer:
                baseActivity.replaceFragment(CameraFragment.class);
                break;
            case R.id.btn_camera_system:
                baseActivity.replaceFragment(CallSystemCameraFragment.class);
                break;
            case R.id.btn_camera2_system:
                baseActivity.replaceFragment(Camera2Fragment.class);
                break;
        }
    }
}
