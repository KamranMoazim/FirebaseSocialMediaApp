package com.example.firebasechatapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.Adapters.FriendsRecyclerViewAdapter
import com.example.firebasechatapp.Adapters.PostRecyclerViewAdapter
import com.example.firebasechatapp.ViewHolders.FriendsViewHolder
import com.example.firebasechatapp.ViewHolders.PostViewHolder
import com.example.firebasechatapp.data.Post
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.repositories.UserRepository
import com.example.firebasechatapp.utils.AppConsts
import com.example.firebasechatapp.utils.FirebaseUtils
import com.example.firebasechatapp.utils.SharedPreferencesHelper
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.CollectionReference

class FriendsFragment : Fragment() {


    private lateinit var progressBar: ProgressBar
    private fun showLoader() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        progressBar.visibility = View.GONE
    }


    private lateinit var myFriendsRecyclerView : RecyclerView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firestoreReference: CollectionReference
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var savedCredentials:Triple<String?, String?, User?>

    private lateinit var userRepository: UserRepository

//    private lateinit var options:FirebaseRecyclerOptions<User>
//    private lateinit var adapter: FirebaseRecyclerAdapter<User, FriendsViewHolder>
    private lateinit var adapter: FriendsRecyclerViewAdapter
    private lateinit var users : List<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_friends, container, false)

        progressBar = view.findViewById(R.id.loadingProgressBarLayout)

        userRepository = UserRepository(requireContext())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        myFriendsRecyclerView = view.findViewById(R.id.my_friends_recycler_view)
        sharedPreferencesHelper = SharedPreferencesHelper(requireContext())
        savedCredentials = sharedPreferencesHelper.getSavedCredentials()
//        databaseReference = FirebaseUtils.getDatabaseReference().child("Users")
//        firestoreReference = FirebaseUtils.allUsersCollectionReference()


        myFriendsRecyclerView.layoutManager = LinearLayoutManager(context)
        myFriendsRecyclerView.setHasFixedSize(false)


        adapter = FriendsRecyclerViewAdapter(requireContext())
        myFriendsRecyclerView.adapter = adapter


        var searchBtn = view.findViewById<ImageView>(R.id.search_friend_btn)
        searchBtn.setOnClickListener{
            var intent = Intent(context, SearchUserActivity::class.java)
            startActivity(intent)
        }


        loadData("")
    }



    private fun loadData(data: String) {
        showLoader()
        users = mutableListOf()

        val myFriendsIds = savedCredentials.third!!.MyFriendsIds

        if (myFriendsIds.isNotEmpty()) {

            userRepository.fetchMyFriends(){
                success, message, userList ->
                    run {
//                        myToast(message)
                        if (success){
                            users = userList
                            adapter.updateUserLis(userList)
                        }
                        hideLoader()
                    }
                }

        } else {
            // you have no friends
            myToast("You have no friends")
        }


    }


    private fun myToast(msg:String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

}