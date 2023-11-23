package com.example.firebasechatapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.Adapters.MyGroupChatsRecyclerViewAdapter
import com.example.firebasechatapp.data.GroupChatRoom
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.extensions.GroupNameDialog
import com.example.firebasechatapp.repositories.ChatRoomRepository
import com.example.firebasechatapp.utils.MyUtils
import com.example.firebasechatapp.utils.SharedPreferencesHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton


class GroupsFragment : Fragment() {

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
    private lateinit var adapter: MyGroupChatsRecyclerViewAdapter
    private lateinit var groupChatRooms : List<GroupChatRoom>




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_groups, container, false)


        val fabAddGroup: FloatingActionButton = view.findViewById(R.id.fabAddGroup)
        fabAddGroup.setOnClickListener {
            showGroupNameDialog()
        }

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


        adapter = MyGroupChatsRecyclerViewAdapter(this.requireContext())
        recyclerView.adapter = adapter

        loadAllGroupChatRooms()

    }



    private fun loadAllGroupChatRooms() {

        showLoader()

        groupChatRooms = mutableListOf()

        chatRoomRepository.getMyGroupChatRooms(){
                success, message, receivedGroupChatRooms ->
            run {

                myToast(message)
                if (success) {
                    groupChatRooms = receivedGroupChatRooms
                    adapter.updateMyGroupChatsLis(groupChatRooms)
                }
                hideLoader()
            }
        }

    }

    private fun showGroupNameDialog() {
        val groupNameDialog = GroupNameDialog(requireContext()) { groupName ->

            chatRoomRepository.createGroupChatRoom(savedCredentials.third!!.UserId, groupName){
                success, message, groupChatRoom ->
                run {
//                    myToast(message)
                    if (success){
                        val intent = Intent(context, GroupChatActivity::class.java)
                        MyUtils.passGroupChatRoomAsIntent(intent, groupChatRoom!!)
                        startActivity(intent)
                    }
                }
            }
        }

        groupNameDialog.show()
    }

    private fun myToast(msg:String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

}