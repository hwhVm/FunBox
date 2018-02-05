package com.beini.ui.fragment.customview;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.View;
import android.widget.ImageView;

import com.beini.R;
import com.beini.app.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.bind.ViewInject;
import com.beini.ui.view.CropImageView;

/**
 * Create by beini 2018/2/5
 */
@ContentView(R.layout.fragment_cut_view)
public class CutViewFragment extends BaseFragment {
    @ViewInject(R.id.v_shade)
    private View v_shade;
    @ViewInject(R.id.iv_manual_over)
    private CropImageView iv_manual_over;
    @ViewInject(R.id.iv_manual_old)
    private ImageView iv_manual_old;
    @ViewInject(R.id.iv_manual_new)
    private ImageView iv_manual_new;
    private Bitmap mNewBitmap = null;

    @Override
    public void initView() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.a);
        iv_manual_old.setImageBitmap(bitmap);

        baseActivity.setTopBar(View.GONE);
        baseActivity.setBottom(View.GONE);
        iv_manual_old.setDrawingCacheEnabled(true);
    }

    @Event({R.id.btn_cut_manual_begin, R.id.btn_cut_manual_end})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_cut_manual_begin:
                v_shade.setVisibility(View.VISIBLE);
                iv_manual_over.setVisibility(View.VISIBLE);
                iv_manual_over.setOrigBitmap(iv_manual_old.getDrawingCache());
                iv_manual_over.setBitmapRect(new Rect(200, 200, 200, 200));
                break;
            case R.id.btn_cut_manual_end:
                v_shade.setVisibility(View.GONE);
                iv_manual_over.setVisibility(View.GONE);
                mNewBitmap = iv_manual_over.getCropBitmap();
                iv_manual_new.setImageBitmap(mNewBitmap);
                break;
        }
    }
}
