package com.example.firebasechatapp.repositories

// User Repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.utils.FirebaseUtils
import com.example.firebasechatapp.utils.SharedPreferencesHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference

class UserRepository(
    private val context: Context
) {

    private val auth: FirebaseAuth = FirebaseUtils.getFirebaseAuthInstance()
    private val firestoreReference: CollectionReference = FirebaseUtils.allUsersCollectionReference()
    private val sharedPreferencesHelper: SharedPreferencesHelper = SharedPreferencesHelper(this.context)



    fun sendPasswordResetEmail(email: String, callback: (Boolean, String) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Password reset email sent successfully
                    callback(true, "Password reset email sent successfully")
                } else {
                    // If the email address is not registered or other errors occur
                    callback(false, "Failed to send password reset email: ${task.exception?.message}")
                }
            }
    }

    fun registerUser(
        email: String,
        password: String,
        username: String,
        fullName: String,
        about: String,
        age: Int,
        callback: (Boolean, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registration successful, get the UID
                    val uid = FirebaseAuth.getInstance().currentUser?.uid

                    // Save additional details to the database
                    if (uid != null) {
                        val userData = User(
                            UserName = username,
                            FullName = fullName,
                            About = about,
                            Age = age,
                            UserId = uid,
                            MyFriendsIds = listOf()
                        )

                        firestoreReference.add(userData).addOnCompleteListener { dbTask ->
                            if (dbTask.isSuccessful) {
                                // User details saved to the database
//                                showToast("Registration successful")
                                callback(task.isSuccessful, "Registration successful")
                            } else {
                                // Handle database error
//                                showToast("Registration failed: Please Try Again Later")
                                callback(false, "Registration failed: Please Try Again Later")
                            }
                        }
                    }
                } else {
                    // If registration fails, display a message to the user.
//                    showToast("Registration failed: ${task.exception?.message}")
                    callback(false, "Registration failed: ${task.exception?.message}")
                }
            }
    }


    fun loginUser(email: String, password: String, callback: (Boolean, String) -> Unit) {
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login success
                    val uid = auth.currentUser?.uid
                    if (uid != null) {
                        fetchUserDetailsUsingCollections(uid) { user ->
                            if (user != null) {
                                sharedPreferencesHelper.saveCredentials(user.UserId, email, password, user.UserName, user.FullName, user.About, user.Age, user.MyFriendsIds)
//                                showToast("Login successful")
                                callback(true, "Login successful")

                            } else {
//                                showToast("Error fetching user details")
                                callback(false, "Error fetching user details")
                            }
                        }
                    }
                } else {
                    // If login fails, display a message to the user.
                    showToast("Login failed: ${task.exception?.message}")
                    callback(false, "Login failed: ${task.exception?.message}")
                }
            }
    }

    private fun fetchUserDetailsUsingCollections(uid: String, callback: (User?) -> Unit) {
        firestoreReference
            .whereEqualTo("userId", uid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val user = querySnapshot.documents[0].toObject(User::class.java)
                    callback(user)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }


    fun fetchMyFriends(
        callback: (Boolean, String, List<User>) -> Unit
    ) {

        var me = sharedPreferencesHelper.getSavedCredentials().third
        var users = mutableListOf<User>()

        firestoreReference
            .whereIn("userId", me!!.MyFriendsIds)
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Check if there is data in the snapshot
                if (!querySnapshot.isEmpty) {
                    // Data exists, log or process it
                    for (documentSnapshot in querySnapshot.documents) {
                        val user = documentSnapshot.toObject(User::class.java)
                        // Log or process each user
//                        Log.d("UserData", "User: $user")
                        if (user?.UserId != me.UserId) {
//                            users.add(user!!)
                            (users as MutableList<User>).add(user!!)
                            callback(true, "Fetched Your Friends", users)
                        }
                    }
//                    adapter.updateUserLis(users)
                } else {
                    // No data found
//                    Log.d("UserData", "No users found")
                    callback(false, "No users found", users)
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors here
//                Log.e("UserData", "Error getting users: ${exception.message}")
                callback(false, "Error getting users: ${exception.message}", users)
            }
    }




    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
