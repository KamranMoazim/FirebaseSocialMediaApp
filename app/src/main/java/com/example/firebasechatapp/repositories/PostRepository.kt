package com.example.firebasechatapp.repositories

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.firebasechatapp.ProfileActivity
import com.example.firebasechatapp.data.Post
import com.example.firebasechatapp.utils.AppConsts
import com.example.firebasechatapp.utils.FirebaseUtils
import com.example.firebasechatapp.utils.SharedPreferencesHelper
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.StorageReference
import kotlin.reflect.typeOf

class PostRepository (
    private val context: Context
) {

    private val firestoreReference: CollectionReference = FirebaseUtils.allPostsCollectionReference()
    private val sharedPreferencesHelper: SharedPreferencesHelper = SharedPreferencesHelper(this.context)
    private lateinit var storageReference: StorageReference



    fun getPostsForUser(searchText: String): FirestoreRecyclerOptions<Post> {
//        var myFriendsIds = sharedPreferencesHelper.getSavedCredentials().third!!.MyFriendsIds

        var myFriendsIds = mutableListOf<String>()
        myFriendsIds.addAll(sharedPreferencesHelper.getSavedCredentials().third!!.MyFriendsIds)
//         myFriendsIds.add(sharedPreferencesHelper.getSavedCredentials().third!!.UserId)

        val query = firestoreReference
             .whereIn(AppConsts.POST_AuthorId_CONSTANT, myFriendsIds)
             .orderBy(AppConsts.POST_Name_CONSTANT)
             .startAt(searchText)
             .endAt(searchText + "\uf8ff")

        val options = FirestoreRecyclerOptions.Builder<Post>()
            .setQuery(query, Post::class.java)
            .build()

        return options
    }



    fun deleteUsersPost(
        postId: String,
        callback: (Boolean, String) -> Unit
    ):Boolean {

        storageReference = FirebaseUtils.getStorageReferenceForPostImage(postId)

        firestoreReference.document(postId).delete()
            .addOnSuccessListener {
                storageReference.delete()
                    .addOnSuccessListener {
                        callback(true, "Deleted Data Successfully")
                    }
                    .addOnFailureListener {
                        callback(false, "Error Deleting Image")
                    }
            }
            .addOnFailureListener {
                callback(false, "Error Deleting Data")
            }

        return false
    }

}