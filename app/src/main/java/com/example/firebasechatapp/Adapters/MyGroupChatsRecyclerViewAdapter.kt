package com.example.firebasechatapp.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.ChatActivity
import com.example.firebasechatapp.GroupChatActivity
import com.example.firebasechatapp.R
import com.example.firebasechatapp.ViewHolders.MyGroupChatsViewHolder
import com.example.firebasechatapp.data.GroupChatRoom
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.utils.FirebaseUtils
import com.example.firebasechatapp.utils.MyUtils
import com.example.firebasechatapp.utils.SharedPreferencesHelper

class MyGroupChatsRecyclerViewAdapter(private val ctx: Context): RecyclerView.Adapter<MyGroupChatsViewHolder>() {

    private var myGroupChatsList = ArrayList<GroupChatRoom>()
    private var sharedPreferencesHelper: SharedPreferencesHelper
    private var savedCredentials:Triple<String?, String?, User?>

    init {
        sharedPreferencesHelper = SharedPreferencesHelper(ctx)
        savedCredentials = sharedPreferencesHelper.getSavedCredentials()
    }

    fun updateMyGroupChatsLis(myGroupChatsList : List<GroupChatRoom>){
        this.myGroupChatsList.clear()
        this.myGroupChatsList.addAll(myGroupChatsList)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyGroupChatsViewHolder {
//        Log.d("onCreateViewHolder:viewType", viewType.toString())
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_recent_chat_view, parent, false)
        return MyGroupChatsViewHolder(view)
    }



    override fun getItemCount(): Int {
        return myGroupChatsList.size
    }


    override fun onBindViewHolder(holder: MyGroupChatsViewHolder, position: Int) {

        var currentGroupChatRoom = myGroupChatsList[position]


        val lastMessageSendByMe = currentGroupChatRoom.LastMessageSenderId == savedCredentials.third!!.UserId

        holder.groupNameTextView.text = MyUtils.getSubString(currentGroupChatRoom.GroupName, 8) + "..."

        if (lastMessageSendByMe) {
            holder.lastMessageTextView.text = MyUtils.getSubString("You : " + currentGroupChatRoom.LastMessage, 25) + "..."
        } else {
            holder.lastMessageTextView.text = currentGroupChatRoom.LastMessage
        }
        holder.lastMessageTimeTextView.text = MyUtils.timeStampToString(currentGroupChatRoom.LastMessageTimestamp!!)

        holder.profileTextView.text = MyUtils.getInitials(currentGroupChatRoom.GroupName)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, GroupChatActivity::class.java)
            MyUtils.passGroupChatRoomAsIntent(intent, currentGroupChatRoom)
            holder.itemView.context.startActivity(intent)
        }

    }


}