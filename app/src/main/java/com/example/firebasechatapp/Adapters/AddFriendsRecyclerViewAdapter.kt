package com.example.firebasechatapp.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.R
import com.example.firebasechatapp.ViewHolders.AddFriendsViewHolder
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.utils.FirebaseUtils
import com.example.firebasechatapp.utils.FriendsDatabaseHelper
import com.example.firebasechatapp.utils.SharedPreferencesHelper

class AddFriendsRecyclerViewAdapter(private val ctx: Context): RecyclerView.Adapter<AddFriendsViewHolder>()  {

    private var addNewFriendsList = ArrayList<User>()
    private var sharedPreferencesHelper: SharedPreferencesHelper
    private var savedCredentials:Triple<String?, String?, User?>


    init {
        sharedPreferencesHelper = SharedPreferencesHelper(ctx)
        savedCredentials = sharedPreferencesHelper.getSavedCredentials()
    }

    fun updateAddNewFriendsList(addNewFriendsList : List<User>){
        this.addNewFriendsList.clear()
        this.addNewFriendsList.addAll(addNewFriendsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddFriendsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_search_friend_view, parent, false)
        return AddFriendsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return addNewFriendsList.size
    }

    override fun onBindViewHolder(holder: AddFriendsViewHolder, position: Int) {
        var addNewFriend = addNewFriendsList[position]
        holder.bindData(addNewFriend)

        holder.addFriendBtn.setOnClickListener {
            FirebaseUtils.addToFriendsListInFirestore(savedCredentials.third!!.UserId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val userDocument = querySnapshot.documents[0]
                        val currentFriendsIds = userDocument["myFriendsIds"] as List<String>

                        // Remove the friendToRemoveId from the currentFriendsIds list
                        val updatedFriendsIds = currentFriendsIds.toMutableList().apply {
                            add(addNewFriend.UserId)
                        }

                        // Update the myFriendsIds field in Firestore with the updated list
                        userDocument.reference.update("myFriendsIds", updatedFriendsIds)
                            .addOnSuccessListener {
                                // Successfully updated friends list in Firestore
                                val friendsDatabaseHelper = FriendsDatabaseHelper(this.ctx)
                                friendsDatabaseHelper.addFriend(addNewFriend.UserId)
                                Toast.makeText(this.ctx, "Added Successfully to your Friend List", Toast.LENGTH_LONG).show()
                                addNewFriendsList.removeAt(position)
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
}