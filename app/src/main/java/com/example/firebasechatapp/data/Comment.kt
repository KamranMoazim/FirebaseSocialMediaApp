package com.example.firebasechatapp.data

import com.google.firebase.Timestamp

class Comment {

    var CommentId: String = ""
    var CommenterId: String = ""
    var Comment: String = ""
    var CommenterImageUri: String = ""
    var Commenter: String = ""
    var CommentedAt: Timestamp? = null

    constructor() // Required empty constructor for Firebase

    constructor(CommentId:String, CommenterId:String, Comment: String, CommenterImageUri: String, Commenter:String, CommentTime:Timestamp) {
        this.CommentId = CommentId
        this.CommenterId = CommenterId
        this.Comment = Comment
        this.CommenterImageUri = CommenterImageUri
        this.Commenter = Commenter
        this.CommentedAt = CommentTime
    }
    override fun toString(): String {
        return "Comment(CommenterId=$CommenterId, Comment='$Comment')"
    }
}