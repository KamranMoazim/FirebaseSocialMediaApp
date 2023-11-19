package com.example.firebasechatapp.repositories

import android.content.Context
import android.util.Log
import com.example.firebasechatapp.data.ChatRoom
import com.example.firebasechatapp.utils.FirebaseUtils
import com.example.firebasechatapp.utils.SharedPreferencesHelper
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query


class ChatRoomRepository(
    private val context: Context
) {

    private val firestoreReference: CollectionReference = FirebaseUtils.allChatRoomCollectionReference()
    private val sharedPreferencesHelper: SharedPreferencesHelper = SharedPreferencesHelper(this.context)


    fun getMyChatRooms(
        callback: (Boolean, String, List<ChatRoom>) -> Unit
    ){

        var chatRooms = mutableListOf<ChatRoom>()

        firestoreReference
            .whereArrayContains("userIds", sharedPreferencesHelper.getSavedCredentials().third!!.UserId)
            .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Check if there is data in the snapshot
                if (!querySnapshot.isEmpty) {
                    // Data exists, log or process it
                    for (documentSnapshot in querySnapshot.documents) {
                        val chatRoom = documentSnapshot.toObject(ChatRoom::class.java)
                        // Log or process each user
                        chatRooms.add(chatRoom!!)
                    }
                    callback(true,  "Received ChatRooms from Server", chatRooms)
//                    adapter.updateMyChatsLis(chatRooms)
                } else {
                    // No data found
//                    Log.d("UserData", "No users found")
                    callback(false,  "No ChatRooms found", chatRooms)
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors here
//                Log.e("UserData", "Error getting users: ${exception.message}")
                callback(false,  "Error getting users: ${exception.message}", chatRooms)
            }
    }



    fun getOrCreateChatRoom(
            myId:String,
            otherUser:String,
            chatroomId:String,
            callback: (Boolean, String, ChatRoom?) -> Unit
        ){

        var tempChatRoom: ChatRoom

        sharedPreferencesHelper.getSavedCredentials()

        FirebaseUtils.getChatRoomReference(chatroomId).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    tempChatRoom = task.getResult().toObject(ChatRoom::class.java)!!

                    if (tempChatRoom == null){
                        // first time chat
                        tempChatRoom = ChatRoom(
                            ChatRoomId = chatroomId,
                            LastMessageSenderId = "",
                            LastMessage = "",
                            LastMessageTimestamp = Timestamp.now(),
                            listOf(myId, otherUser)
                        )
                        FirebaseUtils.getChatRoomReference(chatroomId).set(tempChatRoom!!)
//                        callback(true, "Start your Chat", tempChatRoom)
                    } else {
//                        callback(true, "Start your Chat", tempChatRoom)
                    }
                    callback(true, "Start your Chat", tempChatRoom)

                } else {
                    callback(false, "Server Error, Try Again", null)
                }
            }

    }

}