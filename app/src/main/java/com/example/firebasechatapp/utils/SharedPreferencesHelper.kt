package com.example.firebasechatapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.firebasechatapp.data.User

class SharedPreferencesHelper(var context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        const val PREFS_NAME = "UserPrefs"
        const val KEY_EMAIL = "Email"
        const val KEY_PASSWORD = "Password"
        const val KEY_USREID = "UserId"
        const val KEY_USERNAME = "UserName"
        const val KEY_FULLNAME = "FullName"
        const val KEY_ABOUT = "About"
        const val KEY_AGE = "Age"
    }

    fun saveCredentials(userId: String, email: String, password: String, username: String, fullname: String, about:String, age: Int, friendsIDs:List<String>) {

        sharedPreferences.edit().apply {
            putString(KEY_EMAIL, email)
            putString(KEY_PASSWORD, password)
            putString(KEY_USREID, userId)
            putString(KEY_USERNAME, username)
            putString(KEY_FULLNAME, fullname)
            putString(KEY_ABOUT, about)
            putInt(KEY_AGE, age)
            apply()
        }
        val friendsDatabaseHelper = FriendsDatabaseHelper(context)

        friendsIDs.forEach { friendId ->
            friendsDatabaseHelper.addFriend(friendId)
        }

    }

    fun getSavedCredentials(): Triple<String?, String?, User?> {
        val email = sharedPreferences.getString(KEY_EMAIL, "")
        val password = sharedPreferences.getString(KEY_PASSWORD, "")

        val friendsDatabaseHelper = FriendsDatabaseHelper(context)
        var friendsIDs:List<String> = friendsDatabaseHelper.getAllFriends()

        val user = User(
            UserId = sharedPreferences.getString(KEY_USREID, "")!!,
            UserName = sharedPreferences.getString(KEY_USERNAME, "")!!,
            FullName = sharedPreferences.getString(KEY_FULLNAME, "")!!,
            About = sharedPreferences.getString(KEY_ABOUT, "")!!,
            Age = sharedPreferences.getInt(KEY_AGE, 0),
            MyFriendsIds = friendsIDs
        )

        return Triple(email, password, user)
    }


    fun clearCredentials() {
        sharedPreferences.edit().clear().apply()
    }
}
