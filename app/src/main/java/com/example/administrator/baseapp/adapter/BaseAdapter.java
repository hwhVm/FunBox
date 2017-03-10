package com.example.administrator.baseapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.baseapp.bean.BaseBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;


/**
 * Created by beini on 2017/2/18.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private List<T> baseLit;
    private int layoutId;
    private final int i = 0;

    public BaseAdapter(List<T> baseLit) {
        this.baseLit = baseLit;
    }

    public BaseAdapter(BaseBean baseBean) {
        this.baseLit = baseBean.getBaseList();
        this.layoutId = baseBean.getId();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return holder;
    }

    @Override
    public int getItemCount() {
        if (baseLit == null || baseLit.size() == 0) {
            return 0;
        }
        return baseLit.size();
    }

    private View view;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);
            setView(view);
        }
    }

    private void setView(View view) {
        this.view = view;
    }

    private View getView() {
        return this.view;
    }

    public TextView getTextView(int viewId) {
        return (TextView) getView().findViewById(viewId);
    }

    public ImageView getImageView(int viewId) {
        return (ImageView) getView().findViewById(viewId);
    }

    public SimpleDraweeView getSimpleDraweeView(int viewId) {
        return (SimpleDraweeView) getView().findViewById(viewId);
    }

    //item  click 事件
    OnItemClickListener itemClickListener = null;

    public void setItemClick(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        if (itemClickListener != null)
            itemClickListener.onItemClick(view, (int) view.getTag());
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    // item onlongClick 事件
    onItemLongClickListener itemLongClickListener = null;

    public void setOnItemLongClickListener(onItemLongClickListener onItemLongClickListener) {
        this.itemLongClickListener = onItemLongClickListener;
    }

    @Override
    public boolean onLongClick(View v) {
        if (itemLongClickListener != null)
            itemLongClickListener.onItemLongClick(v, (int) v.getTag());
        return true;
    }

    public interface onItemLongClickListener {
        void onItemLongClick(View view, int position);
    }
}
