package com.softvision.codingexercise.repository.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.softvision.codingexercise.repository.db.model.AlbumDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlbum(album: AlbumDbModel): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateAlbum(album: AlbumDbModel)

    @Transaction
    suspend fun upsert(album: AlbumDbModel) {
        insertAlbum(album)
        updateAlbum(album)
    }

    @Transaction
    suspend fun upsert(albums: List<AlbumDbModel>) = albums.forEach { upsert(it) }

    @Query("SELECT * FROM Albums ORDER BY title ASC")
    fun getAlbums(): Flow<List<AlbumDbModel>>

    @Query("SELECT * FROM Albums WHERE id = :albumId")
    fun getAlbumById(albumId: Int): Flow<AlbumDbModel?>
}
