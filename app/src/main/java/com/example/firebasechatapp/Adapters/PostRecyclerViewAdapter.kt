package com.example.firebasechatapp.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.firebasechatapp.PostDetailActivity
import com.example.firebasechatapp.R
import com.example.firebasechatapp.ViewHolders.ChatMessageViewHolder
import com.example.firebasechatapp.ViewHolders.PostViewHolder
import com.example.firebasechatapp.data.ChatMessage
import com.example.firebasechatapp.data.Post
import com.example.firebasechatapp.utils.AppConsts
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class PostRecyclerViewAdapter(options: FirestoreRecyclerOptions<Post>) :
    FirestoreRecyclerAdapter<Post, PostViewHolder>(options) {

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
            val postId = snapshots.getSnapshot(position).id
            val intent = Intent(holder.itemView.context, PostDetailActivity::class.java)
            intent.putExtra(AppConsts.POST_KEY_CONSTANT, postId)
            holder.itemView.context.startActivity(intent)
        }

    }
}

//class PostRecyclerViewAdapter(options: FirebaseRecyclerOptions<Post>) :
//    FirebaseRecyclerAdapter<Post, PostViewHolder>(options) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.single_post_view, parent, false)
//        return PostViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
//        // Bind the data to the PostViewHolder
//        holder.bindData(model)
//
//        // Set button value or handle button clicks here
//        holder.itemView.setOnClickListener {
//            val intent = Intent(holder.itemView.context, PostDetailActivity::class.java)
//            intent.putExtra(AppConsts.POST_KEY_CONSTANT, getRef(position).key)
//            holder.itemView.context.startActivity(intent)
//        }
//
//    }
//}