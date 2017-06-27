package com.beini.ui.fragment.picPicker;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.beini.R;
import com.beini.base.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.bind.ViewInject;
import com.beini.util.BLog;
import com.beini.util.listener.ActivityResultListener;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.filter.Filter;

import java.util.List;

import static android.app.Activity.RESULT_OK;

@ContentView(R.layout.fragment_pic_picke)
public class PicPickeFragment extends BaseFragment implements ActivityResultListener {
    @ViewInject(R.id.btn_show_pic)
    Button button;

    private int REQUEST_GALLERY = 111;

    @Override
    public void initView() {
        baseActivity.setActivityResultListener(this);
    }


    @Event({R.id.btn_show_pic, R.id.btn_matisse, R.id.btn_dracula})
    private void mEVent(View view) {
        switch (view.getId()) {
            case R.id.btn_show_pic:
                BLog.d("    -------------->PicPickeFragment   ");
                Intent toGallery = new Intent(Intent.ACTION_GET_CONTENT);
                toGallery.setType("image/*");
                toGallery.addCategory(Intent.CATEGORY_OPENABLE);
                baseActivity.startActivityForResult(toGallery, REQUEST_GALLERY);
                break;
            case R.id.btn_matisse:
                Matisse.from(getActivity())
                        .choose(MimeType.allOf())
                        .countable(true)
                        .maxSelectable(9)
                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(REQUEST_GALLERY);
                break;
            case R.id.btn_dracula://要导入Picasso，就不导入了
                Matisse.from(getActivity())
                        .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                        .theme(R.style.Matisse_Dracula)
                        .countable(false)
                        .maxSelectable(9)
                        .imageEngine(new PicassoEngine())
                        .forResult(REQUEST_GALLERY);
                break;
        }
    }

    List<Uri> mSelected;

    @Override
    public void resultCallback(int requestCode, int resultCode, Intent data) {
        BLog.d("   requestCode=" + requestCode + "    resultCode=" + resultCode + "  ");
        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            BLog.d("mSelected: " + mSelected);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        baseActivity.setActivityResultListener(null);
    }
}
