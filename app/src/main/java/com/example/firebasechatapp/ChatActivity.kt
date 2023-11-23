package com.example.firebasechatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.Adapters.ChatMessageRecyclerViewAdapter
import com.example.firebasechatapp.data.ChatRoom
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.repositories.ChatMessageRepository
import com.example.firebasechatapp.repositories.ChatRoomRepository
import com.example.firebasechatapp.repositories.UserRepository
import com.example.firebasechatapp.utils.MyUtils
import com.example.firebasechatapp.utils.SharedPreferencesHelper


class ChatActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private fun showLoader() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        progressBar.visibility = View.GONE
    }

    private lateinit var userRepository: UserRepository
    private lateinit var chatRoomRepository: ChatRoomRepository
    private lateinit var chatMessageRepository: ChatMessageRepository

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

        progressBar = findViewById(R.id.loadingProgressBarLayout)

        userRepository = UserRepository(this)
        chatRoomRepository = ChatRoomRepository(this)
        chatMessageRepository = ChatMessageRepository(this)


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
        showLoader()

        var manager = LinearLayoutManager(this)
        manager.reverseLayout = true
        chatRecyclerView.layoutManager = manager

//        adapter = ChatMessageRecyclerViewAdapter(options, this)
        adapter = ChatMessageRecyclerViewAdapter(chatMessageRepository.getAllReentChatMessagesQuery(chatroomId), this)
        chatRecyclerView.adapter = adapter
        adapter.startListening()

        val dataObserver = object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                chatRecyclerView.smoothScrollToPosition(0)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            }

            // Other methods for additional data change events
        }
        adapter.registerAdapterDataObserver(dataObserver)

        hideLoader()
    }

    private fun sendMessageToUser(message: String) {

        chatMessageRepository.sendMessageToUser(chatRoom!!, message, chatroomId){
            success, message ->
                run {
                    if (success){
                        messageInput.setText("")
                    } else {
                        myToast(message)
                    }
                }
        }
    }


    private fun getOrCreateChatRoom(){

        chatRoomRepository.getOrCreateChatRoom(
                savedCredentials.third!!.UserId,
                otherUser.UserId,
                chatroomId){
                    success, message, receivedChatRoom ->
                        run {
//                            myToast(message)
                            if (success){
                                chatRoom = receivedChatRoom
                            }
                        }
                }

    }

    private fun myToast(msg:String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}