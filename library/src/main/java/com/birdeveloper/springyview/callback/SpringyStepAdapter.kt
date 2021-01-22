package com.birdeveloper.springyview.callback

import androidx.recyclerview.widget.RecyclerView.ViewHolder

interface SpringyStepAdapter<T: ViewHolder>
{
    fun onItemMoved(fromPosition: Int, toPosition: Int)
    fun onItemSwipedToStart(viewHolder: ViewHolder, positionOfItem: Int)
    fun onItemSwipedToEnd(viewHolder: ViewHolder, positionOfItem: Int)
    fun onItemSelected(viewHolder: ViewHolder?)
    fun onItemReleased(viewHolder: ViewHolder)
}