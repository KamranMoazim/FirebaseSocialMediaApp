package com.example.firebasechatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.Adapters.GroupFriendsRecyclerViewAdapter
import com.example.firebasechatapp.data.GroupChatRoom
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.repositories.UserRepository
import com.example.firebasechatapp.utils.MyUtils
import com.example.firebasechatapp.utils.SharedPreferencesHelper

class ViewGroupMembersActivity : AppCompatActivity() {



    private lateinit var progressBar: ProgressBar
    private fun showLoader() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        progressBar.visibility = View.GONE
    }


    private var groupChatRoom: GroupChatRoom? = null



    private lateinit var recyclerView : RecyclerView
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var savedCredentials:Triple<String?, String?, User?>

    private lateinit var userRepository: UserRepository

    private lateinit var adapter: GroupFriendsRecyclerViewAdapter
    private lateinit var users : List<User>

    private lateinit var groupName : TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_group_members)

        groupChatRoom = MyUtils.getGroupChatRoomFromIntent(intent)
        progressBar = findViewById(R.id.loadingProgressBarLayout)
        userRepository = UserRepository(this)




        recyclerView = findViewById(R.id.my_friends_recycler_view)
        groupName = findViewById(R.id.group_name)
        sharedPreferencesHelper = SharedPreferencesHelper(this)
        savedCredentials = sharedPreferencesHelper.getSavedCredentials()


        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(false)


        adapter = GroupFriendsRecyclerViewAdapter(this, groupChatRoom!!, false)
        recyclerView.adapter = adapter


        groupName.text = groupChatRoom!!.GroupName

        var searchBtn = findViewById<ImageView>(R.id.search_friend_btn)
        searchBtn.visibility = View.GONE


        loadData()
    }



    private fun loadData() {
        showLoader()
        users = mutableListOf()

        val myFriendsIds = savedCredentials.third!!.MyFriendsIds

        if (myFriendsIds.isNotEmpty()) {

            userRepository.fetchMyFriends() { success, message, userList ->
                run {
                    //                        myToast(message)
                    if (success) {
                        users = removeUsersInGroup(userList, groupChatRoom!!.UserIds)
                        adapter.updateUserLis(users)
                    }
                    if (users.isEmpty()) {
                        myToast("Your have no Friends in this group")
                    }
                    hideLoader()
                }
            }

        } else {
            // you have no friends
            myToast("You have ")
            hideLoader()
        }


    }




    fun removeUsersInGroup(users: List<User>, UserIds: List<String>): List<User> {
        // Filter out users whose UserId is in the UserIds list of the GroupChatRoom
        return users.filter { user ->
            user.UserId in UserIds
        }
    }


    private fun myToast(msg:String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}