package com.beini.adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;

import com.beini.R;
import com.beini.bean.BaseBean;

import java.util.List;

/**
 * Created by beini on 2017/7/25.
 */

public class SnapPicAdapter extends BaseAdapter{
    List<Drawable> drawables;

    public SnapPicAdapter(BaseBean<Drawable> baseBean) {
        super(baseBean);
        this.drawables = baseBean.getBaseList();
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        getImageView((ViewHolder) holder, R.id.image_snap_pic).setBackground(drawables.get(position));
    }
}
