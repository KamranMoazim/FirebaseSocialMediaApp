package com.example.firebasechatapp.data

import com.google.firebase.Timestamp


class ChatMessage {

    var Message: String = ""
    var SenderID: String = ""
    var MessageTimestamp: Timestamp

    constructor() {
        MessageTimestamp = Timestamp.now()
    }

    constructor(Message: String, SenderID: String, MessageTimestamp: Timestamp) {
        this.Message = Message
        this.SenderID = SenderID
        this.MessageTimestamp = MessageTimestamp
    }

    override fun toString(): String {
        return "Message(Message='$Message', SenderID='$SenderID', MessageTimestamp=${MessageTimestamp})"
    }
}
