package com.beini.adapter;

import android.support.v7.widget.RecyclerView;

import com.beini.R;
import com.beini.bean.BaseBean;

import java.util.List;

/**
 * Created by beini on 2017/2/18.
 */

public class HomeListAdapter extends BaseAdapter {
    private List<String> strings;

    public HomeListAdapter(BaseBean<String> baseBean) {
        super(baseBean);
        this.strings = baseBean.getBaseList();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        getTextView((ViewHolder) holder, R.id.text_home_list).setText(strings.get(position));
    }
}
