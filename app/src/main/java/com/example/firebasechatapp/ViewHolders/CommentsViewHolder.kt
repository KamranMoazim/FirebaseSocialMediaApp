package com.example.firebasechatapp.ViewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.R
import com.example.firebasechatapp.data.Comment
import com.example.firebasechatapp.utils.MyUtils

class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {

    private lateinit var imageTextView: TextView
    private lateinit var commenterUsername: TextView
    private lateinit var commentTime: TextView
    private lateinit var commentText: TextView
    // private lateinit var messageFriendBtn: ImageButton
    // private lateinit var removeFriendBtn: ImageButton

    fun bindData(comment: Comment) {

        imageTextView = itemView.findViewById(R.id.commenter_imageText)
        commenterUsername = itemView.findViewById(R.id.commenter_username)
        commentTime = itemView.findViewById(R.id.comment_time)
        commentText = itemView.findViewById(R.id.comment_text)
        // messageFriendBtn = itemView.findViewById(R.id.message_friend_btn)
        // removeFriendBtn = itemView.findViewById(R.id.remove_friend_btn)

        imageTextView.text = MyUtils.getInitials(comment.CommenterImageUri) // here we have to use image uri
        commenterUsername.text = MyUtils.getSubString(comment.Commenter, 12) + "..."
        commentTime.text = MyUtils.timeStampToString(comment.CommentedAt!!)
        commentText.text = comment.Comment

    }




}