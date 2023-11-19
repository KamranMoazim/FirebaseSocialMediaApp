package com.example.firebasechatapp.ViewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.R
import com.example.firebasechatapp.data.Post
import com.example.firebasechatapp.utils.MyUtils
import com.squareup.picasso.Picasso

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var nameTextView:TextView
    private lateinit var postDescriptionTextView:TextView
    private lateinit var uriTextView:ImageView
//    private lateinit var view:View

    fun bindData(post: Post) {
        // Bind the data from the 'person' parameter to your views
        // For example:
        nameTextView = itemView.findViewById(R.id.name_single_post_view)
        postDescriptionTextView = itemView.findViewById(R.id.description_single_post_view)
        uriTextView = itemView.findViewById(R.id.image_single_post_view)

        nameTextView.text = MyUtils.getSubString(post.PostName, 20) + "..."
        postDescriptionTextView.text = MyUtils.getSubString(post.PostDescription, 70) + "..."
//        uriTextView.setImageURI(car.ImageUri)
        Picasso.get().load(post.ImageUri).into(uriTextView)
    }
}