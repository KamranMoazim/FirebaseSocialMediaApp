package com.example.firebasechatapp.ViewHolders

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.R
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.utils.MyUtils

class AddFriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    lateinit var addFriendBtn: ImageButton
    lateinit var imageTextView: TextView
    lateinit var usernameTextView: TextView
    lateinit var singleFriendFullnameView: TextView

    fun bindData(user: User) {

        addFriendBtn = itemView.findViewById(R.id.add_friend_btn)
        imageTextView = itemView.findViewById(R.id.single_friend_imageText)
        usernameTextView = itemView.findViewById(R.id.single_friend_username_text)
        singleFriendFullnameView = itemView.findViewById(R.id.single_friend_fl_name_text)

        imageTextView.text = MyUtils.getInitials(user.FullName)
        usernameTextView.text = MyUtils.getSubString(user.UserName, 20) + "..."
        singleFriendFullnameView.text = MyUtils.getSubString(user.FullName, 25) + "..."



    }

}