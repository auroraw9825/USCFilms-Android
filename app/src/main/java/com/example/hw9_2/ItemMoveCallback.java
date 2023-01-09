package com.example.hw9_2;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemMoveCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter adapter;

    public ItemMoveCallback(ItemTouchHelperAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public  boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public  boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public  void onSwiped(RecyclerView.ViewHolder holder, int i) {

    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder holder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END; //  | ItemTouchHelper.START | ItemTouchHelper.END
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder holder,RecyclerView.ViewHolder target) {
        adapter.onRowMoved(holder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder holder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (holder instanceof WatchlistAdapter.WatchlistViewHolder) {
                WatchlistAdapter.WatchlistViewHolder myViewHolder =(WatchlistAdapter.WatchlistViewHolder) holder;
                adapter.onRowSelected(myViewHolder);
            }
        }
        super.onSelectedChanged(holder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView,RecyclerView.ViewHolder holder) {
        super.clearView(recyclerView,holder);
        if (holder instanceof WatchlistAdapter.WatchlistViewHolder) {
            WatchlistAdapter.WatchlistViewHolder myViewHolder =(WatchlistAdapter.WatchlistViewHolder) holder;
            adapter.onRowClear(myViewHolder);
        }
    }

    public interface ItemTouchHelperAdapter {
        void onRowMoved(int fromPosition, int toPosition);
        void onRowSelected(WatchlistAdapter.WatchlistViewHolder myViewHolder);
        void onRowClear(WatchlistAdapter.WatchlistViewHolder myViewHolder);
    }
}

