package com.example.firebasechatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.Adapters.PostRecyclerViewAdapter
import com.example.firebasechatapp.ViewHolders.PostViewHolder
import com.example.firebasechatapp.data.Post
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.repositories.PostRepository
import com.example.firebasechatapp.repositories.UserRepository
import com.example.firebasechatapp.utils.AppConsts
import com.example.firebasechatapp.utils.FirebaseUtils
import com.example.firebasechatapp.utils.FriendsDatabaseHelper
import com.example.firebasechatapp.utils.SharedPreferencesHelper
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.CollectionReference

class HomeActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private fun showLoader() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        progressBar.visibility = View.GONE
    }


    private lateinit var databaseReference: DatabaseReference
    private lateinit var firestoreReference: CollectionReference
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var savedCredentials:Triple<String?, String?, User?>

    private lateinit var postRepository: PostRepository

    private lateinit var inputSearch:EditText
    private lateinit var carRecyclerView:RecyclerView


//    private lateinit var  mainFab: FloatingActionButton
//    private lateinit var  profileFab: FloatingActionButton
//    private lateinit var  addFab: FloatingActionButton
//    private lateinit var  logoutFab: FloatingActionButton

    private lateinit var floatingAddBtn: FloatingActionButton
    private lateinit var floatingProfileBtn: FloatingActionButton
    private lateinit var floatingLogoutBtn: FloatingActionButton
    private lateinit var mainFab: FloatingActionButton

    private lateinit var options:FirestoreRecyclerOptions<Post>
    private lateinit var adapter: FirestoreRecyclerAdapter<Post, PostViewHolder>







    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        progressBar = findViewById(R.id.loadingProgressBarLayout)

        postRepository = PostRepository(this)

        sharedPreferencesHelper = SharedPreferencesHelper(this)
        savedCredentials = sharedPreferencesHelper.getSavedCredentials()


        databaseReference = FirebaseUtils.getPostsReferenceForUser(savedCredentials.third!!.UserId)
        firestoreReference = FirebaseUtils.allPostsCollectionReference()




        inputSearch = findViewById(R.id.search_input)
        carRecyclerView = findViewById(R.id.car_recycler_view)

        floatingAddBtn =  findViewById(R.id.floating_add_button)
        floatingLogoutBtn =  findViewById(R.id.floating_logout_button)
        floatingProfileBtn =  findViewById(R.id.floating_profile_button)

        mainFab = findViewById(R.id.floatingActionButton)
//        profileFab = findViewById(R.id.floating_profile_button)
//        addFab = findViewById(R.id.floating_add_button)
//        logoutFab = findViewById(R.id.floating_logout_button)

        floatingProfileBtn.visibility = View.GONE
        floatingAddBtn.visibility = View.GONE
        floatingLogoutBtn.visibility = View.GONE

        mainFab.setOnClickListener {
            // Toggle visibility of other FABs
            if (floatingProfileBtn.visibility == View.VISIBLE) {
                floatingProfileBtn.visibility = View.GONE
                floatingAddBtn.visibility = View.GONE
                floatingLogoutBtn.visibility = View.GONE
                mainFab.setImageResource(R.drawable.ic_send)
            } else {
                floatingProfileBtn.visibility = View.VISIBLE
                floatingAddBtn.visibility = View.VISIBLE
                floatingLogoutBtn.visibility = View.VISIBLE
                mainFab.setImageResource(R.drawable.ic_cross)
            }
        }


        floatingProfileBtn.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        floatingAddBtn.setOnClickListener{
            val intent = Intent(this, UploadPostActivity::class.java)
            startActivity(intent)
        }

        floatingLogoutBtn.setOnClickListener{
            sharedPreferencesHelper.clearCredentials()

            val friendsDatabaseHelper = FriendsDatabaseHelper(this)
            friendsDatabaseHelper.removeAllFriendsData()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        carRecyclerView.layoutManager = LinearLayoutManager(this)
        carRecyclerView.setHasFixedSize(true)


        loadData("")



        inputSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called to notify you that characters within `charSequence` are about to be replaced with new text.
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called to notify you that somewhere within `charSequence`, the text has been changed.
            }

            override fun afterTextChanged(editable: Editable?) {
                // This method is called to notify you that somewhere within `editable`, the text has been changed.
                // You can use `editable.toString()` to get the updated text.
                val searchText = editable.toString()
                if (searchText.isNotEmpty()){
                    loadData(inputSearch.text.toString())
                } else {
                    loadData("")
                }

                // Do something with the updated text (search functionality, etc.)
                // Example: performSearch(searchText)
            }
        })



    }

    private fun loadData(data: String) {

        showLoader()

        val myFriendsIds = mutableListOf<String>()
        myFriendsIds.addAll(savedCredentials.third!!.MyFriendsIds)
        // myFriendsIds.add(savedCredentials.third!!.UserId) // including myself, to view my posts to

        if (myFriendsIds.isNotEmpty()) {

            options = postRepository.getPostsForUser(data)
            adapter = PostRecyclerViewAdapter(options)
            carRecyclerView.adapter = adapter
            adapter.startListening()

            hideLoader()

            Log.d("adapter.itemCount", adapter.itemCount.toString())


        } else {
            myToast("Please Add Some Friends to see their Posts")
            hideLoader()
        }


    }


    private fun myToast(msg:String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}