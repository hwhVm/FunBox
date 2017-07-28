package com.beini.ui.fragment.multimedia.video.adapter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.beini.R;
import com.beini.adapter.BaseAdapter;
import com.beini.bean.BaseBean;

import java.util.List;

/**
 * Created by beini on 2017/7/28.
 */

public class VideoListAdapter extends BaseAdapter {

    List<Bitmap> bitmaps;

    public VideoListAdapter(@NonNull BaseBean<Bitmap> baseBean) {
        super(baseBean);
        bitmaps = baseBean.getBaseList();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        getImageView((ViewHolder)holder,R.id.image_video_index).setImageBitmap(bitmaps.get(position));
    }
}
