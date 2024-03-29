package com.example.firebasechatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.OnBackPressedDispatcher
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.Adapters.AddFriendsRecyclerViewAdapter
import com.example.firebasechatapp.Adapters.ChatMessageRecyclerViewAdapter
import com.example.firebasechatapp.data.ChatMessage
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.utils.FirebaseUtils
import com.example.firebasechatapp.utils.SharedPreferencesHelper
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query

class SearchUserActivity : AppCompatActivity() {

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var savedCredentials:Triple<String?, String?, User?>
    private lateinit var adapter: AddFriendsRecyclerViewAdapter

    private lateinit var users : MutableList<User>
    private lateinit var myFriendsIds : List<String>
    private lateinit var firestoreReference: CollectionReference

    private lateinit var searchInput:EditText
    private lateinit var backBtn:ImageButton
    private lateinit var searchBtn:ImageButton
    private lateinit var recyclerView:RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_user)

        searchInput = findViewById(R.id.search_username_input)
        searchBtn = findViewById(R.id.search_user_btn)
        backBtn = findViewById(R.id.go_back)
        recyclerView = findViewById(R.id.search_user_recycler)

        sharedPreferencesHelper = SharedPreferencesHelper(this)
        savedCredentials = sharedPreferencesHelper.getSavedCredentials()

        firestoreReference = FirebaseUtils.allUsersCollectionReference()

        myFriendsIds = mutableListOf()
        (myFriendsIds as MutableList<String>).addAll(savedCredentials.third!!.MyFriendsIds)
        (myFriendsIds as MutableList<String>).add(savedCredentials.third!!.UserId) // skipping myself too

        backBtn.setOnClickListener { onBackPressed() }

        searchBtn.setOnClickListener{
            searchUsers(searchInput.text.toString(), myFriendsIds)
        }




        setupChatRecyclerView()
        loadData("")

    }

    private fun searchUsers(
        userNameToSearch:String,
        peoplesToSkip:List<String>
    ) {

        FirebaseUtils.searchUsersByUsername(userNameToSearch, peoplesToSkip) { filteredUsers ->
            // Process the list of users returned from the search
            users = filteredUsers.toMutableList()
            adapter.updateAddNewFriendsList(filteredUsers)
        }

    }

    private fun loadData(data: String) {
        users = mutableListOf()

        val existingUsers = mutableListOf<User>()

        FirebaseUtils.getTop10UsersByUsername(existingUsers, listOf(savedCredentials.third!!.UserId) + myFriendsIds) { filteredUsers ->
            users = filteredUsers.toMutableList()
            adapter.updateAddNewFriendsList(filteredUsers)
        }

    }

    private fun setupChatRecyclerView() {


        var manager = LinearLayoutManager(this)
        manager.reverseLayout = true
        recyclerView.layoutManager = manager

        adapter = AddFriendsRecyclerViewAdapter(this)
        recyclerView.adapter = adapter

    }
}