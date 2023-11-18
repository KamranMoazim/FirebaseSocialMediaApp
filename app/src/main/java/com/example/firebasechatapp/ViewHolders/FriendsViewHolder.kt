package com.example.firebasechatapp.ViewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.R
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.utils.MyUtils

class FriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {

    private lateinit var imageTextView: TextView
    private lateinit var singleFriendUsernameView: TextView
    // private lateinit var messageFriendBtn: ImageButton
    // private lateinit var removeFriendBtn: ImageButton

    fun bindData(user: User) {

        imageTextView = itemView.findViewById(R.id.single_friend_imageText)
        singleFriendUsernameView = itemView.findViewById(R.id.single_friend_username_text)
        // messageFriendBtn = itemView.findViewById(R.id.message_friend_btn)
        // removeFriendBtn = itemView.findViewById(R.id.remove_friend_btn)

        imageTextView.text = MyUtils.getInitials(user.FullName)
//        singleFriendUsernameView.text = user.UserName.subSequence(0, 20).toString() + "..."
        singleFriendUsernameView.text = MyUtils.getSubString(user.UserName, 20) + "..."

    }

}