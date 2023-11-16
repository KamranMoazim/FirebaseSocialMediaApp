package com.example.firebasechatapp.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

object FirebaseUtils {
    private val auth = FirebaseAuth.getInstance()
    private val databaseReference = FirebaseDatabase.getInstance().getReference()
    private val storageReference = FirebaseStorage.getInstance().getReference()
    private val storeReference = FirebaseFirestore.getInstance()



    fun getFirebaseAuthInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    fun getDatabaseReference(): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference()
    }

    fun getStorageReference(): StorageReference {
        return FirebaseStorage.getInstance().getReference()
    }

    fun getStoreReference(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    fun getPostsReferenceForUser(userId: String): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference().child(AppConsts.POSTS_CONSTANT).child(userId)
    }

    fun getPostReference(postKey: String, userId: String): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference().child(AppConsts.POSTS_CONSTANT).child(userId).child(postKey)
    }

    fun getStorageReferenceForPostImage(postKey: String): StorageReference {
        return FirebaseStorage.getInstance().getReference().child(AppConsts.POST_Image_CONSTANT).child("$postKey.jpg")
    }
}