package com.beini.ui.fragment.screenrecord;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.beini.R;
import com.beini.adapter.BaseAdapter;
import com.beini.bean.BaseBean;

import java.io.File;
import java.util.List;

/**
 * Created by beini on 2017/3/28.
 */

public class VideoAdapter extends BaseAdapter {
    private List<VideoBean> videoBeens;
    private  ScreenRecordFragment mainActivity;

    public VideoAdapter(BaseBean<VideoBean> baseLit, ScreenRecordFragment mainActivity) {
        super(baseLit);
        this.videoBeens = baseLit.getBaseList();
        this.mainActivity=mainActivity;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setTag(position);
        final File file = new File(videoBeens.get(position).getUrl());
        if (file.exists()) {
            getImageView((ViewHolder) holder, R.id.image_video).setImageBitmap(Utils.createVideoThumbnail(videoBeens.get(position).getUrl()));
            getButton((ViewHolder) holder, R.id.btn_detele).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (file.exists()) {
                        boolean mBoolean = file.delete();
                        Log.d("com.beini","  detelt  mBoolean="+mBoolean);
                        mainActivity.updata();
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }
}
