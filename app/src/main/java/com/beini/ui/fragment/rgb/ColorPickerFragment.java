package com.beini.ui.fragment.rgb;

import android.widget.TextView;

import com.beini.R;
import com.beini.base.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;
import com.beini.ui.view.colorPicker.ColorPickerView;
import com.beini.utils.BLog;

@ContentView(R.layout.fragment_color_picker)
public class ColorPickerFragment extends BaseFragment {
    @ViewInject(R.id.color_picker_view)
    ColorPickerView color_picker_view;
    @ViewInject(R.id.text_show_color)
    TextView text_show_color;

    @Override
    public void initView() {
        color_picker_view.setOnColorChangedListener(new ColorPickerView.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                BLog.d("  color===" + color);
                text_show_color.setBackgroundColor(color);
            }
        });
    }


}