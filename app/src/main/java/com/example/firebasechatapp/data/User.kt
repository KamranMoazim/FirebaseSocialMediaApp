package com.example.firebasechatapp.data

class User {
    var UserId: String = ""
    var UserName: String = ""
    var FullName: String = ""
    var About: String = ""
    var Age: Int = 0

    constructor()

    constructor(UserId:String = "", UserName: String, FullName: String, About: String, Age:Int) {
        this.UserId = UserId
        this.UserName = UserName
        this.FullName = FullName
        this.About = About
        this.Age = Age
    }


    override fun toString(): String {
        return "User(username='$UserName', fullName='$FullName', age=$Age)"
    }


}