package com.example.firebasechatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.firebasechatapp.data.Post
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.repositories.PostRepository
import com.example.firebasechatapp.utils.AppConsts
import com.example.firebasechatapp.utils.FirebaseUtils
import com.example.firebasechatapp.utils.MyUtils
import com.example.firebasechatapp.utils.SharedPreferencesHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class PostDetailActivity : AppCompatActivity() {

//    private lateinit var firestoreReference: CollectionReference
//    private lateinit var storageReference: StorageReference
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var savedCredentials:Triple<String?, String?, User?>

    private lateinit var postRepository: PostRepository

    private lateinit var detailViewImage:ImageView
    private lateinit var detailNameTextView:TextView
    private lateinit var detailDescriptionTextView:TextView
    private lateinit var detailViewDeleteBtn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        postRepository = PostRepository(this)


        var postFromIntent = MyUtils.getPostFromIntent(intent)


        sharedPreferencesHelper = SharedPreferencesHelper(this)
        savedCredentials = sharedPreferencesHelper.getSavedCredentials()


//        storageReference = FirebaseUtils.getStorageReferenceForPostImage(postFromIntent.PostID)
//        firestoreReference = FirebaseUtils.allPostsCollectionReference()


        detailViewImage = findViewById(R.id.detail_image_single_car_view)
        detailNameTextView = findViewById(R.id.detail_name_single_car_view)
        detailDescriptionTextView = findViewById(R.id.detail_description_single_car_view)
        detailViewDeleteBtn = findViewById(R.id.deletebtn)



        detailNameTextView.text = postFromIntent.PostName
        detailDescriptionTextView.text = postFromIntent.PostDescription
        Picasso.get().load(postFromIntent.ImageUri).into(detailViewImage)



        if (postFromIntent.AuthorId == savedCredentials.third!!.UserId) {
            // If the post author is the current user, show the delete button
            detailViewDeleteBtn.visibility = View.VISIBLE
        } else {
            // If the post author is not the current user, hide the delete button
            detailViewDeleteBtn.visibility = View.GONE
        }



        detailViewDeleteBtn.setOnClickListener {

            postRepository.deleteUsersPost(postFromIntent.PostID) {
                success, message ->
                run {
                    if (success) {
                        goToProfileActivity()
                    } else {
                        myToast(message)
                    }
                }
            }
        }

    }

    private fun goToProfileActivity() {
        myToast("Deleted Data Successfully")
        val intent = Intent(applicationContext, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun myToast(msg:String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}