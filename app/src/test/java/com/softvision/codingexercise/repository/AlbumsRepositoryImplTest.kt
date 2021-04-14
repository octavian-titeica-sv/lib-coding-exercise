package com.softvision.codingexercise.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.softvision.codingexercise.domain.model.AlbumModel
import com.softvision.codingexercise.domain.repository.AlbumFetchResult
import com.softvision.codingexercise.repository.db.AppDatabase
import com.softvision.codingexercise.repository.db.dao.AlbumsDao
import com.softvision.codingexercise.repository.db.model.AlbumDbModel
import com.softvision.codingexercise.repository.network.RetrofitService
import com.softvision.codingexercise.repository.network.model.AlbumNetworkModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import java.net.SocketTimeoutException

@ExperimentalCoroutinesApi
class AlbumsRepositoryImplTest {

    @get:Rule
    var instantRuleExecutor = InstantTaskExecutorRule()

    @MockK
    lateinit var api: RetrofitService

    @MockK
    lateinit var database: AppDatabase

    @MockK
    lateinit var dao: AlbumsDao

    lateinit var albumsRepositoryImpl: AlbumsRepositoryImpl

    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { database.albumsDao() } returns dao

        albumsRepositoryImpl = AlbumsRepositoryImpl(api, database, dispatcher)
    }

    @Test
    fun `given no data was persisted in the database, when fetching the albums, then the results will be coming in from the network`() {
        dispatcher.runBlockingTest {
            val expectedResult = AlbumFetchResult.Data(DOMAIN_ALBUMS_ONLY_REMOTE)

            // given
            val channel = Channel<List<AlbumDbModel>>(1)
            val dbFlow = channel.receiveAsFlow()

            coEvery { api.getAlbums() } returns Response.success(NETWORK_ALBUMS)
            every { dao.getAlbums() } returns dbFlow
            coEvery { dao.upsert(DB_MAPPED_NETWORK_ALBUMS) } coAnswers { channel.send(DB_MAPPED_NETWORK_ALBUMS) }

            // when
            val results = albumsRepositoryImpl.getAlbums().first()

            // then
            assertEquals("Results should match: ", expectedResult, results)
        }
    }

    @Test
    fun `given no data was persisted in the database and the network is down, when fetching the albums, then an error will be reported`() {
        dispatcher.runBlockingTest {
            val expectedResult = AlbumFetchResult.Error("Failed fetching albums due to Timeout exception")

            // given
            val channel = Channel<List<AlbumDbModel>>(1)
            val dbFlow = channel.receiveAsFlow()

            coEvery { api.getAlbums() } throws SocketTimeoutException()
            every { dao.getAlbums() } returns dbFlow

            // when
            val results = albumsRepositoryImpl.getAlbums().first()

            // then
            assertEquals("Results should match: ", expectedResult, results)
        }
    }

    @Test
    fun `given data was persisted in the database, when fetching the albums, then the results will be coming in from the database and from the network`() {
        dispatcher.runBlockingTest {
            val expectedResult = listOf(
                AlbumFetchResult.Data(DOMAIN_ALBUMS_ONLY_PERSISTED),
                AlbumFetchResult.Data(DOMAIN_ALBUMS_ALL)
            )

            // given
            val channel = Channel<List<AlbumDbModel>>(1)
            val dbFlow = channel.receiveAsFlow()
            channel.send(DB_ALBUMS)

            coEvery { api.getAlbums() } returns Response.success(NETWORK_ALBUMS)
            every { dao.getAlbums() } returns dbFlow
            coEvery { dao.upsert(DB_MAPPED_NETWORK_ALBUMS) } coAnswers { channel.send(DB_ALBUMS + DB_MAPPED_NETWORK_ALBUMS) }

            // when
            val flow = albumsRepositoryImpl.getAlbums()
            val result1 = flow.first()
            val result2 = flow.first()

            // then
            assertEquals("Results should match: ", expectedResult, listOf(result1, result2))
        }
    }

    @Test
    fun `given data was persisted in the database and the network is down, when fetching the albums, then the results will be coming in from the database and an error from the network`() {
        dispatcher.runBlockingTest {
            val expectedResult = listOf(
                AlbumFetchResult.Data(DOMAIN_ALBUMS_ONLY_PERSISTED),
                AlbumFetchResult.Error("Failed fetching albums due to Timeout exception"),
            )

            // given
            val channel = Channel<List<AlbumDbModel>>(1)
            val dbFlow = channel.receiveAsFlow()
            channel.send(DB_ALBUMS)

            coEvery { api.getAlbums() } throws SocketTimeoutException()
            every { dao.getAlbums() } returns dbFlow

            // when
            val flow = albumsRepositoryImpl.getAlbums()
            val result1 = flow.first()
            val result2 = flow.first()

            // then
            assertEquals("Results should match: ", expectedResult, listOf(result1, result2))
        }
    }

    companion object {
        val DB_ALBUMS = listOf(
            AlbumDbModel(1, 101, "First persisted album"),
            AlbumDbModel(2, 102, "Second persisted album")
        )

        val NETWORK_ALBUMS = listOf(
            AlbumNetworkModel(3, 101, "First remote album"),
            AlbumNetworkModel(4, 102, "Second remote album")
        )

        val DB_MAPPED_NETWORK_ALBUMS = listOf(
            AlbumDbModel(3, 101, "First remote album"),
            AlbumDbModel(4, 102, "Second remote album")
        )

        val DOMAIN_ALBUMS_ONLY_PERSISTED = listOf(
            AlbumModel(1, 101, "First persisted album"),
            AlbumModel(2, 102, "Second persisted album")
        )

        val DOMAIN_ALBUMS_ONLY_REMOTE = listOf(
            AlbumModel(3, 101, "First remote album"),
            AlbumModel(4, 102, "Second remote album")
        )

        val DOMAIN_ALBUMS_ALL = DOMAIN_ALBUMS_ONLY_PERSISTED + DOMAIN_ALBUMS_ONLY_REMOTE
    }
}
