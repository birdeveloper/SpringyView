package com.birdeveloper.springyview.sample.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.birdeveloper.springyview.SpringyRecyclerView
import com.birdeveloper.springyview.sample.R
import com.birdeveloper.springyview.sample.model.remote.User
import com.birdeveloper.springyview.sample.util.AppExtention.setImageDrawableWithAnimation
import java.util.*

class UserAdapter (val userList: MutableList<User?> = mutableListOf()): SpringyRecyclerView.Adapter<UserAdapter.UserViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.UserViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_user, parent, false))
    }

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(userList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemSwipedToStart(viewHolder: RecyclerView.ViewHolder, positionOfItem: Int) {
        Log.e(this.javaClass.simpleName,"$positionOfItem swiped to start")
        notifyItemChanged(positionOfItem)
    }

    override fun onItemSwipedToEnd(viewHolder: RecyclerView.ViewHolder, positionOfItem: Int) {
        Log.e(this.javaClass.simpleName,"$positionOfItem swiped to end")
        notifyItemChanged(positionOfItem)
    }

    override fun onItemSelected(viewHolder: RecyclerView.ViewHolder?) {
        viewHolder?.itemView?.animate()?.alpha(0.5f)?.setDuration(300)?.start()
    }

    override fun onItemReleased(viewHolder: RecyclerView.ViewHolder) {
        viewHolder.itemView.animate()?.alpha(1f)?.setDuration(300)?.start()
    }

    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(user: User?){
            itemView.findViewById<ImageView>(R.id.imgProfilePhoto).setImageDrawableWithAnimation(user?.avatar.toString(),300)
            itemView.findViewById<TextView>(R.id.txtFullName).text = user?.first_name+" "+user?.last_name
            itemView.findViewById<TextView>(R.id.txtMailAdress).text = user?.email
        }
    }
}