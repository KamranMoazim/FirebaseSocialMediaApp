package com.example.firebasechatapp.ViewHolders

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.R
import com.example.firebasechatapp.data.ChatRoom
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.utils.MyUtils

class MyChatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var usernameTextView: TextView
    var lastMessageTextView: TextView
    var lastMessageTimeTextView: TextView
    var profileTextView: TextView

    init {
        // Initialize your views here
        usernameTextView = itemView.findViewById(R.id.username_text)
        lastMessageTextView = itemView.findViewById(R.id.last_text_message)
        lastMessageTimeTextView = itemView.findViewById(R.id.last_text_message_time)
        profileTextView = itemView.findViewById(R.id.single_friend_imageText)
    }


}