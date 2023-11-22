package com.example.firebasechatapp.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.firebasechatapp.data.GroupChatRoom
import com.example.firebasechatapp.data.Post
import com.example.firebasechatapp.data.User
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object MyUtils {

    fun getInitials(fullName: String): String {
        return fullName.split(" ").joinToString("") { it.first().toString() }
    }

    fun getSubString(str: String, maxLength:Int): String {
        return str.subSequence(0, minOf(str.length, maxLength)).toString()
    }

    fun timeStampToString(timestamp : Timestamp): String {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())
        return dateFormat.format(timestamp.toDate())
    }

    fun getChatRoomId(userId1:String, userId2:String): String {
        if (userId1.hashCode() < userId2.hashCode()){
            return userId1 + "_" + userId2
        } else {
            return userId2 + "_" + userId1
        }
    }

    fun passUserAsIntent(intent:Intent, user:User){
        intent.putExtra("UserId", user.UserId)
        intent.putExtra("FullName", user.FullName)
        intent.putExtra("UserName", user.UserName)
        intent.putExtra("Age", user.Age)
        intent.putExtra("About", user.About)
    }

    fun getUserFromIntent(intent:Intent):User{
        // Create a new User object
        val user = User()

        // Retrieve data from the Intent and set it on the User object
        user.UserId = intent.getStringExtra("UserId") ?: ""
        user.FullName = intent.getStringExtra("FullName") ?: ""
        user.UserName = intent.getStringExtra("UserName") ?: ""
        user.Age = intent.getIntExtra("Age", 0) // Default value 0, adjust as needed
        user.About = intent.getStringExtra("About") ?: ""

        // Return the User object
        return user
    }


    fun passPostAsIntent(intent:Intent, post:Post){
        intent.putExtra("PostID", post.PostID)
        intent.putExtra("AuthorId", post.AuthorId)
        intent.putExtra("PostName", post.PostName)
        intent.putExtra("PostDescription", post.PostDescription)
        intent.putExtra("ImageUri", post.ImageUri)
    }

    fun getPostFromIntent(intent:Intent):Post{
        // Create a new Post object
        val post = Post()

        // Retrieve data from the Intent and set it on the User object
        post.PostID = intent.getStringExtra("PostID") ?: ""
        post.AuthorId = intent.getStringExtra("AuthorId") ?: ""
        post.PostDescription = intent.getStringExtra("PostDescription") ?: ""
        post.PostName = intent.getStringExtra("PostName") ?: ""
        post.ImageUri = intent.getStringExtra("ImageUri") ?: ""

        // Return the Post object
        return post
    }



    fun passGroupChatRoomAsIntent(intent: Intent, groupChatRoom: GroupChatRoom) {
        intent.putExtra("GroupChatRoomId", groupChatRoom.GroupChatRoomId)
        intent.putExtra("GroupCreatorId", groupChatRoom.GroupCreatorId)
        intent.putExtra("GroupName", groupChatRoom.GroupName)
        intent.putExtra("LastMessageSenderName", groupChatRoom.LastMessageSenderName)
        intent.putExtra("LastMessageSenderId", groupChatRoom.LastMessageSenderId)
        intent.putExtra("LastMessage", groupChatRoom.LastMessage)
        intent.putExtra("LastMessageTimestamp", groupChatRoom.LastMessageTimestamp)
        intent.putStringArrayListExtra("UserIds", ArrayList(groupChatRoom.UserIds))
    }

    fun getGroupChatRoomFromIntent(intent: Intent): GroupChatRoom {
        val groupChatRoom = GroupChatRoom()

        groupChatRoom.GroupChatRoomId = intent.getStringExtra("GroupChatRoomId") ?: ""
        groupChatRoom.GroupCreatorId = intent.getStringExtra("GroupCreatorId") ?: ""
        groupChatRoom.GroupName = intent.getStringExtra("GroupName") ?: ""
        groupChatRoom.LastMessageSenderName = intent.getStringExtra("LastMessageSenderName") ?: ""
        groupChatRoom.LastMessageSenderId = intent.getStringExtra("LastMessageSenderId") ?: ""
        groupChatRoom.LastMessage = intent.getStringExtra("LastMessage") ?: ""
        groupChatRoom.LastMessageTimestamp = intent.getSerializableExtra("LastMessageTimestamp") as? Timestamp
        groupChatRoom.UserIds = intent.getStringArrayListExtra("UserIds") ?: emptyList()

        return groupChatRoom
    }




    fun showToast(message: String, ctx: Context) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
    }



}

