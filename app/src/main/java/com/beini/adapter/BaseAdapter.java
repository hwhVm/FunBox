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

    private List<T> baseLit;
    private int layoutId;

    public BaseAdapter(@NonNull BaseBean<T> baseBean) {
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



    public class ViewHolder extends RecyclerView.ViewHolder {
         View view;

        public ViewHolder(View view) {
            super(view);
           this.view=view;
        }
    }

    public TextView getTextView(@NonNull ViewHolder viewHolder,@IdRes int viewId) {
        return (TextView)   viewHolder.view.findViewById(viewId);
    }

    public ImageView getImageView(@NonNull ViewHolder viewHolder,@IdRes int viewId) {
        return (ImageView) viewHolder.view.findViewById(viewId);
    }
    public Button getButton(@NonNull ViewHolder viewHolder, @IdRes int viewId) {
        return (Button) viewHolder.view.findViewById(viewId);
    }

    public SimpleDraweeView getSimpleDraweeView(@NonNull ViewHolder viewHolder,@IdRes int viewId) {
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
    private  onItemLongClickListener itemLongClickListener = null;

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
