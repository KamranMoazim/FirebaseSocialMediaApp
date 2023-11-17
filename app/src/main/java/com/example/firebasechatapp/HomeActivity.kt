package com.example.firebasechatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.Adapters.PostRecyclerViewAdapter
import com.example.firebasechatapp.ViewHolders.PostViewHolder
import com.example.firebasechatapp.data.Post
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.utils.AppConsts
import com.example.firebasechatapp.utils.FirebaseUtils
import com.example.firebasechatapp.utils.SharedPreferencesHelper
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference

class HomeActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var savedCredentials:Triple<String?, String?, User?>

    private lateinit var inputSearch:EditText
    private lateinit var carRecyclerView:RecyclerView
    private lateinit var floatingAddBtn:FloatingActionButton
    private lateinit var floatingProfileBtn:FloatingActionButton
    private lateinit var floatingLogoutBtn:FloatingActionButton
    private lateinit var options:FirebaseRecyclerOptions<Post>
    private lateinit var adapter:FirebaseRecyclerAdapter<Post, PostViewHolder>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sharedPreferencesHelper = SharedPreferencesHelper(this)
        savedCredentials = sharedPreferencesHelper.getSavedCredentials()

//        databaseReference = Firebase.database.getReference().child(AppConsts.POSTS_CONSTANT).child(savedCredentials.third!!.UserId)
        databaseReference = FirebaseUtils.getPostsReferenceForUser(savedCredentials.third!!.UserId)




        inputSearch = findViewById(R.id.search_input)
        carRecyclerView = findViewById(R.id.car_recycler_view)

        floatingAddBtn =  findViewById(R.id.floating_add_button)
        floatingLogoutBtn =  findViewById(R.id.floating_logout_button)
        floatingProfileBtn =  findViewById(R.id.floating_profile_button)




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
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        carRecyclerView.layoutManager = LinearLayoutManager(this)
        carRecyclerView.setHasFixedSize(true)






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


        loadData("")
    }


    private fun loadData(data:String) {

        var query = databaseReference.orderByChild(AppConsts.POST_Name_CONSTANT).startAt(data).endAt(data+"\uf8ff")

        options =
            FirebaseRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java)
                .build()

//        adapter = object : FirebaseRecyclerAdapter<Post, PostViewHolder>(options) {
//            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
//                // Create and return a new MyViewHolder instance
//                // You need to inflate your item layout here
//                val view = LayoutInflater.from(parent.context)
//                    .inflate(R.layout.single_post_view, parent, false)
//                var holder = PostViewHolder(view)
//                return holder
//            }
//
//            override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
//                // Bind the data to the MyViewHolder
//                // Set the data from the 'model' parameter to your views in the MyViewHolder
//                holder.itemView.setOnClickListener {
//                    val intent = Intent(this@HomeActivity, PostDetailActivity::class.java)
//                    intent.putExtra(AppConsts.POST_KEY_CONSTANT, getRef(position).key)
//                    startActivity(intent)
//                }
//                holder.bindData(model)
//            }
//        }
        adapter = PostRecyclerViewAdapter(options)
        carRecyclerView.adapter = adapter
        adapter.startListening()
    }


}