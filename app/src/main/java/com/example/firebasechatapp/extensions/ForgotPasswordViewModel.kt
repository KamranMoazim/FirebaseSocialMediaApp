package com.example.firebasechatapp.extensions

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel

class ForgotPasswordViewModel: ViewModel() {
    var isTimerRunning: Boolean = false
    var secondsRemaining: Long = 0
}