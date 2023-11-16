package com.example.firebasechatapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.utils.AppConsts
import com.example.firebasechatapp.utils.FirebaseUtils
import com.example.firebasechatapp.utils.SharedPreferencesHelper
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference

@Suppress("DEPRECATION")
class UploadPostActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var savedCredentials:Triple<String?, String?, User?>

    private val REQUEST_CODE_IMAGE = 101
    private lateinit var imageViewAdd:ImageView
    private lateinit var imageInputName:EditText
    private lateinit var textViewProgress:TextView
    private lateinit var progressBar:ProgressBar
    private lateinit var btnUpload:Button

    private lateinit var imageUri:Uri
    private var isImageAdded = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_post)

        sharedPreferencesHelper = SharedPreferencesHelper(this)
        savedCredentials = sharedPreferencesHelper.getSavedCredentials()

        // databaseReference = Firebase.database.getReference().child(AppConsts.POSTS_CONSTANT).child(savedCredentials.third!!.UserId)
        // storageReference = Firebase.storage.getReference().child(AppConsts.POST_Image_CONSTANT)

        databaseReference = FirebaseUtils.getPostsReferenceForUser(savedCredentials.third!!.UserId)
        storageReference = FirebaseUtils.getStorageReference().child(AppConsts.POST_Image_CONSTANT)


        imageViewAdd = findViewById(R.id.imageViewAdd)
        imageInputName = findViewById(R.id.inputImageName)
        textViewProgress = findViewById(R.id.textViewProgressBar)
        progressBar = findViewById(R.id.progressBar)
        btnUpload = findViewById(R.id.btnUpload)


        textViewProgress.visibility = View.GONE
        progressBar.visibility = View.GONE



        imageViewAdd.setOnClickListener {
            var intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(intent, REQUEST_CODE_IMAGE)
        }

        btnUpload.setOnClickListener {
            val imageName = imageInputName.text.toString()
            if (isImageAdded && imageName.isNotEmpty()){
                uploadImage(imageName)
            }
        }

    }

    private fun uploadImage(imageName: String) {
        textViewProgress.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE

        var key = databaseReference.push().key

//        Log.d("UploadCarActivity", "uploadImage: KEY=$key")

        if (key != null) {
            storageReference.child(key + ".jpg").putFile(imageUri)
                .addOnSuccessListener {

//                    Log.d("UploadCarActivity", "uploadImage: KEY1=$key")

                    storageReference.child(key + ".jpg").downloadUrl.addOnSuccessListener (
                        OnSuccessListener { uri ->
                            val hm = HashMap<String, Any>()
                            hm.put(AppConsts.POST_Name_CONSTANT, imageName)
                            hm.put(AppConsts.POST_ImageUri_CONSTANT, uri.toString())

//                            Log.d("UploadCarActivity", "uploadImage: KEY2=$key")

                            databaseReference.child(key).setValue(hm)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Data Saved Successfully", Toast.LENGTH_LONG).show()

                                    val intent = Intent(this, HomeActivity::class.java)
                                    startActivity(intent)
                                }
                        })
                }
                .addOnProgressListener (OnProgressListener { snapshot ->
                    var progress:Int = ((snapshot.bytesTransferred*100.0)/snapshot.totalByteCount).toInt()
                    textViewProgress.text = progress.toString() + "%"
                    progressBar.progress = progress
                })
                .addOnCanceledListener {
                    Toast.makeText(this, "Data could not be Saved", Toast.LENGTH_LONG).show()
                }
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && data != null){
            imageUri = data.data!!
            isImageAdded = true
            imageViewAdd.setImageURI(imageUri)
        }
    }
}