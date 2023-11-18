package com.example.firebasechatapp.data

class User {
    var UserId: String = ""
    var UserName: String = ""
    var FullName: String = ""
    var About: String = ""
    var Age: Int = 0
    var MyFriendsIds: List<String> = listOf()

    constructor()

    constructor(UserId:String = "", UserName: String, FullName: String, About: String, Age:Int, MyFriendsIds: List<String> = listOf()) {
        this.UserId = UserId
        this.UserName = UserName
        this.FullName = FullName
        this.About = About
        this.Age = Age
        this.MyFriendsIds = MyFriendsIds
    }

    fun addFriend(friendId: String) {
        if (friendId !in MyFriendsIds) {
            MyFriendsIds = MyFriendsIds.toMutableList().apply {
                add(friendId)
            }
        }
    }


    override fun toString(): String {
        return "User(username='$UserName', fullName='$FullName', age=$Age)"
    }


}