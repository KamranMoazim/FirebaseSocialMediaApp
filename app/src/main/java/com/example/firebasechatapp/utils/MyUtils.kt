package com.example.firebasechatapp.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.firebasechatapp.data.ChatRoom
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

    fun showToast(message: String, ctx: Context) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
    }



}

