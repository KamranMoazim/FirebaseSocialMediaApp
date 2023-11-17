package com.example.firebasechatapp.ViewHolders

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.R

class ChatMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var leftChatLayout:LinearLayout
    var rightChatLayout:LinearLayout
    var leftChatText:TextView
    var rightChatText:TextView

    init {
        // Initialize your views here
        leftChatLayout = itemView.findViewById(R.id.left_chat_layout)
        rightChatLayout = itemView.findViewById(R.id.right_chat_layout)
        leftChatText = itemView.findViewById(R.id.left_chat_layout_text)
        rightChatText = itemView.findViewById(R.id.right_chat_layout_text)
    }

}