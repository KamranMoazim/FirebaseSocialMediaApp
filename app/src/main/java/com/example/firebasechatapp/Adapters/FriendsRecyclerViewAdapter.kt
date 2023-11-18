package com.example.firebasechatapp.Adapters

import android.app.AlertDialog
import android.content.Context
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
import com.example.firebasechatapp.utils.FirebaseUtils
import com.example.firebasechatapp.utils.FriendsDatabaseHelper
import com.example.firebasechatapp.utils.MyUtils
import com.example.firebasechatapp.utils.SharedPreferencesHelper
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions


class FriendsRecyclerViewAdapter(ctx: Context): RecyclerView.Adapter<FriendsViewHolder>() {

    private var sharedPreferencesHelper: SharedPreferencesHelper = SharedPreferencesHelper(ctx)
    private var savedCredentials:Triple<String?, String?, User?> = sharedPreferencesHelper.getSavedCredentials()
    private val usersList = ArrayList<User>()

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

        var friendToRemoveId = currentUser.UserId


        holder.itemView.findViewById<ImageButton>(R.id.remove_friend_btn).apply {
            setOnClickListener {
                // Handle button click here
                // You can use 'position' or 'model' to get data associated with this item
                // showYesNoAlertDialog(holder.itemView)
                FirebaseUtils.removeFromFriendsListInFirestore(savedCredentials.third!!.UserId)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty) {
                            val userDocument = querySnapshot.documents[0]
                            val currentFriendsIds = userDocument["myFriendsIds"] as List<String>

                            // Remove the friendToRemoveId from the currentFriendsIds list
                            val updatedFriendsIds = currentFriendsIds.toMutableList().apply {
                                remove(friendToRemoveId)
                            }

                            // Update the myFriendsIds field in Firestore with the updated list
                            userDocument.reference.update("myFriendsIds", updatedFriendsIds)
                                .addOnSuccessListener {
                                    // Successfully updated friends list in Firestore
                                    val friendsDatabaseHelper = FriendsDatabaseHelper(this.context)
                                    friendsDatabaseHelper.removeFriend(friendToRemoveId)
                                    Toast.makeText(this.context, "Removed Successfully from your Friend List", Toast.LENGTH_LONG).show()
                                    usersList.removeAt(position)
                                    notifyDataSetChanged()
                                }
                                .addOnFailureListener { e ->
                                    // Handle the failure to update friends list
                                    Log.e("User", "Error updating friends list: $e")
                                }
                        } else {
                            // Handle the case where no user with the specified userId is found
                            Log.e("User", "Error: User not found for update.")
                        }

                    }
                    .addOnFailureListener { exception ->
                        // Handle the failure to query the user document
                        Log.e("User", "Error querying user document: $exception")
                    }
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