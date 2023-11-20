package com.example.firebasechatapp.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import com.example.firebasechatapp.PostDetailActivity
import com.example.firebasechatapp.R
import com.example.firebasechatapp.ViewHolders.PostViewHolder
import com.example.firebasechatapp.data.Post
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.repositories.PostRepository
import com.example.firebasechatapp.utils.AppConsts
import com.example.firebasechatapp.utils.MyUtils
import com.example.firebasechatapp.utils.SharedPreferencesHelper
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class PostRecyclerViewAdapter(options: FirestoreRecyclerOptions<Post>) :
    FirestoreRecyclerAdapter<Post, PostViewHolder>(options) {


    private lateinit var postRepository: PostRepository

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var savedCredentials:Triple<String?, String?, User?>



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_post_view, parent, false)

        postRepository = PostRepository(parent.context)
        sharedPreferencesHelper = SharedPreferencesHelper(parent.context)
        savedCredentials = sharedPreferencesHelper.getSavedCredentials()



        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
        // Bind the data to the PostViewHolder
        holder.bindData(model)

//        Log.d("onBindViewHolder", model.PostName)

        // Set button value or handle button clicks here
        holder.itemView.findViewById<ImageButton>(R.id.post_comment_btn).setOnClickListener {
            val intent = Intent(holder.itemView.context, PostDetailActivity::class.java)
            MyUtils.passPostAsIntent(intent, model)
            holder.itemView.context.startActivity(intent)
        }

        // Set button value or handle button clicks here
        holder.itemView.findViewById<ImageButton>(R.id.post_like_btn).setOnClickListener {
//            onItemDoubleClicked(holder.itemView.context, model)
            var currentUserId = savedCredentials.third!!.UserId

            if (model.PeopleLikedPost.contains(currentUserId)){
                postRepository.removeLike(model.PostID, currentUserId){
                        success, message ->
                    run {
                        if (success){
                            // Update local data: Remove current user from PeopleLikedPost list
                            model.PeopleLikedPost = model.PeopleLikedPost.filter { it != currentUserId }
                            notifyDataSetChanged() // Notify adapter about the data change
                        }
                        myToast(holder.itemView.context, message)
                        holder.updateColor(false)

                    }
                }
            } else {
                postRepository.likePost(model.PostID, currentUserId){
                        success, message ->
                    run {
                        if (success){
                            // Update local data: Add current user to PeopleLikedPost list
                            model.PeopleLikedPost = model.PeopleLikedPost + currentUserId
                            notifyDataSetChanged() // Notify adapter about the data change
                        }
                        myToast(holder.itemView.context, message)
                        holder.updateColor(true)
                    }
                }
            }
        }


    }


    private fun myToast(context:Context, msg:String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

}