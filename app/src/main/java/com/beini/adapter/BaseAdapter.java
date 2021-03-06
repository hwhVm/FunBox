package com.beini.adapter;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.beini.bean.BaseBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;


/**
 * Created by beini on 2017/2/18.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private List<T> baseList;
    private int layoutId;

    public BaseAdapter(@NonNull BaseBean<T> baseBean) {
        this.baseList = baseBean.getBaseList();
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
        if (baseList == null || baseList.size() == 0) {
            return 0;
        }
        return baseList.size();
    }


    protected class ViewHolder extends RecyclerView.ViewHolder {
        View view;

        ViewHolder(View view) {
            super(view);
            this.view = view;
        }
    }

    public void addItem(T bean, int postion) {
        baseList.add(postion, bean);
        notifyItemInserted(postion);
        notifyItemRangeChanged(postion, baseList.size());
    }

    public void removeItem(int postion) {
        baseList.remove(postion);
        notifyItemRemoved(postion);
        notifyItemRangeChanged(postion, baseList.size());
    }

    protected TextView getTextView(@NonNull ViewHolder viewHolder, @IdRes int viewId) {
        return (TextView) viewHolder.view.findViewById(viewId);
    }

    protected ImageView getImageView(@NonNull ViewHolder viewHolder, @IdRes int viewId) {
        return (ImageView) viewHolder.view.findViewById(viewId);
    }

    protected Button getButton(@NonNull ViewHolder viewHolder, @IdRes int viewId) {
        return (Button) viewHolder.view.findViewById(viewId);
    }

    protected SimpleDraweeView getSimpleDraweeView(@NonNull ViewHolder viewHolder, @IdRes int viewId) {
        return (SimpleDraweeView) viewHolder.view.findViewById(viewId);
    }

    //item  click 事件
    private OnItemClickListener itemClickListener = null;

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
    private onItemLongClickListener itemLongClickListener = null;

    public void setOnItemLongClickListener(onItemLongClickListener onItemLongClickListener) {
        this.itemLongClickListener = onItemLongClickListener;
    }

    @Override
    public boolean onLongClick(View v) {
        if (itemLongClickListener != null)
            itemLongClickListener.onItemLongClick(v, (int) v.getTag());
        return true;
    }

    private interface onItemLongClickListener {
        void onItemLongClick(View view, int position);
    }
}
