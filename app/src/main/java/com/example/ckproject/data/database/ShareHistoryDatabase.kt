package com.example.ckproject.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ckproject.utils.DB_NAME
import com.example.ckproject.utils.DB_VERSION

@Database(entities = [ShareHistory::class], version = DB_VERSION)
abstract class ShareHistoryDatabase : RoomDatabase() {

    abstract fun shareHistoryDao(): ShareHistoryDao

    companion object {
        private var instance: ShareHistoryDatabase? = null

        fun getInstance(context: Context): ShareHistoryDatabase {
            if (instance == null) {

                synchronized(ShareHistoryDatabase::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context, ShareHistoryDatabase::class.java,
                            DB_NAME
                        ).build()
                    }
                }
            }
            return instance!!
        }
    }
}