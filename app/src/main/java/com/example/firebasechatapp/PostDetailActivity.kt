package com.example.firebasechatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.firebasechatapp.data.Post
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.utils.AppConsts
import com.example.firebasechatapp.utils.FirebaseUtils
import com.example.firebasechatapp.utils.SharedPreferencesHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class PostDetailActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var savedCredentials:Triple<String?, String?, User?>

    private lateinit var detailViewImage:ImageView
    private lateinit var detailViewTextView:TextView
    private lateinit var detailViewDeleteBtn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        var postKey = intent.getStringExtra(AppConsts.POST_KEY_CONSTANT)!!

        // databaseReference = Firebase.database.getReference().child("Car").child(carKey)
        sharedPreferencesHelper = SharedPreferencesHelper(this)
        savedCredentials = sharedPreferencesHelper.getSavedCredentials()

        // databaseReference = Firebase.database.getReference().child(AppConsts.POSTS_CONSTANT).child(savedCredentials.third!!.UserId).child(postKey)
        // storageReference = Firebase.storage.getReference().child(AppConsts.POST_Image_CONSTANT).child(postKey + ".jpg")
        databaseReference = FirebaseUtils.getPostReference(postKey, savedCredentials.third!!.UserId)
        storageReference = FirebaseUtils.getStorageReferenceForPostImage(postKey)


        detailViewImage = findViewById(R.id.detail_image_single_car_view)
        detailViewTextView = findViewById(R.id.detail_name_single_car_view)
        detailViewDeleteBtn = findViewById(R.id.deletebtn)


//        Toast.makeText(this@CarDetailActivity, carKey, Toast.LENGTH_SHORT).show()

//        Log.d("CarDetailActivity", carKey)


        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

//                Toast.makeText(this@CarDetailActivity, dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show()

//                Log.d("CarDetailActivity", dataSnapshot.getValue().toString())

                // This method is called once with the initial value and again whenever data at this location is updated.
                if (dataSnapshot.exists()) {

                    val dataSnapshotValue = dataSnapshot.getValue(Post::class.java)

//                    Toast.makeText(this@CarDetailActivity, dataSnapshotValue!!.CarName, Toast.LENGTH_SHORT).show()


                    if (dataSnapshotValue != null) {
                        // Now you can use the 'dataSnapshotValue' object
                        val postName = dataSnapshotValue.PostName
                        val imageUri = dataSnapshotValue.ImageUri

                        detailViewTextView.text = postName
                        Picasso.get().load(imageUri).into(detailViewImage)

//                        Toast.makeText(this@CarDetailActivity, "here2", Toast.LENGTH_SHORT).show()

                        // Do something with the data...
                    } else {
                        // Handle the case where the value is null or not of the expected type
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Failed to read value
                // Handle the error here if needed
                Toast.makeText(this@PostDetailActivity, "Error Getting Data", Toast.LENGTH_LONG).show()
            }

        })


        detailViewDeleteBtn.setOnClickListener{
            databaseReference.removeValue()
                .addOnSuccessListener {
                    storageReference.delete()
                        .addOnSuccessListener {
                            var intent = Intent(applicationContext, HomeActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(this@PostDetailActivity, "Deleted Data Successfully", Toast.LENGTH_LONG).show()
                        }
                        .addOnFailureListener{
                            Toast.makeText(this@PostDetailActivity, "Error Deleting Image", Toast.LENGTH_LONG).show()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(this@PostDetailActivity, "Error Deleting Data", Toast.LENGTH_LONG).show()
                }
        }
    }
}