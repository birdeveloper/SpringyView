package com.birdeveloper.springyview.callback

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class SpringyStepCallBack(private val adapter: RecyclerView.Adapter<*>,
                          private var longPressDragEnabled: Boolean,
                          private var itemSwipeEnabled: Boolean) : ItemTouchHelper.Callback()
{


    override fun isLongPressDragEnabled(): Boolean
    {
        return longPressDragEnabled
    }

    override fun isItemViewSwipeEnabled(): Boolean
    {
        return itemSwipeEnabled
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int)
    {
        if (isItemViewSwipeEnabled && adapter is SpringyStepAdapter<*>)
        {
            if (i == ItemTouchHelper.START)
                adapter.onItemSwipedToStart(viewHolder, viewHolder.adapterPosition)
            else if (i == ItemTouchHelper.END)
                adapter.onItemSwipedToEnd(viewHolder, viewHolder.adapterPosition)
        }
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int
    {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean
    {
        return if (isLongPressDragEnabled && adapter is SpringyStepAdapter<*>)
        {
            adapter.onItemMoved(viewHolder.adapterPosition, target.adapterPosition)
            true
        }
        else
            false
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int)
    {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE && adapter is SpringyStepAdapter<*>)
        {
            adapter.onItemSelected(viewHolder)
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder)
    {
        super.clearView(recyclerView, viewHolder)
        if (adapter is SpringyStepAdapter<*>)
            adapter.onItemReleased(viewHolder)
    }

    fun setDragEnabled(enabled: Boolean)
    {
        longPressDragEnabled = enabled
    }

    fun setSwipeEnabled(enabled: Boolean)
    {
        itemSwipeEnabled = enabled
    }

}