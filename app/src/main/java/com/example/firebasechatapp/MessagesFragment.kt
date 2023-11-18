package com.example.firebasechatapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.Adapters.ChatMessageRecyclerViewAdapter
import com.example.firebasechatapp.Adapters.FriendsRecyclerViewAdapter
import com.example.firebasechatapp.Adapters.MyChatsRecyclerViewAdapter
import com.example.firebasechatapp.ViewHolders.FriendsViewHolder
import com.example.firebasechatapp.data.ChatMessage
import com.example.firebasechatapp.data.ChatRoom
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.utils.FirebaseUtils
import com.example.firebasechatapp.utils.SharedPreferencesHelper
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query


class MessagesFragment : Fragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var firestoreReference: CollectionReference

    private lateinit var options: FirebaseRecyclerOptions<User>

    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var savedCredentials:Triple<String?, String?, User?>
    private lateinit var adapter: MyChatsRecyclerViewAdapter
    private lateinit var chatRooms : List<ChatRoom>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_messages, container, false)

//        recyclerView = view.findViewById(R.id.my_chats_recycler_view)
//
//        sharedPreferencesHelper = SharedPreferencesHelper(this.requireContext())
//        savedCredentials = sharedPreferencesHelper.getSavedCredentials()
//        firestoreReference = FirebaseUtils.allChatRoomCollectionReference()
//
//        setupRecyclerView()

        return view
    }

//    private fun setupRecyclerView() {
//        var query = FirebaseUtils.allChatRoomCollectionReference()
//            .whereArrayContains("userIds", savedCredentials.third!!.UserId)
//            .orderBy("lastMessageTimeStamp", Query.Direction.DESCENDING)
//
//        var options =
//            FirestoreRecyclerOptions.Builder<ChatRoom>()
//                .setQuery(query, ChatRoom::class.java).build()
//
////        adapter = MyChatsRecyclerViewAdapter(options, this.requireContext())
//        adapter = MyChatsRecyclerViewAdapter(this.requireContext())
//
//        var manager = LinearLayoutManager(this.requireContext())
//        manager.reverseLayout = true
//        recyclerView.layoutManager = manager
//
//
//        recyclerView.adapter = adapter
////        adapter.startListening()
//
//
//    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        recyclerView = view.findViewById(R.id.my_chats_recycler_view)

        sharedPreferencesHelper = SharedPreferencesHelper(this.requireContext())
        savedCredentials = sharedPreferencesHelper.getSavedCredentials()
        firestoreReference = FirebaseUtils.allChatRoomCollectionReference()

//        setupRecyclerView()

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(false)


        adapter = MyChatsRecyclerViewAdapter(this.requireContext())
        recyclerView.adapter = adapter

        loadData("")

    }




    private fun loadData(data: String) {
        chatRooms = mutableListOf()

        val myUserId = savedCredentials.third!!.UserId

        firestoreReference
            .whereArrayContains("userIds", myUserId)
            .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Check if there is data in the snapshot
                if (!querySnapshot.isEmpty) {
                    // Data exists, log or process it
                    for (documentSnapshot in querySnapshot.documents) {
                        val chatRoom = documentSnapshot.toObject(ChatRoom::class.java)
                        // Log or process each user
                        Log.d("ChatRoom", "chatRoom: $chatRoom")
                        (chatRooms as MutableList<ChatRoom>).add(chatRoom!!)
                    }
                    adapter.updateMyChatsLis(chatRooms)
                    Log.d("loadData", chatRooms.size.toString())
                } else {
                    // No data found
                    Log.d("UserData", "No users found")
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors here
                Log.e("UserData", "Error getting users: ${exception.message}")
            }


    }



}