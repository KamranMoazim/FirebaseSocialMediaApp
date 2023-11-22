package com.example.firebasechatapp.data

import com.google.firebase.Timestamp

class GroupChatRoom {

    var GroupChatRoomId: String = ""
    var GroupCreatorId: String = ""
    var GroupName: String = ""
    var LastMessageSenderName: String = ""
    var LastMessageSenderId: String = ""
    var LastMessage: String = ""
    var LastMessageTimestamp: Timestamp? = null
    var UserIds: List<String> = listOf()
    var IsGroupChatRoom: Boolean = true


    constructor() // Required empty constructor for Firebase

    constructor(
        GroupChatRoomId: String,
        GroupCreatorId: String,
        GroupName: String,
        LastMessageSenderName: String,
        LastMessageSenderId: String,
        LastMessage: String,
        LastMessageTimestamp: Timestamp?,
        UserIds: List<String>,
    ) {
        this.GroupChatRoomId = GroupChatRoomId
        this.GroupCreatorId = GroupCreatorId
        this.GroupName = GroupName
        this.LastMessageSenderName = LastMessageSenderName
        this.LastMessageSenderId = LastMessageSenderId
        this.LastMessage = LastMessage
        this.LastMessageTimestamp = LastMessageTimestamp
        this.UserIds = UserIds
        this.IsGroupChatRoom = true
    }
}