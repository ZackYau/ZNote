package com.example.zack.znote.helper;

/**
 * Created by Zack on 2016/12/22.
 */
public interface ItemTouchHelperAdapter {

    /**
     * 拖动item
     * @param fromPosition The start position of the moved item.
     * @param toPosition Then resolved position of the moved item.
     * @return True if the item was moved to the new adapter position.
     */
    boolean onItemMove(int fromPosition, int toPosition);

    /**
     * 滑动删除item
     * @param position The position of the item dismissed.
     */
    //void onItemDismiss(int position);
}
