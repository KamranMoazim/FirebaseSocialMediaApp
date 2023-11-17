package com.example.firebasechatapp.Adapters

import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.ChatActivity
import com.example.firebasechatapp.PostDetailActivity
import com.example.firebasechatapp.R
import com.example.firebasechatapp.ViewHolders.FriendsViewHolder
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.utils.AppConsts
import com.example.firebasechatapp.utils.MyUtils
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions


class FriendsRecyclerViewAdapter: RecyclerView.Adapter<FriendsViewHolder>() {

    private val usersList = ArrayList<User>()

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.single_friend_view, parent, false)
//        return FriendsViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int, model: User) {
//        Log.d("onBindViewHolder", model.toString())
//        // Bind the data to the PostViewHolder
//        holder.bindData(model)
//
//        // Set button value or handle button clicks here
//        holder.itemView.setOnClickListener {
//            val intent = Intent(holder.itemView.context, PostDetailActivity::class.java)
//            intent.putExtra(AppConsts.POST_KEY_CONSTANT, getRef(position).key)
//            holder.itemView.context.startActivity(intent)
//        }

    //        // Access the button from the layout and set its value or handle clicks
//        holder.itemView.findViewById<ImageButton>(R.id.remove_friend_btn).apply {
//            setOnClickListener {
//                // Handle button click here
//                // You can use 'position' or 'model' to get data associated with this item
//                // showYesNoAlertDialog(holder.itemView)
//            }
//        }
//
//        holder.itemView.findViewById<ImageButton>(R.id.message_friend_btn).apply {
//            setOnClickListener {
//                // Handle button click here
//                // You can use 'position' or 'model' to get data associated with this item
//                // showYesNoAlertDialog(holder.itemView)
//            }
//        }
//    }

    fun updateUserLis(users : List<User>){
        this.usersList.clear()
        this.usersList.addAll(users)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        Log.d("onCreateViewHolder:viewType", viewType.toString())
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_friend_view, parent, false)
        return FriendsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        Log.d("onBindViewHolder:position", position.toString())
        var currentUser = usersList[position]
        holder.bindData(currentUser)


        holder.itemView.findViewById<ImageButton>(R.id.remove_friend_btn).apply {
            setOnClickListener {
                // Handle button click here
                // You can use 'position' or 'model' to get data associated with this item
                // showYesNoAlertDialog(holder.itemView)
            }
        }

        holder.itemView.findViewById<ImageButton>(R.id.message_friend_btn).apply {
            setOnClickListener {
                val intent = Intent(holder.itemView.context, ChatActivity::class.java)
                MyUtils.passUserAsIntent(intent, currentUser)
                holder.itemView.context.startActivity(intent)

            }
        }

    }
}