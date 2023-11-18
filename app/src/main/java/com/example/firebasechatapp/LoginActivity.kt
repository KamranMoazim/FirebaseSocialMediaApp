package com.example.firebasechatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.utils.AppConsts
import com.example.firebasechatapp.utils.FirebaseUtils
import com.example.firebasechatapp.utils.SharedPreferencesHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.CollectionReference

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firestoreReference: CollectionReference

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var savedCredentials:Triple<String?, String?, User?>

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonMoveToRegister:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // auth = FirebaseAuth.getInstance()
        // databaseReference = Firebase.database.getReference().child(AppConsts.USERS_CONSTANT)
        auth = FirebaseUtils.getFirebaseAuthInstance()
        databaseReference = FirebaseUtils.getDatabaseReference().child(AppConsts.USERS_CONSTANT)
        firestoreReference = FirebaseUtils.allUsersCollectionReference()

        sharedPreferencesHelper = SharedPreferencesHelper(this)
        savedCredentials = sharedPreferencesHelper.getSavedCredentials()



        if (savedCredentials.first?.isNotEmpty() == true) {
            startHomeActivity()
        }

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonMoveToRegister = findViewById(R.id.textViewRegisterLink)

        buttonLogin.setOnClickListener {
            loginUser()
        }
        buttonMoveToRegister.setOnClickListener {
            // Move to the RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        // Check for empty fields
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            return
        }

        // Firebase Authentication to sign in the user with email and password
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login success

                    val uid = FirebaseAuth.getInstance().currentUser?.uid

//                    fetchUserDetails(uid) { user ->
//                        if (user != null) {
//                            sharedPreferencesHelper.saveCredentials(
//                                password = password,
//                                email = email,
//                                username = user.UserName,
//                                fullname = user.FullName,
//                                about = user.About,
//                                age = user.Age,
//                                userId = user.UserId
//                            )
//
//                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
//
//                            startHomeActivity()
//                        } else {
//                            // Handle the case where user details are not available
//                            Toast.makeText(this, "Error fetching user details", Toast.LENGTH_SHORT).show()
//                        }
//                    }

                    fetchUserDetailsUsingCollections(uid) { user ->
                        if (user != null) {
                            sharedPreferencesHelper.saveCredentials(
                                password = password,
                                email = email,
                                username = user.UserName,
                                fullname = user.FullName,
                                about = user.About,
                                age = user.Age,
                                userId = user.UserId,
                                friendsIDs = user.MyFriendsIds
                            )

                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                            startHomeActivity()
                        } else {
                            // Handle the case where user details are not available
                            Toast.makeText(this, "Error fetching user details", Toast.LENGTH_SHORT).show()
                        }
                    }


                } else {
                    // If login fails, display a message to the user.
                    Toast.makeText(
                        this,
                        "Login failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun startHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun fetchUserDetails(uid: String?, callback: (User?) -> Unit) {
        // Your logic to fetch user details using the uid
        // Replace this with your actual database or API call
        // For simplicity, I'm using a placeholder User object
//        val user = User(UserName = "john_doe", FullName = "John Doe", Age = 25)
//        callback(user)

        uid?.let { userId ->
            databaseReference.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    callback(user)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                    callback(null)
                }
            })
        } ?: run {
            callback(null)
        }
    }

    private fun fetchUserDetailsUsingCollections(uid: String?, callback: (User?) -> Unit) {
        uid?.let { userId ->
//            firestoreReference
//                .document(userId)
//                .get()
//                .addOnSuccessListener { documentSnapshot ->
//                    val user = documentSnapshot.toObject(User::class.java)
//                    callback(user)
//                }
//                .addOnFailureListener { exception ->
//                    // Handle error
//                    callback(null)
//                }
            firestoreReference
                .whereEqualTo("userId", userId)  // Replace "userId" with the actual field name in your document
                .get()
                .addOnSuccessListener { querySnapshot ->
                    // This block is executed if the query is successful

                    if (!querySnapshot.isEmpty) {
                        // Assuming there is only one user with the given userId (unique userId)

                        val user = querySnapshot.documents[0].toObject(User::class.java)
                        callback(user)
                    } else {
                        // Handle the case where no user with the specified userId is found
                        callback(null)
                    }
                }
                .addOnFailureListener { exception ->
                    // This block is executed if there's an error during the query
                    // Handle error
                    callback(null)
                }

        } ?: run {
            callback(null)
        }
    }

}