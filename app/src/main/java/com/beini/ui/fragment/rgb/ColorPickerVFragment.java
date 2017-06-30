package com.beini.ui.fragment.rgb;

import com.beini.R;
import com.beini.base.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;
import com.beini.ui.view.colorPicker.ColorPickerVView;
import com.beini.util.BLog;

@ContentView(R.layout.fragment_color_picker_v)
public class ColorPickerVFragment extends BaseFragment {
    @ViewInject(R.id.color_picker_view)
    ColorPickerVView color_picker_view;

    @Override
    public void initView() {
        color_picker_view.setOnColorChangedListener(new ColorPickerVView.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                BLog.d("  color===" + color);
            }
        });
    }


}