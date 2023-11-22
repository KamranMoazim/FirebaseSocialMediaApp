package com.example.firebasechatapp.data

import com.google.firebase.Timestamp

class GroupChatMessage {

    var Message: String = ""
    var SenderID: String = ""
    var SenderName: String = ""
    var MessageTimestamp: Timestamp

    constructor() {
        MessageTimestamp = Timestamp.now()
    }

    constructor(Message: String, SenderName:String, SenderID: String, MessageTimestamp: Timestamp) {
        this.Message = Message
        this.SenderID = SenderID
        this.SenderName = SenderName
        this.MessageTimestamp = MessageTimestamp
    }

}