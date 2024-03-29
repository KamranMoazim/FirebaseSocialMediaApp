package com.example.firebasechatapp.data

import com.google.firebase.Timestamp


class ChatRoom {

    var IsGroupChatRoom: Boolean = false
    var ChatRoomId: String = ""
    var LastMessageSenderId: String = ""
    var LastMessage: String = ""
    var LastMessageTimestamp: Timestamp? = null
    var UserIds: List<String> = listOf()

    constructor() // Required empty constructor for Firebase

    constructor(
        ChatRoomId: String,
        LastMessageSenderId: String,
        LastMessage: String,
        LastMessageTimestamp: Timestamp?,
        UserIds: List<String>,
    ) {
        this.ChatRoomId = ChatRoomId
        this.LastMessageSenderId = LastMessageSenderId
        this.LastMessage = LastMessage
        this.LastMessageTimestamp = LastMessageTimestamp
        this.UserIds = UserIds
        this.IsGroupChatRoom = false
    }

//    fun getOrCreateChatRoom(){
//        FirebaseUtils.getChatRoomReference(ChatRoomId).get()
//            .addOnCompleteListener{task -> {
//            }}
//    }


}