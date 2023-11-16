package com.example.firebasechatapp.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.firebasechatapp.PostDetailActivity
import com.example.firebasechatapp.R
import com.example.firebasechatapp.ViewHolders.PostViewHolder
import com.example.firebasechatapp.data.Post
import com.example.firebasechatapp.utils.AppConsts
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions


class PostRecyclerViewAdapter(options: FirebaseRecyclerOptions<Post>) :
    FirebaseRecyclerAdapter<Post, PostViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_post_view, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
        // Bind the data to the PostViewHolder
        holder.bindData(model)

        // Set button value or handle button clicks here
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, PostDetailActivity::class.java)
            intent.putExtra(AppConsts.POST_KEY_CONSTANT, getRef(position).key)
            holder.itemView.context.startActivity(intent)
        }

        // Access the button from the layout and set its value or handle clicks
//        holder.itemView.findViewById<Button>(R.id.your_button_id).apply {
//            text = "Your Button Text"  // Set your button value here
//            setOnClickListener {
//                // Handle button click here
//                // You can use 'position' or 'model' to get data associated with this item
//            }
//        }
    }
}