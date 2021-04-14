package com.softvision.codingexercise.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.softvision.codingexercise.repository.db.dao.AlbumsDao
import com.softvision.codingexercise.repository.db.model.AlbumDbModel

@Database(entities = [AlbumDbModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun albumsDao(): AlbumsDao
}
