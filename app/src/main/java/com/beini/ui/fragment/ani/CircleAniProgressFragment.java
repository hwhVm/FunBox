package com.beini.ui.fragment.ani;


import android.view.View;
import android.widget.LinearLayout;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.bind.ViewInject;
import com.beini.ui.fragment.ani.view.CicleAnimation;

/**
 * Create by beini 2018/2/2
 */
@ContentView(R.layout.fragment_circle_ani_progress)
public class CircleAniProgressFragment extends BaseFragment {
    CicleAnimation cicle_animation;
    @ViewInject(R.id.ll_cicle_animation)
    LinearLayout ll_cicle_animation;

    @Override
    public void initView() {

    }

    @Event(R.id.btn_start_ani)
    private void mEvent(View view) {
        cicle_animation = new CicleAnimation(getActivity());
        ll_cicle_animation.addView(cicle_animation);
        cicle_animation.render();
    }

}
