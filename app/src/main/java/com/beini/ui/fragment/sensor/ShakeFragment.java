package com.beini.ui.fragment.sensor;

import android.hardware.Camera;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;

/**
 * Create by beini 2018/1/19
 */
@ContentView(R.layout.fragment_shake)
public class ShakeFragment extends BaseFragment {
    @ViewInject(R.id.toggle_btn)
    ToggleButton toggle_btn;
    private ShakeListener mShakeListener = null;
    public static boolean isChecked = false;
    public static boolean flashTag = false;

    @Override
    public void initView() {
        baseActivity.setBottom(View.GONE);
        baseActivity.setTopBar(View.GONE);
        mShakeListener = new ShakeListener(getActivity());

        toggle_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ShakeFragment.isChecked = isChecked;
            }
        });

        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
            @Override
            public void onShake() {
                if (isChecked) {
                    if (flashTag) {
                        closeFlashLamp();
                        flashTag = false;
                    } else {
                        openFlashLamp();
                        flashTag = true;
                    }
                }

            }
        });

    }

    public void openFlashLamp() {
        Camera m_Camera = Camera.open();
        Camera.Parameters mParameters;
        mParameters = m_Camera.getParameters();
        mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        m_Camera.setParameters(mParameters);
    }

    public void closeFlashLamp() {
        Camera m_Camera = Camera.open();
        Camera.Parameters mParameters;
        mParameters = m_Camera.getParameters();
        mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        m_Camera.setParameters(mParameters);
        m_Camera.release();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
