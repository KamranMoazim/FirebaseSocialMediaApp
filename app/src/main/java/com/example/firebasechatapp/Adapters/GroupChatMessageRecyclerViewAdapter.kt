package com.example.firebasechatapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.firebasechatapp.R
import com.example.firebasechatapp.ViewHolders.GroupChatMessageViewHolder
import com.example.firebasechatapp.data.ChatMessage
import com.example.firebasechatapp.data.GroupChatMessage
import com.example.firebasechatapp.utils.SharedPreferencesHelper
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class GroupChatMessageRecyclerViewAdapter(options: FirestoreRecyclerOptions<GroupChatMessage>) :
    FirestoreRecyclerAdapter<GroupChatMessage, GroupChatMessageViewHolder>(options) {

    private lateinit var context: Context

    constructor(options: FirestoreRecyclerOptions<GroupChatMessage>, ctx:Context) : this(options) {
        this.context = ctx
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupChatMessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_group_message, parent, false)
        return GroupChatMessageViewHolder(view)
    }


    override fun onBindViewHolder(
        holder: GroupChatMessageViewHolder,
        position: Int,
        model: GroupChatMessage,
    ) {

        var sharedPreferencesHelper = SharedPreferencesHelper(this.context)
        var savedCredentials = sharedPreferencesHelper.getSavedCredentials()

        if (model.SenderID.equals(savedCredentials.third!!.UserId)){
            holder.leftChatLayout.visibility = View.GONE

            holder.rightChatLayout.visibility = View.VISIBLE
            holder.rightChatText.text = model.Message
            holder.rightChatUser.text = model.SenderName
        } else {
            holder.rightChatLayout.visibility = View.GONE

            holder.leftChatLayout.visibility = View.VISIBLE
            holder.leftChatText.text = model.Message
            holder.leftChatUser.text = model.SenderName
        }

    }


}