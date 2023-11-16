package com.example.firebasechatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.OnBackPressedDispatcher
import androidx.recyclerview.widget.RecyclerView

class SearchUserActivity : AppCompatActivity() {

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

        backBtn.setOnClickListener { onBackPressed() }

    }
}