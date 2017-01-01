package com.example.zack.znote.helper;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Zack on 2016/12/22.
 */
public interface OnStartDragListener {
    /**
     * 需要拖动时候调用.
     * @param viewHolder The holder of the view to drag.
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}
