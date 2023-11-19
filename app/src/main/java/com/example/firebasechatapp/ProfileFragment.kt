package com.example.firebasechatapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.Adapters.FriendsRecyclerViewAdapter
import com.example.firebasechatapp.Adapters.PostRecyclerViewAdapter
import com.example.firebasechatapp.ViewHolders.PostViewHolder
import com.example.firebasechatapp.data.Post
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.utils.AppConsts
import com.example.firebasechatapp.utils.FirebaseUtils
import com.example.firebasechatapp.utils.MyUtils
import com.example.firebasechatapp.utils.SharedPreferencesHelper
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.CollectionReference
import io.reactivex.rxjava3.annotations.NonNull
import javax.annotation.Nullable


class ProfileFragment : Fragment() {


    private lateinit var databaseReference: DatabaseReference
    private lateinit var firestoreReference: CollectionReference

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var savedCredentials:Triple<String?, String?, User?>

    private lateinit var options: FirestoreRecyclerOptions<Post>
    private lateinit var adapter: FirestoreRecyclerAdapter<Post, PostViewHolder>

    private lateinit var usernameTextView: TextView
    private lateinit var fullnameTextView: TextView
    private lateinit var aboutTextView: TextView
    private lateinit var imageTextView: TextView
    private lateinit var recyclerView : RecyclerView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(@NonNull view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find TextViews by their IDs
        usernameTextView = view.findViewById(R.id.username)
        fullnameTextView = view.findViewById(R.id.fullname)
        aboutTextView = view.findViewById(R.id.about)
        imageTextView = view.findViewById(R.id.imageText)
        recyclerView = view.findViewById(R.id.all_my_posts)

        sharedPreferencesHelper = SharedPreferencesHelper(requireContext())
        savedCredentials = sharedPreferencesHelper.getSavedCredentials()

        databaseReference = FirebaseUtils.getPostsReferenceForUser(savedCredentials.third!!.UserId)
        firestoreReference = FirebaseUtils.allPostsCollectionReference()

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(false)


//        adapter = FriendsRecyclerViewAdapter(requireContext())
//        recyclerView.adapter = adapter


        // Set new text values
        usernameTextView.setText(savedCredentials.third?.UserName)
        fullnameTextView.setText(savedCredentials.third?.FullName)
        aboutTextView.setText(savedCredentials.third?.About)
        imageTextView.setText(MyUtils.getInitials(savedCredentials.third?.FullName!!))

        loadData("")
    }



    private fun loadData(data: String) {

        var myFriendsIds = mutableListOf<String>()
         myFriendsIds.add(savedCredentials.third!!.UserId) // including myself, to view my posts to

        if (myFriendsIds.isNotEmpty()) {

            val query = firestoreReference
                .whereIn("AuthorId", myFriendsIds)
                .orderBy(AppConsts.POST_Name_CONSTANT)
                .startAt(data)
                .endAt(data + "\uf8ff")

            options = FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post::class.java)
                .build()

            adapter = PostRecyclerViewAdapter(options)
            recyclerView.adapter = adapter
            adapter.startListening()

        } else {

        }


    }

}