package com.example.firebasechatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.repositories.UserRepository
import com.example.firebasechatapp.utils.AppConsts
import com.example.firebasechatapp.utils.FirebaseUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.CollectionReference

class RegisterActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private fun showLoader() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        progressBar.visibility = View.GONE
    }


    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonRegister: Button
    private lateinit var editTextUsername: EditText
    private lateinit var editTextFullName: EditText
    private lateinit var editTextAbout: EditText
    private lateinit var editTextAge: EditText
    private lateinit var buttonMoveToLogin:TextView

    private lateinit var userRepository: UserRepository




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        progressBar = findViewById(R.id.loadingProgressBarLayout)

        userRepository = UserRepository(this)


        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonRegister = findViewById(R.id.buttonRegister)
        buttonMoveToLogin = findViewById(R.id.textViewLoginLink)
        editTextUsername = findViewById(R.id.editTextUsername)
        editTextFullName = findViewById(R.id.editTextFullName)
        editTextAbout = findViewById(R.id.editTextAbout)
        editTextAge = findViewById(R.id.editTextAge)


        buttonRegister.setOnClickListener {
            showLoader()
            registerUserByRepo()
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

    private fun registerUserByRepo(){
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()
        val username = editTextUsername.text.toString()
        val fullName = editTextFullName.text.toString()
        val about = editTextAbout.text.toString()
        val age = editTextAge.text.toString().toIntOrNull() ?: 0  // Convert to Int, default to 0 if not a valid number

        // Check for empty fields
        if (email.isEmpty() || password.isEmpty()) {
            myToast("Please enter both email and password")
            return
        }

        userRepository.registerUser(email, password, username, fullName, about, age){
                success, message ->
                    run {
                        hideLoader()
                        myToast(message)
                        if (success) {
                            goToLoginActivity()
                            finish()
                        }
                    }
                }
    }

    private fun myToast(msg:String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}