package com.example.firebasechatapp.utils

import android.content.Context
import android.widget.Toast

object MyUtils {

    fun getInitials(fullName: String): String {
        return fullName.split(" ").joinToString("") { it.first().toString() }
    }

    fun showToast(message: String, ctx: Context) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
    }

}

