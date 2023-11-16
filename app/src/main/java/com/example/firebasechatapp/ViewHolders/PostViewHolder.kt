package com.example.firebasechatapp.ViewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.R
import com.example.firebasechatapp.data.Post
import com.squareup.picasso.Picasso

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var nameTextView:TextView
    private lateinit var uriTextView:ImageView
//    private lateinit var view:View

    fun bindData(post: Post) {
        // Bind the data from the 'person' parameter to your views
        // For example:
        nameTextView = itemView.findViewById<TextView>(R.id.name_single_post_view)
        uriTextView = itemView.findViewById<ImageView>(R.id.image_single_post_view)

        nameTextView.text = post.PostName
//        uriTextView.setImageURI(car.ImageUri)
        Picasso.get().load(post.ImageUri).into(uriTextView)
    }
}