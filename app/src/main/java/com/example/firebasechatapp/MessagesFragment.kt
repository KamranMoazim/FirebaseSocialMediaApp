package com.example.firebasechatapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.Adapters.MyChatsRecyclerViewAdapter
import com.example.firebasechatapp.data.ChatRoom
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.repositories.ChatRoomRepository
import com.example.firebasechatapp.utils.SharedPreferencesHelper
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference


class MessagesFragment : Fragment() {

    private lateinit var progressBar: ProgressBar
    private fun showLoader() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        progressBar.visibility = View.GONE
    }




    private lateinit var chatRoomRepository: ChatRoomRepository


    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var savedCredentials:Triple<String?, String?, User?>
    private lateinit var adapter: MyChatsRecyclerViewAdapter
    private lateinit var chatRooms : List<ChatRoom>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_messages, container, false)

        progressBar = view.findViewById(R.id.loadingProgressBarLayout)

        chatRoomRepository = ChatRoomRepository(requireContext())

        return view
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        recyclerView = view.findViewById(R.id.my_chats_recycler_view)

        sharedPreferencesHelper = SharedPreferencesHelper(this.requireContext())
        savedCredentials = sharedPreferencesHelper.getSavedCredentials()


        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(false)


        adapter = MyChatsRecyclerViewAdapter(this.requireContext())
        recyclerView.adapter = adapter

        loadData("")

    }




    private fun loadData(data: String) {

        showLoader()

        chatRooms = mutableListOf()

        chatRoomRepository.getMyChatRooms(){
            success, message, receivedChatRooms ->
            run {

                myToast(message)
                if (success) {
                    chatRooms = receivedChatRooms
                    adapter.updateMyChatsLis(chatRooms)
                }
                hideLoader()
            }
        }

    }

    private fun myToast(msg:String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }


}