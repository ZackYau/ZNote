package com.example.zack.znote.helper;

/**
 * Created by Zack on 2016/12/28.
 */
public interface ItemTouchHelperViewHolder {
    /**
     * Called when the first registers an item as being moved or swiped.
     * Implementations should update the item view to indicate it's active state.
     */
    void onItemSelected();
    /**
     * Called when the has completed the move or swipe, and the active item state should be cleared.
     */
    void onItemClear();
}
