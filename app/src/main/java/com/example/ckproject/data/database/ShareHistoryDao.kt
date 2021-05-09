package com.example.ckproject.data.database

import androidx.room.*

@Dao
interface ShareHistoryDao {

    @Insert
    suspend fun insertData(shareHistory: ShareHistory)

    @Query("SELECT * FROM ShareHistory")
    suspend fun queryData(): List<ShareHistory>

    @Query("DELETE FROM ShareHistory")
    suspend fun deleteAllShareHistory()
}