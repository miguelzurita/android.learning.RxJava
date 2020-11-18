package com.jonbott.learningrxjava.Activities.ReactiveUi.Simple

import android.annotation.SuppressLint
import android.content.Context
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.jonbott.learningrxjava.ModelLayer.Entities.Friend
import com.jonbott.learningrxjava.R
import com.jonbott.learningrxjava.databinding.ListItemFriendBinding
import kotlinx.android.synthetic.main.list_item_friend.view.*

class SimpleAdapter(
        private val context: Context,
        private val dataSource: List<Friend>
) : BaseAdapter() {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        val rowView = inflater.inflate(R.layout.list_item_friend, parent, false)
//        tv_friend

        val holder: FriendViewHolder
        if (convertView == null) {
            val binding: ListItemFriendBinding = DataBindingUtil.inflate(LayoutInflater.from(parent?.context), R.layout.list_item_friend, parent, false)
            holder = FriendViewHolder(binding, dataSource[position])
        } else {
            holder = convertView.tag as FriendViewHolder
        }

        return holder.view
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return dataSource.size
    }

    private class FriendViewHolder(var binding: ListItemFriendBinding, friend: Friend) {
        var view: View = binding.root

        init {
            view.tag = this
            view.tv_friend_first_name.text = friend.firstName
        }
    }

}