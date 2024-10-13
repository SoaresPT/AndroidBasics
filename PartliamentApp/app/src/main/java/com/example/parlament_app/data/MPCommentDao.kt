package com.example.parlament_app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.parlament_app.data.model.MPComment
import kotlinx.coroutines.flow.Flow

// Date: 13.10.2024
// Name: Sergio Soares 2217948
// Description: DAO for MP comments, providing methods to interact with the comments table.
@Dao
interface MPCommentDao {
    // Retrieves comments for a specific MP
    @Query("SELECT * FROM mp_comment_table WHERE mpId = :mpId")
    fun getCommentsForMP(mpId: Int): Flow<List<MPComment>>

    // Insert a new comment into the database
    @Insert
    suspend fun insertComment(comment: MPComment)
}