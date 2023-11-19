package com.example.firebasechatapp.repositories

import android.content.Context
import com.example.firebasechatapp.data.ChatMessage
import com.example.firebasechatapp.data.ChatRoom
import com.example.firebasechatapp.utils.FirebaseUtils
import com.example.firebasechatapp.utils.SharedPreferencesHelper
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query

class ChatMessageRepository (
    private val context: Context
) {

    private val firestoreReference: CollectionReference = FirebaseUtils.allChatRoomCollectionReference()
    private val sharedPreferencesHelper: SharedPreferencesHelper = SharedPreferencesHelper(this.context)

    fun getAllReentChatMessagesQuery (
        chatroomId:String
    ): FirestoreRecyclerOptions<ChatMessage> {

        var query = FirebaseUtils.getChatRoomMessageReference(chatroomId)
            .orderBy("messageTimestamp", Query.Direction.DESCENDING)

        var options =
            FirestoreRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage::class.java).build()

        return options
    }


    fun sendMessageToUser(
        chatRoom:ChatRoom,
        message: String,
        chatroomId:String,
        callback: (Boolean, String) -> Unit
    ) {

        val savedCredentials = sharedPreferencesHelper.getSavedCredentials()

        chatRoom!!.LastMessageTimestamp = Timestamp.now()
        chatRoom!!.LastMessageSenderId = savedCredentials.third!!.UserId
        chatRoom!!.LastMessage = message
        FirebaseUtils.getChatRoomReference(chatroomId).set(chatRoom!!)

        var chatMessage = ChatMessage(message, savedCredentials.third!!.UserId, Timestamp.now())
        FirebaseUtils.getChatRoomMessageReference(chatroomId).add(chatMessage)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
//                    messageInput.setText("")
                    callback(true, "")
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors here
//                Log.e("UserData", "Error getting users: ${exception.message}")
                callback(false, "Error Sending Message")
            }
    }
}