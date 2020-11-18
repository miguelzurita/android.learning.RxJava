package com.jonbott.learningrxjava.Activities.ReactiveUi.Simple

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
        private val presenter: SimpleUIPresenter
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: FriendViewHolder
        val friend = presenter.friends[position]
        if (convertView == null) {
            holder = FriendViewHolder(getBinding(parent), friend)
        } else {
            holder = convertView.tag as FriendViewHolder
            holder.setData(friend)
        }
        holder.view.setOnClickListener {
            presenter.onItemClick(friend)
        }

        return holder.view
    }

    private fun getBinding(parent: ViewGroup?): ListItemFriendBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context)
                    , R.layout.list_item_friend, parent
                    , false)

    override fun getItem(position: Int): Any = presenter.friends[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount() = presenter.friends.size

    private class FriendViewHolder(binding: ListItemFriendBinding, friend: Friend) {
        var view: View = binding.root

        init {
            view.tag = this
            setData(friend)
        }

        fun setData(friend: Friend) {
            view.tv_friend_first_name.text = friend.firstName
            view.tv_friend_last_name.text = friend.lastName
        }
    }

}