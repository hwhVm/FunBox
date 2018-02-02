package com.beini.ui.fragment.ani;


import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.bind.ViewInject;

/**
 * Create by beini 2018/2/2
 * 淡入淡出动画
 */
@ContentView(R.layout.fragment_fade)
public class FadeFragment extends BaseFragment {
    @ViewInject(R.id.image_fade)
    ImageView image_fade;
    private AlphaAnimation alphaAnimation;
    
    @Override
    public void initView() {
        baseActivity.setTopBar(View.GONE);
        baseActivity.setBottom(View.GONE);
        alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(3000);
        image_fade.setAnimation(alphaAnimation);
    }

    @Event({R.id.btn_click_alpha_animation})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_click_alpha_animation:
                alphaAnimation.start();
                break;
        }
    }

}
