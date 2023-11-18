package com.example.firebasechatapp.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.ChatActivity
import com.example.firebasechatapp.R
import com.example.firebasechatapp.ViewHolders.ChatMessageViewHolder
import com.example.firebasechatapp.ViewHolders.FriendsViewHolder
import com.example.firebasechatapp.ViewHolders.MyChatsViewHolder
import com.example.firebasechatapp.data.ChatMessage
import com.example.firebasechatapp.data.ChatRoom
import com.example.firebasechatapp.data.Post
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.utils.FirebaseUtils
import com.example.firebasechatapp.utils.MyUtils
import com.example.firebasechatapp.utils.SharedPreferencesHelper
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.Query


class MyChatsRecyclerViewAdapter(private val ctx: Context): RecyclerView.Adapter<MyChatsViewHolder>() {

    private var myChatsList = ArrayList<ChatRoom>()
    private var sharedPreferencesHelper: SharedPreferencesHelper
    private var savedCredentials:Triple<String?, String?, User?>


    init {
        sharedPreferencesHelper = SharedPreferencesHelper(ctx)
        savedCredentials = sharedPreferencesHelper.getSavedCredentials()
    }

    fun updateMyChatsLis(myChatsList : List<ChatRoom>){
        this.myChatsList.clear()
        this.myChatsList.addAll(myChatsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyChatsViewHolder {
//        Log.d("onCreateViewHolder:viewType", viewType.toString())
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_recent_chat_view, parent, false)
        return MyChatsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return myChatsList.size
    }

    override fun onBindViewHolder(holder: MyChatsViewHolder, position: Int) {

        var currentChatRoom = myChatsList[position]
//        Log.d("onBindViewHolder", "currentChatRoom: ${currentChatRoom.UserIds}")


        var toFindUserID:String

        if (currentChatRoom.UserIds[0] == savedCredentials.third!!.UserId){
            toFindUserID = currentChatRoom.UserIds[1]
        } else {
            toFindUserID = currentChatRoom.UserIds[0]
        }
        var otherUser:User? = null
//        FirebaseUtils.allUsersCollectionReference()
//            .whereArrayContains("userId", toFindUserID)
//            .get()
//            .addOnCompleteListener { task ->
//
//            }

        FirebaseUtils.allUsersCollectionReference()
            .whereEqualTo("userId", toFindUserID)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val querySnapshot = task.result

                    if (querySnapshot != null && !querySnapshot.isEmpty) {
                        // Assuming the query returns only one document
                        val userDocument = querySnapshot.documents[0]
                        otherUser = userDocument.toObject(User::class.java)!!

                        // Now you can use the otherUser object here
                        Log.d("MyActivity", "Other User: $otherUser")

                        val lastMessageSendByMe = currentChatRoom.LastMessageSenderId == savedCredentials.third!!.UserId

                        if (otherUser != null) {
                            holder.usernameTextView.text = MyUtils.getSubString(otherUser!!.UserName, 8) + "..."
                            if (lastMessageSendByMe) {
//                                holder.lastMessageTextView.text = "You : " + currentChatRoom.LastMessage.subSequence(0, 25).toString() + "..."
                                holder.lastMessageTextView.text = MyUtils.getSubString("You : " + currentChatRoom.LastMessage, 25) + "..."
                            } else {
                                holder.lastMessageTextView.text = currentChatRoom.LastMessage
                            }
                            holder.lastMessageTimeTextView.text = MyUtils.timeStampToString(currentChatRoom.LastMessageTimestamp!!)

                            holder.profileTextView.text = MyUtils.getInitials(otherUser!!.FullName)

                            holder.itemView.setOnClickListener {
                                val intent = Intent(holder.itemView.context, ChatActivity::class.java)
                                MyUtils.passUserAsIntent(intent, otherUser!!)
                                holder.itemView.context.startActivity(intent)
                            }
                        } else {
                            Log.e("MyChatsRecyclerViewAdapter", "Error: Other user is null")
                        }

                    } else {
                        // Handle the case where no user is found
                        Log.e("MyActivity", "No user found with userId: $toFindUserID")
                    }
                } else {
                    // Handle the task failure
                    Log.e("MyActivity", "Error getting user: ${task.exception}")
                }
            }


//        FirebaseUtils.getOtherUserFromChatReference(currentChatRoom.UserIds, savedCredentials.third!!.UserId)
//            .get()
//            .addOnCompleteListener { task ->
//                if (task != null && task.isSuccessful) {
//                    val lastMessageSendByMe = currentChatRoom.LastMessageSenderId == savedCredentials.third!!.UserId
//
//                    val otherUser = task.getResult().toObject(User::class.java)
//                    Log.d("MyChatsRecyclerViewAdapter", "Other User: $otherUser")
//
//                    if (otherUser != null) {
//                        holder.usernameTextView.text = otherUser.UserName
//                        if (lastMessageSendByMe) {
//                            holder.lastMessageTextView.text = "You : " + currentChatRoom.LastMessage
//                        } else {
//                            holder.lastMessageTextView.text = currentChatRoom.LastMessage
//                        }
//                        holder.lastMessageTimeTextView.text = MyUtils.timeStampToString(currentChatRoom.LastMessageTimestamp!!)
//
//                        holder.itemView.setOnClickListener {
//                            val intent = Intent(holder.itemView.context, ChatActivity::class.java)
//                            MyUtils.passUserAsIntent(intent, otherUser)
//                            holder.itemView.context.startActivity(intent)
//                        }
//                    } else {
//                        Log.e("MyChatsRecyclerViewAdapter", "Error: Other user is null")
//                    }
//                } else {
//                    Log.e("MyChatsRecyclerViewAdapter", "Error getting other user: ${task.exception}")
//                }
//            }

    }
}