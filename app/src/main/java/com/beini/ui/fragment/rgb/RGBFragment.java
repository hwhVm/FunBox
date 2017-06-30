package com.beini.ui.fragment.rgb;


import android.widget.SeekBar;
import android.widget.TextView;

import com.beini.R;
import com.beini.base.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;
import com.beini.ui.view.ColorSelectBar;

/**
 * Create by beini 2017/6/23
 *
 */
@ContentView(R.layout.fragment_rgb)
public class RGBFragment extends BaseFragment {
    @ViewInject(R.id.color_select_bar)
    ColorSelectBar color_select_bar;
    @ViewInject(R.id.text_show_color_view)
    TextView text_show_color_view;

    @Override
    public void initView() {
        color_select_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                text_show_color_view.setBackgroundColor(color_select_bar.getColor());
            }
        });
    }
}
