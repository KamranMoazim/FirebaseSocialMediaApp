package com.example.firebasechatapp.Adapters

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import com.example.firebasechatapp.PostDetailActivity
import com.example.firebasechatapp.R
import com.example.firebasechatapp.ViewHolders.FriendsViewHolder
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.utils.AppConsts
import com.example.firebasechatapp.utils.MyUtils
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class FriendsRecyclerViewAdapter(options: FirebaseRecyclerOptions<User>) :
    FirebaseRecyclerAdapter<User, FriendsViewHolder>(options) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_friend_view, parent, false)
        return FriendsViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int, model: User) {
        // Bind the data to the PostViewHolder
        holder.bindData(model)

        // Set button value or handle button clicks here
//        holder.itemView.setOnClickListener {
//            val intent = Intent(holder.itemView.context, PostDetailActivity::class.java)
//            intent.putExtra(AppConsts.POST_KEY_CONSTANT, getRef(position).key)
//            holder.itemView.context.startActivity(intent)
//        }

        // Access the button from the layout and set its value or handle clicks
        holder.itemView.findViewById<ImageButton>(R.id.remove_friend_btn).apply {
            setOnClickListener {
                // Handle button click here
                // You can use 'position' or 'model' to get data associated with this item
            }
        }

        holder.itemView.findViewById<ImageButton>(R.id.message_friend_btn).apply {
            setOnClickListener {
                // Handle button click here
                // You can use 'position' or 'model' to get data associated with this item
                showYesNoAlertDialog()
            }
        }
    }

    private fun showYesNoAlertDialog() {
        val builder = AlertDialog.Builder(FriendsViewHolder.itemView.context)

        // Set the title for the AlertDialog
        builder.setTitle("Confirmation")

        // Set the message for the AlertDialog
        builder.setMessage("Do you want to proceed?")

        // Set a positive button and its click listener
        builder.setPositiveButton("Yes") { dialog, which ->
            // Handle the "Yes" button click
            MyUtils.showToast("You clicked Yes", FriendsViewHolder.itemView.context)
            dialog.dismiss() // Close the dialog
        }

        // Set a negative button and its click listener
        builder.setNegativeButton("No") { dialog, which ->
            // Handle the "No" button click
            MyUtils.showToast("You clicked No")
            dialog.dismiss() // Close the dialog
        }

        // Create and show the AlertDialog
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}