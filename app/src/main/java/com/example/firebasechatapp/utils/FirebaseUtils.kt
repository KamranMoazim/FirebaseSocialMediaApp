package com.example.firebasechatapp.utils

import com.example.firebasechatapp.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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

    fun getChatRoomReference(chatRoomId:String) : DocumentReference {
        return getStoreReference().collection(AppConsts.CHAT_ROOM_CONSTANT).document(chatRoomId)
    }

    fun getChatRoomMessageReference(chatRoomId:String) : CollectionReference {
        return getChatRoomReference(chatRoomId).collection(AppConsts.CHATS_CONSTANT)
    }

    fun allChatRoomCollectionReference() : CollectionReference {
        return getStoreReference().collection(AppConsts.CHAT_ROOM_CONSTANT)
    }

    fun allPostsCollectionReference() : CollectionReference {
        return getStoreReference().collection(AppConsts.POSTS_CONSTANT)
    }

    fun allUsersCollectionReference() : CollectionReference {
        return getStoreReference().collection(AppConsts.USERS_CONSTANT)
    }
    fun searchUsersByUsername(username: String, userIdsToSkip: List<String>, callback: (List<User>) -> Unit) {
        allUsersCollectionReference()
            .whereEqualTo("userName", username)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val users = mutableListOf<User>()

                for (document in querySnapshot.documents) {
                    val user = document.toObject(User::class.java)
                    user?.let { users.add(it) }
                }

//                callback(users)

                // Filter out the users to skip
                val filteredUsers = users.filter { user ->
                    user.UserId !in userIdsToSkip
                }

                // Limit the result to 10 users
                val finalUsers = filteredUsers.take(10)

                callback(finalUsers)

            }
            .addOnFailureListener { exception ->
                // Handle failure
                callback(emptyList())
            }
    }

    fun getTop10UsersByUsername(existingUsers: MutableList<User>, userIdsToSkip: List<String>, callback: (List<User>) -> Unit) {
        allUsersCollectionReference()
            .orderBy("userName")
            .limit(20)  // Fetch more users to account for possible skipped users
            .get()
            .addOnSuccessListener { querySnapshot ->
                val users = mutableListOf<User>()

                for (document in querySnapshot.documents) {
                    val user = document.toObject(User::class.java)
                    user?.let {
                        users.add(it)
                        existingUsers.add(it)  // Add to the existing list
                    }
                }

                // Filter out the users to skip
                val filteredUsers = users.filter { user ->
                    user.UserId !in userIdsToSkip
                }

                // Limit the result to 10 users
                val finalUsers = filteredUsers.take(10)

                callback(finalUsers)
            }
            .addOnFailureListener { exception ->
                // Handle failure
                callback(emptyList())
            }
    }






    fun removeFromFriendsListInFirestore(CurrentUserId: String): Query {
        return allUsersCollectionReference()
            .whereEqualTo("userId", CurrentUserId)
    }


    fun addToFriendsListInFirestore(CurrentUserId: String): Query {
        return allUsersCollectionReference()
            .whereEqualTo("userId", CurrentUserId)
    }


    fun getOtherUserFromChatReference(usersIds:List<String>, currentUserId:String) : DocumentReference {
        if (usersIds.get(0).equals(currentUserId)){
            return allUsersCollectionReference().document(usersIds.get(0))
        } else {
            return allUsersCollectionReference().document(usersIds.get(1))
        }
    }
}