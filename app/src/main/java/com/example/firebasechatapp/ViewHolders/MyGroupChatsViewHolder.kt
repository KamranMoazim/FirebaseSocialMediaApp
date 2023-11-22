package com.example.firebasechatapp.ViewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.R

class MyGroupChatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var groupNameTextView: TextView
    var lastMessageTextView: TextView
    var lastMessageTimeTextView: TextView
    var profileTextView: TextView

    init {
        // Initialize your views here
        groupNameTextView = itemView.findViewById(R.id.username_text)
        lastMessageTextView = itemView.findViewById(R.id.last_text_message)
        lastMessageTimeTextView = itemView.findViewById(R.id.last_text_message_time)
        profileTextView = itemView.findViewById(R.id.single_friend_imageText)
    }

}