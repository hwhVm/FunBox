package com.beini.ui.fragment.rgb;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;
import com.beini.ui.view.colorPicker.ColorPickerView;
import com.beini.util.BLog;

@ContentView(R.layout.fragment_color_picker)
public class ColorPickerFragment extends BaseFragment {
    @ViewInject(R.id.color_picker_view)
    ColorPickerView color_picker_view;

    @Override
    public void initView() {
        color_picker_view.setOnColorChangedListener(new ColorPickerView.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                BLog.e("  color===" + color);
            }
        });
    }


}