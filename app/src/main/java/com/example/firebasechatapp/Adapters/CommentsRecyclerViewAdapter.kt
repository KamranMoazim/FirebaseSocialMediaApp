package com.example.firebasechatapp.Adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.R
import com.example.firebasechatapp.ViewHolders.AddFriendsViewHolder
import com.example.firebasechatapp.ViewHolders.CommentsViewHolder
import com.example.firebasechatapp.data.Comment
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.repositories.PostRepository
import com.example.firebasechatapp.utils.SharedPreferencesHelper

class CommentsRecyclerViewAdapter (ctx: Context): RecyclerView.Adapter<CommentsViewHolder>()  {

    private var commentsList = ArrayList<Comment>()
    private var sharedPreferencesHelper: SharedPreferencesHelper
    private var savedCredentials:Triple<String?, String?, User?>

    private var postRepository: PostRepository
    private lateinit var postId: String


    init {
        sharedPreferencesHelper = SharedPreferencesHelper(ctx)
        savedCredentials = sharedPreferencesHelper.getSavedCredentials()
        postRepository = PostRepository(ctx)
    }

    fun updateCommentsList(commentList : List<Comment>, receivingPostId:String){
        postId = receivingPostId
        this.commentsList.clear()
        this.commentsList.addAll(commentList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_comment_view, parent, false)
        return CommentsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return commentsList.size
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {

        var currentComment = commentsList[position]

        holder.bindData(currentComment)

        // Set a long click listener for the item view
        holder.itemView.setOnLongClickListener {
            // Show a confirmation dialog

            if (currentComment.CommenterId == savedCredentials.third!!.UserId){
                showDeleteConfirmationDialog(holder, position)
            } else {
                myToast(holder.itemView.context,"You cannot delete this comment")
            }

            // Return true to indicate that the long press event is consumed
            true
        }
    }





    fun showDeleteConfirmationDialog(holder: CommentsViewHolder, position: Int) {
        AlertDialog.Builder(holder.itemView.context)
            .setTitle("Delete Comment")
            .setMessage("Are you sure you want to delete this comment?")
            .setPositiveButton("Delete") { _, _ ->

                var currentComment = commentsList[position]

                postRepository.deleteComment(postId, currentComment.CommentId) {
                        success, message ->
                    run {
                        if (success) {
                            commentsList.removeAt(position)
                            notifyItemRemoved(position)
                            myToast(holder.itemView.context, message)
                        }
//                        callback(message)
                    }
                }

//                commentsList.removeAt(position)
//                notifyItemRemoved(position)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }



    private fun myToast(ctx:Context, msg:String) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
    }
}