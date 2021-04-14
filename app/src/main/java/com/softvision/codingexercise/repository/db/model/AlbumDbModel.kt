package com.softvision.codingexercise.repository.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Albums")
data class AlbumDbModel(
    @PrimaryKey val id: Int,
    val userId: Int,
    val title: String
)
