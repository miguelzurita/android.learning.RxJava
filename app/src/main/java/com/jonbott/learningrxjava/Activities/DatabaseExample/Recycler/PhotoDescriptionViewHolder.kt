package com.jonbott.learningrxjava.Activities.DatabaseExample.Recycler

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jonbott.learningrxjava.ModelLayer.PersistenceLayer.PhotoDescription
import com.jonbott.learningrxjava.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class PhotoDescriptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val context = itemView.context
    val cardView: CardView
    val photoDescriptionImageView: ImageView
    val titleTextView: TextView
    val tv_created: TextView
    val photoDescriptionInfoTextView: TextView

    init {
        cardView = itemView.findViewById(R.id.photoDescriptionCardView)
        photoDescriptionImageView = itemView.findViewById(R.id.photoDescriptionImageView)
        titleTextView = itemView.findViewById(R.id.photoDescriptionTitleTextView)
        tv_created = itemView.findViewById(R.id.tv_created)
        photoDescriptionInfoTextView = itemView.findViewById(R.id.photoDescriptionInfoTextView)
    }

    fun configureWith(photoDescription: PhotoDescription) {

        photoDescription.createdAt?.let {
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
            val dateString: String = simpleDateFormat.format(photoDescription.createdAt)
            tv_created.setText(String.format("Date: %s", dateString))
        }


        titleTextView.text = photoDescription.title
        photoDescriptionInfoTextView.text = photoDescription.url

        Picasso.get()
                .load(photoDescription.thumbnailUrl)
                .resize(100, 100)
                .centerCrop()
                .into(photoDescriptionImageView)

    }
}