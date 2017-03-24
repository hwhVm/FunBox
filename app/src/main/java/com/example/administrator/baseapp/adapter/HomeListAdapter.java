package com.example.administrator.baseapp.adapter;


import android.support.v7.widget.RecyclerView;

import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.bean.BaseBean;
import com.example.administrator.baseapp.utils.BLog;

import java.util.List;

/**
 * Created by beini on 2017/2/18.
 */

public class HomeListAdapter extends BaseAdapter {
    List<String> strings;

    public HomeListAdapter(BaseBean<String> baseBean) {
        super(baseBean);
        this.strings = baseBean.getBaseList();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        getTextView((ViewHolder) holder,R.id.text_home_list).setText(strings.get(position));
    }
}
