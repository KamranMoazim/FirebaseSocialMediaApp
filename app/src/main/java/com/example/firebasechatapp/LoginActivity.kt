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
import com.example.firebasechatapp.utils.SharedPreferencesHelper



class LoginActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private fun showLoader() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        progressBar.visibility = View.GONE
    }




    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var savedCredentials:Triple<String?, String?, User?>

    private lateinit var userRepository: UserRepository

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonMoveToRegister:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progressBar = findViewById(R.id.loadingProgressBarLayout)

        userRepository = UserRepository(this)

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
            showLoader() // Show the loader before starting the login process
            loginUserByRepo()
        }
        buttonMoveToRegister.setOnClickListener {
            // Move to the RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUserByRepo(){
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        // Check for empty fields
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            return
        }

        userRepository.loginUser(email, password){
                success, message ->
                run {
                    hideLoader() // Show the loader before starting the login process
                    myToast(message)
                    if (success) {
                        startHomeActivity()
                        finish()
                    }
                }
            }

    }


    private fun startHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun myToast(msg:String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}