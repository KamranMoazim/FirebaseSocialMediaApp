package com.example.firebasechatapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.Adapters.FriendsRecyclerViewAdapter
import com.example.firebasechatapp.ViewHolders.FriendsViewHolder
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.utils.FirebaseUtils
import com.example.firebasechatapp.utils.SharedPreferencesHelper
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference


class MessagesFragment : Fragment() {

    private lateinit var myFriendsRecyclerView : RecyclerView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var savedCredentials:Triple<String?, String?, User?>

    private lateinit var options: FirebaseRecyclerOptions<User>
    private lateinit var adapter: FirebaseRecyclerAdapter<User, FriendsViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        myFriendsRecyclerView = view.findViewById(R.id.my_friends_recycler_view)
//        sharedPreferencesHelper = SharedPreferencesHelper(requireContext())
//        savedCredentials = sharedPreferencesHelper.getSavedCredentials()
//        databaseReference = FirebaseUtils.getDatabaseReference().child("Users")
//
//
//        myFriendsRecyclerView.layoutManager = LinearLayoutManager(context)
//        myFriendsRecyclerView.setHasFixedSize(true)
//
//        options = FirebaseRecyclerOptions.Builder<User>()
//            .setQuery(databaseReference, User::class.java)
//            .build()
//
//        adapter = FriendsRecyclerViewAdapter(options)
//        myFriendsRecyclerView.adapter = adapter
//        adapter.startListening()
    }


}