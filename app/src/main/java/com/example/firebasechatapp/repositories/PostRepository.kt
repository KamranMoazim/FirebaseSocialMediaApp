package com.example.firebasechatapp.repositories

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.firebasechatapp.ProfileActivity
import com.example.firebasechatapp.data.Comment
import com.example.firebasechatapp.data.Post
import com.example.firebasechatapp.utils.AppConsts
import com.example.firebasechatapp.utils.FirebaseUtils
import com.example.firebasechatapp.utils.SharedPreferencesHelper
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.storage.StorageReference
import kotlin.reflect.typeOf

class PostRepository (
    private val context: Context
) {

    private val firestoreReference: CollectionReference = FirebaseUtils.allPostsCollectionReference()
    private val sharedPreferencesHelper: SharedPreferencesHelper = SharedPreferencesHelper(this.context)
    private lateinit var storageReference: StorageReference


    fun likePost(postId: String, userId: String, callback: (Boolean, String) -> Unit) {
        // Get a reference to the post document
        val postDocument = firestoreReference.document(postId)

        // Atomically update the PeopleLikedPost field
        postDocument.update("peopleLikedPost", FieldValue.arrayUnion(userId))
            .addOnSuccessListener {
                callback(true, "Post liked successfully")
            }
            .addOnFailureListener { e ->
                callback(false, "Error liking post: $e")
            }
    }

    fun removeLike(postId: String, userId: String, callback: (Boolean, String) -> Unit) {
        // Get a reference to the post document
        val postDocument = firestoreReference.document(postId)

        // Atomically update the PeopleLikedPost field
        postDocument.update("PeopleLikedPost", FieldValue.arrayRemove(userId))
            .addOnSuccessListener {
                callback(true, "Like removed successfully")
            }
            .addOnFailureListener { e ->
                callback(false, "Error removing like: $e")
            }
    }



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


    fun addCommentToPost(
        postId: String,
        comment: Comment,
        callback: (Boolean, String) -> Unit
    ):Boolean {

//        comment.CommentedAt = Timestamp.now()

        firestoreReference.document(postId).collection(AppConsts.COMMENTS_CONSTANT).add(comment)
            .addOnSuccessListener { documentReference ->
                // Comment added successfully
                println("Comment added with ID: ${documentReference.id}")
//                callback(true, "Comment added with ID: ${documentReference.id}")
                callback(true, "Comment added Successfully")
            }
            .addOnFailureListener { e ->
                // Handle errors
                println("Error adding comment: $e")
                callback(false, "Error adding comment: $e")
            }

        return false
    }


    fun addCommentToPostNew(
        postId: String,
        comment: Comment,
        callback: (Boolean, String) -> Unit
    ) {
        val commentsCollection = firestoreReference.document(postId).collection(AppConsts.COMMENTS_CONSTANT)

        // Add the comment with an automatically generated document ID
        commentsCollection.add(comment)
            .addOnSuccessListener { documentReference ->
                // Comment added successfully
                val commentId = documentReference.id
                comment.CommentId = commentId // Set the commentId in the Comment object
                println("Comment added with ID: $commentId")

                // Update the comment in Firestore with the assigned commentId
                commentsCollection.document(commentId)
                    .set(comment)
                    .addOnSuccessListener {
                        callback(true, "Comment added successfully")
                    }
                    .addOnFailureListener { e ->
                        callback(false, "Error updating commentId in Firestore: $e")
                    }
            }
            .addOnFailureListener { e ->
                // Handle errors
                callback(false, "Error adding comment: $e")
            }
    }



    fun deleteComment(postId: String, commentId: String, callback: (Boolean, String) -> Unit) {
        val commentsCollection = firestoreReference.document(postId).collection(AppConsts.COMMENTS_CONSTANT)

        // Use the document ID of the comment to delete it
        commentsCollection.document(commentId)
            .delete()
            .addOnSuccessListener {
                // Comment deleted successfully
                callback(true, "Comment deleted successfully")
            }
            .addOnFailureListener { e ->
                // Handle errors
                callback(false, "Error deleting comment: $e")
            }
    }


    fun getCommentsForPost(
        postId: String,
        callback: (Boolean, String, List<Comment>?) -> Unit
    ) {
        // Reference to the comments subcollection under the specific post document
        val commentsCollectionReference = firestoreReference.document(postId).collection(AppConsts.COMMENTS_CONSTANT)

        // Construct a query to retrieve comments
        commentsCollectionReference
            .orderBy("commentedAt")
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Process the comments
                val commentsList = mutableListOf<Comment>()
                for (document in querySnapshot.documents) {
                    val commentData = document.toObject(Comment::class.java)
                    commentData?.let { commentsList.add(it) }
                }
                callback(true, "Retrieved Post Comments", commentsList)
            }
            .addOnFailureListener { e ->
                // Handle errors
                callback(false, "Error getting comments: $e", null)
            }
    }


}