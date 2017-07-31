package com.beini.ui.fragment.multimedia.recording;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.beini.R;
import com.beini.adapter.BaseAdapter;
import com.beini.bean.BaseBean;
import com.beini.ui.fragment.multimedia.bean.RecorderBean;

import java.util.List;

/**
 * Created by beini on 2017/7/27.
 */

public class RecorderAdapter extends BaseAdapter {

    private List<RecorderBean> recorderBeans;

    public RecorderAdapter(@NonNull BaseBean<RecorderBean> baseBean) {
        super(baseBean);
        recorderBeans = baseBean.getBaseList();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        getTextView((ViewHolder) holder, R.id.text_recorder_name).setText(recorderBeans.get(position).getFilePath());
    }
}
