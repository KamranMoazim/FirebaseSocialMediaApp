package com.example.firebasechatapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class FriendsFragment : Fragment() {

    private lateinit var myFriendsRecyclerView : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myFriendsRecyclerView = view.findViewById(R.id.my_friends_recycler_view)

        var searchBtn = view.findViewById<ImageView>(R.id.search_friend_btn)
        searchBtn.setOnClickListener{
            var intent = Intent(context, SearchUserActivity::class.java)
            startActivity(intent)
        }


    }

}