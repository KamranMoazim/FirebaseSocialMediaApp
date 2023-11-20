package com.example.firebasechatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.firebasechatapp.extensions.ForgotPasswordViewModel
import com.example.firebasechatapp.repositories.UserRepository

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private fun showLoader() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        progressBar.visibility = View.GONE
    }

    private var isTimerRunning = false
    private lateinit var userRepository: UserRepository

    private lateinit var editTextEmail: EditText
    private lateinit var buttonSendMail: Button
    private lateinit var buttonMoveToLogin: TextView

    private lateinit var viewModel: ForgotPasswordViewModel


    override fun onStop() {
        super.onStop()
        // Save the timer state when the activity is stopped
        viewModel.secondsRemaining = (viewModel.secondsRemaining / 1000)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)


        progressBar = findViewById(R.id.loadingProgressBarLayout)
        userRepository = UserRepository(this)


        editTextEmail = findViewById(R.id.editTextEmail)
        buttonSendMail = findViewById(R.id.buttonSendMail)
        buttonMoveToLogin = findViewById(R.id.textViewLoginLink)

        viewModel = ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)



        buttonMoveToLogin.setOnClickListener {
            // Move to the RegisterActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


        buttonSendMail.setOnClickListener {
            if (!isTimerRunning) {
                val email = editTextEmail.text.toString().trim()

                if (email.isEmpty()) {
                    editTextEmail.error = "Enter your email"
                    return@setOnClickListener
                }
                forgotPasswordByRepo()
            } else {
                Toast.makeText(this, "Please wait before requesting another reset", Toast.LENGTH_SHORT).show()
            }
        }

        // Restore the timer state if it was running
        if (viewModel.isTimerRunning) {
            startResetTimer(viewModel.secondsRemaining * 1000)
        }

    }

    private fun forgotPasswordByRepo() {
        showLoader() // Show the loader before starting the login process
        userRepository.sendPasswordResetEmail(editTextEmail.text.toString()){
            success, message ->
            run {
                hideLoader()
                if (success) {
                    startResetTimer(60 * 1000)
                    buttonSendMail.isClickable = false
                }
                myToast(message)
            }
        }
//        hideLoader()
//        startResetTimer(60 * 1000)
//        buttonSendMail.isClickable = false
    }

    override fun onBackPressed() {
        // Do nothing or show a message
        // super.onBackPressed() // Uncomment this line if you want to allow back navigation
        if (!isTimerRunning){
            super.onBackPressed()
        } else {
            Toast.makeText(this, "Back navigation is disabled till timer ends", Toast.LENGTH_SHORT).show()
        }
    }


    private fun startResetTimer(l: Long) {

        buttonMoveToLogin.visibility = View.GONE

        object : CountDownTimer(l, 1000) { // 1 minute timer
            override fun onTick(millisUntilFinished: Long) {
                isTimerRunning = true
                val secondsRemaining = millisUntilFinished / 1000
                buttonSendMail.text = "Reset Password ($secondsRemaining seconds)"

                viewModel.isTimerRunning = true
                viewModel.secondsRemaining = secondsRemaining
            }

            override fun onFinish() {

                isTimerRunning = false
                buttonSendMail.text = "Reset Password"

                viewModel.isTimerRunning = false
                viewModel.secondsRemaining = 0

                buttonMoveToLogin.visibility = View.VISIBLE
                buttonSendMail.isClickable = true


            }
        }.start()
    }


    private fun myToast(msg:String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}