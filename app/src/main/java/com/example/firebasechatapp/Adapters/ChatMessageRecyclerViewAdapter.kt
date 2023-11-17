package com.example.firebasechatapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.firebasechatapp.R
import com.example.firebasechatapp.ViewHolders.ChatMessageViewHolder
import com.example.firebasechatapp.ViewHolders.FriendsViewHolder
import com.example.firebasechatapp.ViewHolders.PostViewHolder
import com.example.firebasechatapp.data.ChatMessage
import com.example.firebasechatapp.data.Post
import com.example.firebasechatapp.utils.SharedPreferencesHelper
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

//class ChatMessageRecyclerViewAdapter(options: FirebaseRecyclerOptions<ChatMessage>) :
//    FirebaseRecyclerAdapter<ChatMessage, ChatMessageViewHolder>(options) {


class ChatMessageRecyclerViewAdapter(options: FirestoreRecyclerOptions<ChatMessage>) :
    FirestoreRecyclerAdapter<ChatMessage, ChatMessageViewHolder>(options) {

    private lateinit var context: Context

    constructor(options: FirestoreRecyclerOptions<ChatMessage>, ctx:Context) : this(options) {
        this.context = ctx
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_message_view, parent, false)
        return ChatMessageViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ChatMessageViewHolder,
        position: Int,
        model: ChatMessage,
    ) {

        var sharedPreferencesHelper = SharedPreferencesHelper(this.context)
        var savedCredentials = sharedPreferencesHelper.getSavedCredentials()

        if (model.SenderID.equals(savedCredentials.third!!.UserId)){
            holder.leftChatLayout.visibility = View.GONE
            holder.rightChatLayout.visibility = View.VISIBLE
            holder.rightChatText.text = model.Message
        } else {
            holder.rightChatLayout.visibility = View.GONE
            holder.leftChatLayout.visibility = View.VISIBLE
            holder.leftChatText.text = model.Message
        }

    }
}