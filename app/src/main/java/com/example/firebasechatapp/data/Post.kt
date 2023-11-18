package com.example.firebasechatapp.data


class Post {
    var PostName: String = ""
    var PostDescription: String = ""
    var ImageUri: String = ""
    var AuthorId: String = ""

    constructor() // Required empty constructor for Firebase

    constructor(PostName: String, ImageUri: String, AuthorId:String, PostDescription:String) {
        this.PostName = PostName
        this.ImageUri = ImageUri
        this.AuthorId = AuthorId
        this.PostDescription = PostDescription
    }
    override fun toString(): String {
        return "Post(PostName=$PostName, ImageUri='$ImageUri')"
    }
}