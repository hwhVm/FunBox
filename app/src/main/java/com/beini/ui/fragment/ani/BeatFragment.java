package com.beini.ui.fragment.ani;


import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;

/**
 * Create  by beini  2017/1/16
 */
@ContentView(R.layout.fragment_beat)
public class BeatFragment extends BaseFragment {
    @ViewInject(R.id.image_beat)
    BeatImageView image_beat;

    @Override
    public void initView() {
        image_beat.startAnimation();
    }
}
