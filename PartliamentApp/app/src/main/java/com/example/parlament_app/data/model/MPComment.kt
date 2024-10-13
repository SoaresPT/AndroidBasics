package com.example.parlament_app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Date: 13.10.2024
// Name: Sergio Soares 2217948
// Description: Data class representing a comment and grade given to an MP, stored in the Room database.
@Entity(tableName = "mp_comment_table")
data class MPComment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mpId: Int,
    val comment: String,
    val grade: Int
)
