package com.example.firebasechatapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.Adapters.FriendsRecyclerViewAdapter
import com.example.firebasechatapp.Adapters.PostRecyclerViewAdapter
import com.example.firebasechatapp.ViewHolders.FriendsViewHolder
import com.example.firebasechatapp.ViewHolders.PostViewHolder
import com.example.firebasechatapp.data.Post
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.utils.AppConsts
import com.example.firebasechatapp.utils.FirebaseUtils
import com.example.firebasechatapp.utils.SharedPreferencesHelper
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class FriendsFragment : Fragment() {

    private lateinit var myFriendsRecyclerView : RecyclerView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var savedCredentials:Triple<String?, String?, User?>

    private lateinit var options:FirebaseRecyclerOptions<User>
    // private lateinit var adapter: FirebaseRecyclerAdapter<User, FriendsViewHolder>
    private lateinit var adapter: FriendsRecyclerViewAdapter
    private lateinit var users : List<User>

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
        sharedPreferencesHelper = SharedPreferencesHelper(requireContext())
        savedCredentials = sharedPreferencesHelper.getSavedCredentials()
        databaseReference = FirebaseUtils.getDatabaseReference().child("Users")


        myFriendsRecyclerView.layoutManager = LinearLayoutManager(context)
        myFriendsRecyclerView.setHasFixedSize(false)


        adapter = FriendsRecyclerViewAdapter()
        myFriendsRecyclerView.adapter = adapter


        var searchBtn = view.findViewById<ImageView>(R.id.search_friend_btn)
        searchBtn.setOnClickListener{
            var intent = Intent(context, SearchUserActivity::class.java)
            startActivity(intent)
        }


        loadData("")
    }




    private fun loadData(data:String) {


        var query = databaseReference
        users = mutableListOf()

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check if there is data in the snapshot
                if (dataSnapshot.exists()) {
                    // Data exists, log or process it
                    for (userSnapshot in dataSnapshot.children) {
                        val user = userSnapshot.getValue(User::class.java)
                        // Log or process each user
                        Log.d("UserData", "User: $user")
                        if (user!!.UserId != savedCredentials.third!!.UserId){
                            (users as MutableList<User>).add(user!!)
                        }
                    }
                    adapter.updateUserLis(users)
                } else {
                    // No data found
                    Log.d("UserData", "No users found")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
                Log.e("UserData", "Error getting users: ${databaseError.message}")
            }
        })

    }


}