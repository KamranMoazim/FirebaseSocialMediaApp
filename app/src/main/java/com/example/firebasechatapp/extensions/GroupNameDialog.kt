package com.example.firebasechatapp.extensions

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.example.firebasechatapp.R
import android.view.View
import android.widget.EditText

class GroupNameDialog(context: Context, private val onGroupNameEntered: (String) -> Unit) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_group_name)

        val groupNameEditText: EditText = findViewById(R.id.editTextGroupName)
        val confirmButton: View = findViewById(R.id.buttonCreateGroup)

        confirmButton.setOnClickListener {
            val groupName = groupNameEditText.text.toString().trim()
            if (groupName.isNotEmpty()) {
                onGroupNameEntered(groupName)
                dismiss()
            } else {
                // Handle empty group name
                groupNameEditText.error = "Group name cannot be empty"
            }
        }
    }
}
