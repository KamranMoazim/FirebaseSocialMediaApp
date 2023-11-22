package com.example.firebasechatapp.ViewHolders

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.R

class GroupChatMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var leftChatLayout: LinearLayout
    var rightChatLayout: LinearLayout
    var rightChatUser: TextView
    var leftChatUser: TextView
    var leftChatText: TextView
    var rightChatText: TextView

    init {
        // Initialize your views here
        leftChatLayout = itemView.findViewById(R.id.left_chat_layout)
        leftChatUser = itemView.findViewById(R.id.left_chat_user)
        leftChatText = itemView.findViewById(R.id.left_chat_text)

        rightChatLayout = itemView.findViewById(R.id.right_chat_layout)
        rightChatUser = itemView.findViewById(R.id.right_chat_user)
        rightChatText = itemView.findViewById(R.id.right_chat_text)
    }

}