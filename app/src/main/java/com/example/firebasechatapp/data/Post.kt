package com.example.firebasechatapp.data


class Post {
    var PostName: String = ""
    var ImageUri: String = ""

    constructor() // Required empty constructor for Firebase

    constructor(PostName: String, ImageUri: String) {
        this.PostName = PostName
        this.ImageUri = ImageUri
    }
    override fun toString(): String {
        return "Post(PostName=$PostName, ImageUri='$ImageUri')"
    }
}