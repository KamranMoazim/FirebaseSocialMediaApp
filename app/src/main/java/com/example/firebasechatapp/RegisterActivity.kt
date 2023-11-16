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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonRegister: Button
    private lateinit var editTextUsername: EditText
    private lateinit var editTextFullName: EditText
    private lateinit var editTextAbout: EditText
    private lateinit var editTextAge: EditText
    private lateinit var buttonMoveToLogin:TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // auth = FirebaseAuth.getInstance()
        // databaseReference = Firebase.database.getReference().child(AppConsts.USERS_CONSTANT)
        auth = FirebaseUtils.getFirebaseAuthInstance()
        databaseReference = FirebaseUtils.getDatabaseReference().child(AppConsts.USERS_CONSTANT)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonRegister = findViewById(R.id.buttonRegister)
        buttonMoveToLogin = findViewById(R.id.textViewLoginLink)
        editTextUsername = findViewById(R.id.editTextUsername)
        editTextFullName = findViewById(R.id.editTextFullName)
        editTextAbout = findViewById(R.id.editTextAbout)
        editTextAge = findViewById(R.id.editTextAge)


        buttonRegister.setOnClickListener {
            registerUser()
        }

        buttonMoveToLogin.setOnClickListener {
            // Move to the RegisterActivity
            goToLoginActivity()
        }
    }

    private fun goToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun registerUser() {
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()
        val username = editTextUsername.text.toString()
        val fullName = editTextFullName.text.toString()
        val about = editTextAbout.text.toString()
        val age = editTextAge.text.toString().toIntOrNull() ?: 0  // Convert to Int, default to 0 if not a valid number

        // Check for empty fields
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            return
        }

        // Firebase Authentication to register a new user with email and password
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    // Registration successful, get the UID
                    val uid = FirebaseAuth.getInstance().currentUser?.uid

                    // Save additional details to the database
                    if (uid != null) {
                        val userRef = databaseReference.child(uid)
                        val userData = User(UserName = username, FullName = fullName, About = about, Age = age, UserId = uid)

                        userRef.setValue(userData)
                            .addOnCompleteListener { databaseTask ->
                                if (databaseTask.isSuccessful) {
                                    // User details saved to the database
                                    goToLoginActivity()
                                    finish()
                                } else {
                                    // Handle database error
                                }
                            }
                    }

                    // Registration success
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                } else {
                    // If registration fails, display a message to the user.
                    Toast.makeText(
                        this,
                        "Registration failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}