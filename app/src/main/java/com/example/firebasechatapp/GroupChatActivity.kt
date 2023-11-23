package com.example.firebasechatapp

import android.content.Intent
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
import com.example.firebasechatapp.Adapters.GroupChatMessageRecyclerViewAdapter
import com.example.firebasechatapp.data.ChatRoom
import com.example.firebasechatapp.data.GroupChatRoom
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.repositories.ChatMessageRepository
import com.example.firebasechatapp.repositories.ChatRoomRepository
import com.example.firebasechatapp.repositories.UserRepository
import com.example.firebasechatapp.utils.MyUtils
import com.example.firebasechatapp.utils.SharedPreferencesHelper

class GroupChatActivity : AppCompatActivity() {
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

    private lateinit var groupChatroomId:String
    private var groupChatRoom: GroupChatRoom? = null

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var savedCredentials:Triple<String?, String?, User?>
    private lateinit var adapter: GroupChatMessageRecyclerViewAdapter



    private lateinit var messageInput: EditText
    private lateinit var sendMessageBtn: ImageButton
    private lateinit var backBtn: ImageButton
    private lateinit var detailsBtn: ImageButton
    private lateinit var groupChatName: TextView
    private lateinit var chatRecyclerView: RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat)


        progressBar = findViewById(R.id.loadingProgressBarLayout)

        userRepository = UserRepository(this)
        chatRoomRepository = ChatRoomRepository(this)
        chatMessageRepository = ChatMessageRepository(this)

        groupChatRoom = MyUtils.getGroupChatRoomFromIntent(intent)

        sharedPreferencesHelper = SharedPreferencesHelper(this)
        savedCredentials = sharedPreferencesHelper.getSavedCredentials()

        messageInput = findViewById(R.id.actual_message_box)
        sendMessageBtn = findViewById(R.id.message_send_btn)
        chatRecyclerView = findViewById(R.id.all_messages_recycler_view)

        detailsBtn = findViewById(R.id.group_details)
        backBtn = findViewById(R.id.go_back)
        groupChatName = findViewById(R.id.group_name)

        groupChatroomId = groupChatRoom!!.GroupChatRoomId
        groupChatName.text = groupChatRoom!!.GroupName

        backBtn.setOnClickListener { onBackPressed() }

        sendMessageBtn.setOnClickListener{v ->
            run {
                var message = messageInput.text.toString()
                if (message.isEmpty()) {
                    return@run
                } else {
                    sendMessageToGroup(message)
                }
            }
        }

        setupChatRecyclerView()

        detailsBtn.setOnClickListener {
//            GroupDetailsActivity
            val intent = Intent(this, GroupDetailsActivity::class.java)
            MyUtils.passGroupChatRoomAsIntent(intent, groupChatRoom!!)
            startActivity(intent)
        }
    }


    private fun setupChatRecyclerView() {
        showLoader()

        var manager = LinearLayoutManager(this)
        manager.reverseLayout = true
        chatRecyclerView.layoutManager = manager

//        adapter = ChatMessageRecyclerViewAdapter(options, this)

        adapter = GroupChatMessageRecyclerViewAdapter(chatMessageRepository.getAllSentGroupChatMessagesQuery(groupChatroomId), this)
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





    private fun sendMessageToGroup(message: String) {

        chatMessageRepository.sendMessageToGroup(groupChatRoom!!, message, groupChatroomId){
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


    private fun myToast(msg:String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}