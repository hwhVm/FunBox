package com.beini.ui.fragment.viewflippe;


import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.beini.R;
import com.beini.base.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;

/**
 * Create by beini 2017/6/1
 */
@ContentView(R.layout.fragment_view_flippe)
public class ViewFlippeFragment extends BaseFragment {
    @ViewInject(R.id.image1)
    ImageView image1;
    @ViewInject(R.id.image2)
    ImageView image2;
    @ViewInject(R.id.image3)
    ImageView image3;
    @ViewInject(R.id.view_flipper)
    ViewFlipper view_flipper;
    @Override
    public void initView() {
        view_flipper.setInAnimation(baseActivity, R.anim.pickerview_slide_in_bottom);
        view_flipper.setOutAnimation(baseActivity, R.anim.pickerview_slide_out_bottom);
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_flipper.showNext();
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_flipper.showNext();

            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_flipper.showNext();
            }
        });
    }


}
