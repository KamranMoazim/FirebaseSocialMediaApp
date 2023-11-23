package com.example.firebasechatapp.repositories

import android.content.Context
import android.util.Log
import com.example.firebasechatapp.data.ChatRoom
import com.example.firebasechatapp.data.GroupChatRoom
import com.example.firebasechatapp.utils.FirebaseUtils
import com.example.firebasechatapp.utils.SharedPreferencesHelper
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query


class ChatRoomRepository(
    private val context: Context
) {

    private val firestoreReference: CollectionReference = FirebaseUtils.allChatRoomCollectionReference()
    private val sharedPreferencesHelper: SharedPreferencesHelper = SharedPreferencesHelper(this.context)


    fun getMyChatRooms(
        userId:String,
        callback: (Boolean, String, List<ChatRoom>) -> Unit
    ){

        var chatRooms = mutableListOf<ChatRoom>()

        firestoreReference
//            .whereArrayContains("userIds", sharedPreferencesHelper.getSavedCredentials().third!!.UserId)
            .whereArrayContains("userIds", userId)
            .whereEqualTo("isGroupChatRoom", false)
            .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Check if there is data in the snapshot
                if (!querySnapshot.isEmpty) {
                    // Data exists, log or process it
                    for (documentSnapshot in querySnapshot.documents) {
                        val chatRoom = documentSnapshot.toObject(ChatRoom::class.java)
                        // Log or process each user
                        chatRooms.add(chatRoom!!)
                    }
                    callback(true,  "Received ChatRooms from Server", chatRooms)
                } else {
                    // No data found
                    callback(false,  "No ChatRooms found", chatRooms)
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors here
                callback(false,  "Error getting users: ${exception.message}", chatRooms)
            }
    }



    fun getOrCreateChatRoom(
        myId: String,
        otherUser: String,
        chatroomId: String,
        callback: (Boolean, String, ChatRoom?) -> Unit
    ) {
        // Initialize tempChatRoom to avoid NPE
        var tempChatRoom: ChatRoom? = null

        sharedPreferencesHelper.getSavedCredentials()

        FirebaseUtils.getChatRoomReference(chatroomId).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Get the result of the query
                    val documentSnapshot = task.result

                    // Check if the document exists
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Document already exists, retrieve the ChatRoom
                        tempChatRoom = documentSnapshot.toObject(ChatRoom::class.java)
                    } else {
                        // Document doesn't exist, create a new ChatRoom
                        tempChatRoom = ChatRoom(
                            ChatRoomId = chatroomId,
                            LastMessageSenderId = "",
                            LastMessage = "",
                            LastMessageTimestamp = Timestamp.now(),
                            UserIds = listOf(myId, otherUser)
                        )

                        // Save the new ChatRoom to Firestore
                        FirebaseUtils.getChatRoomReference(chatroomId).set(tempChatRoom!!)
                    }

                    // Pass the result to the callback
                    callback(true, "Start your Chat", tempChatRoom)
                } else {
                    // Handle the case where the get() operation fails
                    callback(false, "Server Error, Try Again", null)
                }
            }
    }





    fun getMyGroupChatRooms(
        callback: (Boolean, String, List<GroupChatRoom>) -> Unit
    ){

        var groupChatRoom = mutableListOf<GroupChatRoom>()

        firestoreReference
//            .whereEqualTo("isGroupChatRoom", true)
            .whereArrayContains("userIds", sharedPreferencesHelper.getSavedCredentials().third!!.UserId)
            .whereEqualTo("isGroupChatRoom", true)
//            .whereEqualTo("groupCreatorId", sharedPreferencesHelper.getSavedCredentials().third!!.UserId)
//            .where("groupCreatorId", sharedPreferencesHelper.getSavedCredentials().third!!.UserId)
            .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Check if there is data in the snapshot
                if (!querySnapshot.isEmpty) {
                    // Data exists, log or process it
                    for (documentSnapshot in querySnapshot.documents) {
                        val chatRoom = documentSnapshot.toObject(GroupChatRoom::class.java)
                        // Log or process each user
                        groupChatRoom.add(chatRoom!!)
                    }
                    callback(true,  "Received GroupChatRooms from Server", groupChatRoom)
//                    adapter.updateMyChatsLis(chatRooms)
                } else {
                    // No data found
//                    Log.d("UserData", "No users found")
                    callback(false,  "No GroupChatRooms found", groupChatRoom)
                    callback(false,  sharedPreferencesHelper.getSavedCredentials().third!!.UserId, groupChatRoom)
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors here
//                Log.e("getMyGroupChatRooms", "Error getting users: ${exception.message}")
                callback(false,  "Error getting users: ${exception.message}", groupChatRoom)
            }
    }




    fun createGroupChatRoom(
        groupCreatorId: String,
        groupName: String,
        callback: (Boolean, String, GroupChatRoom?) -> Unit
    ) {
        // Reference to the Firestore collection
        val chatRoomCollection = FirebaseUtils.allChatRoomCollectionReference()

        // Create a new GroupChatRoom without specifying GroupChatRoomId
        val newGroupChatRoom = GroupChatRoom(
            GroupChatRoomId = "",
            GroupCreatorId = groupCreatorId,
            GroupName = groupName,
            LastMessageSenderName = "",
            LastMessageSenderId = "",
            LastMessage = "",
            LastMessageTimestamp = Timestamp.now(),
            UserIds = listOf(groupCreatorId)
        )

        // Add the new GroupChatRoom to Firestore, Firebase will generate a unique ID
        chatRoomCollection.add(newGroupChatRoom)
            .addOnCompleteListener { addTask ->
                if (addTask.isSuccessful) {
                    val addedDocumentReference = addTask.result
                    val addedDocumentId = addedDocumentReference?.id

                    // Update the GroupChatRoomId with the generated document ID
                    addedDocumentId?.let {
                        newGroupChatRoom.GroupChatRoomId = it
                    }

                    // Update the document in Firestore with the modified GroupChatRoom
                    addedDocumentReference?.set(newGroupChatRoom)
                        ?.addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                // Retrieve the updated document and pass it to the callback
                                addedDocumentReference.get()
                                    .addOnSuccessListener { documentSnapshot ->
                                        val updatedGroupChatRoom =
                                            documentSnapshot.toObject(GroupChatRoom::class.java)
                                        callback(true, "Start your Group Chat", updatedGroupChatRoom)
                                    }
                                    .addOnFailureListener {
                                        callback(false, "Failed to fetch added Group Chat Room", null)
                                    }
                            } else {
                                callback(false, "Failed to update Group Chat Room with ID", null)
                            }
                        }
                } else {
                    callback(false, "Failed to create Group Chat Room", null)
                }
            }
    }



    fun addPersonToGroupChat(
        groupId: String,
        personId: String,
        callback: (Boolean, String) -> Unit
    ) {
        // Reference to the Firestore document for the specific GroupChatRoom
        val groupChatRoomReference = FirebaseUtils.getChatRoomReference(groupId)

        groupChatRoomReference.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documentSnapshot = task.result

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        val groupChatRoom = documentSnapshot.toObject(GroupChatRoom::class.java)

                        if (groupChatRoom != null) {
                            // Check if the person is already in the group
                            if (!groupChatRoom.UserIds.contains(personId)) {
                                // Add the person to the UserIds list
                                groupChatRoom.UserIds = groupChatRoom.UserIds.plus(personId)

                                // Update the document in Firestore with the modified GroupChatRoom
                                groupChatRoomReference.set(groupChatRoom)
                                    .addOnCompleteListener { updateTask ->
                                        if (updateTask.isSuccessful) {
                                            callback(true, "Person added to the group")
                                        } else {
                                            callback(false, "Failed to update Group Chat Room")
                                        }
                                    }
                            } else {
                                callback(false, "Person is already in the group")
                            }
                        } else {
                            callback(false, "Failed to retrieve Group Chat Room")
                        }
                    } else {
                        callback(false, "Group Chat Room does not exist")
                    }
                } else {
                    callback(false, "Server Error, Try Again")
                }
            }
    }



    fun removePersonFromGroupChat(
        groupId: String,
        personId: String,
        callback: (Boolean, String) -> Unit
    ) {
        // Reference to the Firestore document for the specific GroupChatRoom
        val groupChatRoomReference = FirebaseUtils.getChatRoomReference(groupId)

        groupChatRoomReference.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documentSnapshot = task.result

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        val groupChatRoom = documentSnapshot.toObject(GroupChatRoom::class.java)

                        if (groupChatRoom != null) {
                            // Check if the person is in the group
                            if (groupChatRoom.UserIds.contains(personId)) {
                                // Remove the person from the UserIds list
                                groupChatRoom.UserIds = groupChatRoom.UserIds.minus(personId)

                                // Update the document in Firestore with the modified GroupChatRoom
                                groupChatRoomReference.set(groupChatRoom)
                                    .addOnCompleteListener { updateTask ->
                                        if (updateTask.isSuccessful) {
                                            callback(true, "Person removed from the group")
                                        } else {
                                            callback(false, "Failed to update Group Chat Room")
                                        }
                                    }
                            } else {
                                callback(false, "Person is not in the group")
                            }
                        } else {
                            callback(false, "Failed to retrieve Group Chat Room")
                        }
                    } else {
                        callback(false, "Group Chat Room does not exist")
                    }
                } else {
                    callback(false, "Server Error, Try Again")
                }
            }
    }


}