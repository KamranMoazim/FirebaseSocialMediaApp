package com.example.firebasechatapp.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FriendsDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "FriendsDatabase"
        private const val DATABASE_VERSION = 2

        private const val TABLE_FRIENDS = "Friends"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_NAME = "friendId"
    }

//    override fun onCreate(db: SQLiteDatabase) {
//        // db.execSQL("DROP TABLE IF EXISTS $TABLE_FRIENDS;")
//        val createFriendsTable = ("CREATE TABLE $TABLE_FRIENDS ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_NAME TEXT);")
//        db.execSQL(createFriendsTable)
//    }
    override fun onCreate(db: SQLiteDatabase) {
        val createFriendsTable = ("CREATE TABLE $TABLE_FRIENDS ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_NAME TEXT);")
        db.execSQL(createFriendsTable)
    }



    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // If you need to upgrade the table schema, you can handle it here
        // For simplicity, you can drop and recreate the table
//        db.execSQL("DROP TABLE IF EXISTS $TABLE_FRIENDS;")
        onCreate(db)
    }

    fun removeAllFriendsData() {
        // Get a writable database
        val db = writableDatabase

        // Delete all rows from the table
        db.delete(TABLE_FRIENDS, null, null)

        // Close the database
        db.close()
    }


    fun addFriend(friendId: String) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, friendId)
        db.insert(TABLE_FRIENDS, null, values)
        db.close()
    }

    fun removeFriend(friendId: String) {
        val db = writableDatabase
        db.delete(TABLE_FRIENDS, "$COLUMN_NAME=?", arrayOf(friendId))
        db.close()
    }


    fun getAllFriends(): List<String> {
        val friendsList = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_FRIENDS", null)

        try {
            val columnIndex = cursor.getColumnIndex(COLUMN_NAME)

            if (columnIndex != -1 && cursor.moveToFirst()) {
                do {
                    val friendName = cursor.getString(columnIndex)
                    friendsList.add(friendName)
                } while (cursor.moveToNext())
            }
        } finally {
            cursor.close()
            db.close()
        }

        return friendsList
    }



    // Add other CRUD operations as needed
}
