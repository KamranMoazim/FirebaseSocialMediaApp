package com.example.firebasechatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.example.firebasechatapp.Adapters.ChatMessageRecyclerViewAdapter
import com.example.firebasechatapp.Adapters.PostRecyclerViewAdapter
import com.example.firebasechatapp.data.ChatMessage
import com.example.firebasechatapp.data.ChatRoom
import com.example.firebasechatapp.data.Post
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.utils.AppConsts
import com.example.firebasechatapp.utils.FirebaseUtils
import com.example.firebasechatapp.utils.MyUtils
import com.example.firebasechatapp.utils.SharedPreferencesHelper
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query


class ChatActivity : AppCompatActivity() {

    private lateinit var chatroomId:String
    private lateinit var otherUser:User
    private var chatRoom:ChatRoom? = null

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var savedCredentials:Triple<String?, String?, User?>
    private lateinit var adapter:ChatMessageRecyclerViewAdapter

    private lateinit var messageInput:EditText
    private lateinit var sendMessageBtn:ImageButton
    private lateinit var backBtn:ImageButton
    private lateinit var chatImageText:TextView
    private lateinit var chatUserName:TextView
    private lateinit var chatRecyclerView: RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        sharedPreferencesHelper = SharedPreferencesHelper(this)
        savedCredentials = sharedPreferencesHelper.getSavedCredentials()

        otherUser = MyUtils.getUserFromIntent(intent)

        messageInput = findViewById(R.id.actual_message_box)
        sendMessageBtn = findViewById(R.id.message_send_btn)
        chatRecyclerView = findViewById(R.id.all_messages_recycler_view)

        backBtn = findViewById(R.id.go_back)
        chatImageText = findViewById(R.id.chat_imageText)
        chatUserName = findViewById(R.id.chat_username)

        chatroomId = MyUtils.getChatRoomId(savedCredentials.third!!.UserId, otherUser.UserId)

        chatImageText.text = MyUtils.getInitials(otherUser.FullName)
        chatUserName.text = otherUser.UserName

        backBtn.setOnClickListener { onBackPressed() }

        sendMessageBtn.setOnClickListener{v ->
            run {
                var message = messageInput.text.toString()
                if (message.isEmpty()) {
                    return@run
                } else {
                    sendMessageToUser(message)
                }
            }
        }




        getOrCreateChatRoom()
        setupChatRecyclerView()
    }

    private fun setupChatRecyclerView() {
        var query = FirebaseUtils.getChatRoomMessageReference(chatroomId)
            .orderBy("messageTimestamp", Query.Direction.DESCENDING)

        var options =
            FirestoreRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage::class.java).build()

        var manager = LinearLayoutManager(this)
        manager.reverseLayout = true
        chatRecyclerView.layoutManager = manager

        adapter = ChatMessageRecyclerViewAdapter(options, this)
        chatRecyclerView.adapter = adapter
        adapter.startListening()

        val dataObserver = object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                // Handle data set changes (e.g., notifyDataSetChanged())
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                chatRecyclerView.smoothScrollToPosition(0)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                // Handle item removals
            }

            // Other methods for additional data change events
        }
        adapter.registerAdapterDataObserver(dataObserver)
    }

    private fun sendMessageToUser(message: String) {

        chatRoom!!.LastMessageTimestamp = Timestamp.now()
        chatRoom!!.LastMessageSenderId = savedCredentials.third!!.UserId
        chatRoom!!.LastMessage = message
        FirebaseUtils.getChatRoomReference(chatroomId).set(chatRoom!!)

        var chatMessage = ChatMessage(message, savedCredentials.third!!.UserId, Timestamp.now())
        FirebaseUtils.getChatRoomMessageReference(chatroomId).add(chatMessage)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    messageInput.setText("")
                }
            }
    }


    fun getOrCreateChatRoom(){
        FirebaseUtils.getChatRoomReference(chatroomId).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    chatRoom = task.getResult().toObject(ChatRoom::class.java)

                    if (chatRoom == null){
                        // first time chat
                        chatRoom = ChatRoom(
                            ChatRoomId = chatroomId,
                            LastMessageSenderId = "",
                            LastMessage = "",
                            LastMessageTimestamp = Timestamp.now(),
                            listOf(savedCredentials.third!!.UserId, otherUser.UserId)
                        )
                        FirebaseUtils.getChatRoomReference(chatroomId).set(chatRoom!!)
                    } else {

                    }

                } else {

                }
            }

    }
}