package com.beini.ui.view.dialog;

/**
 * Created by beini on 2017/8/11.
 */

public interface OnChannelDragListener {
//    void onStarDrag(BaseViewHolder baseViewHolder);
    void onItemMove(int starPos, int endPos);
    void onMoveToMyChannel(int starPos, int endPos);
    void onMoveToOtherChannel(int starPos, int endPos);
}
