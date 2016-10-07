package com.yujie.heromanager.adapter;

/**
 * Created by yujie on 16-10-7.
 */

public interface OnMoveAndSwipListener {
    boolean onItemMove(int fromPosition , int toPosition);
    void onItemDismiss(int position);
}
