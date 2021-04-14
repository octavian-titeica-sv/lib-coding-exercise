package com.softvision.codingexercise.repository.dao

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.softvision.codingexercise.repository.db.AppDatabase
import com.softvision.codingexercise.repository.db.dao.AlbumsDao
import com.softvision.codingexercise.repository.db.model.AlbumDbModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class AlbumsDaoTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var albumsDao: AlbumsDao

    private val firstAlbum = AlbumDbModel(1, 1, "First album")
    private val secondAlbum = AlbumDbModel(2, 1, "Second album")
    private val albums = listOf(firstAlbum, secondAlbum)

    @Before
    fun initDb() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java
        ).allowMainThreadQueries()
            .setTransactionExecutor(Executors.newSingleThreadExecutor()).build()
        albumsDao = appDatabase.albumsDao()
    }

    @After
    fun closeDb() {
        appDatabase.close()
    }

    @Test
    fun whenDatabaseIsEmpty_thenGetAlbumsReturnsAnEmptyList() {
        runBlocking {
            // when
            val result = albumsDao.getAlbums().first()

            // then
            assert(result.isEmpty())
        }
    }

    @Test
    fun whenDatabaseIsPopulated_thenGetAlbumsShouldReturnedCachedData() {
        runBlocking {
            // given
            albumsDao.upsert(albums)

            // when
            val result = albumsDao.getAlbums().first()

            // then
            assert(result == albums)
        }
    }

    @Test
    fun whenDatabaseIsEmpty_thenGetAlbumByIdShouldReturnAnEmptyList() {
        runBlocking {
            // when
            val result = albumsDao.getAlbumById(firstAlbum.id).first()

            // then
            assert(result == null)
        }
    }

    @Test
    fun whenDatabaseIsPopulated_thenGetAlbumByIdShouldReturnTheCorrectAlbum() {
        runBlocking {
            // given
            albumsDao.upsert(albums)

            // when
            val result = albumsDao.getAlbumById(firstAlbum.id).first()

            // then
            assert(result == firstAlbum)
        }
    }
}
