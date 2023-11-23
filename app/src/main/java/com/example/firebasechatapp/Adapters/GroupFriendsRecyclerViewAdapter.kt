package com.example.firebasechatapp.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import com.example.firebasechatapp.ChatActivity
import com.example.firebasechatapp.R
import com.example.firebasechatapp.ViewHolders.FriendsViewHolder
import com.example.firebasechatapp.data.ChatRoom
import com.example.firebasechatapp.data.GroupChatRoom
import com.example.firebasechatapp.repositories.ChatRoomRepository
import com.example.firebasechatapp.utils.SharedPreferencesHelper

class GroupFriendsRecyclerViewAdapter(val ctx: Context, val currentChatGroup:GroupChatRoom, var isAdd:Boolean = true) : FriendsRecyclerViewAdapter(ctx) {


    private var chatRoomRepository: ChatRoomRepository = ChatRoomRepository(ctx)


    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {


        var currentUser = usersList[position]
        holder.bindData(currentUser)


        var cure = holder.itemView.findViewById<ImageButton>(R.id.remove_friend_btn)





        if (currentChatGroup.GroupCreatorId == savedCredentials.third!!.UserId){
            cure.visibility = View.VISIBLE

            if (isAdd){
                cure.setImageResource(R.drawable.ic_add)
            } else {
                cure.setImageResource(R.drawable.ic_remove_circle)
            }

        } else {
            cure.visibility = View.GONE
        }

        if (isAdd){
            cure.setOnClickListener {
                chatRoomRepository.addPersonToGroupChat(currentChatGroup.GroupChatRoomId, currentUser.UserId){  // means we are adding person to OurChatGroup
                        success, message ->
                    run {
                        myToast(message)
                        usersList.removeAt(position)
                        notifyDataSetChanged()
                    }
                }
            }
        } else {
            cure.setOnClickListener {
                chatRoomRepository.removePersonFromGroupChat(currentChatGroup.GroupChatRoomId, currentUser.UserId){  // means we are adding person to OurChatGroup
                        success, message ->
                    run {
                        myToast(message)
                        usersList.removeAt(position)
                        notifyDataSetChanged()
                    }
                }
            }
        }

        holder.itemView.findViewById<ImageButton>(R.id.message_friend_btn).visibility = View.GONE

    }


    private fun myToast(msg:String) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
    }
}