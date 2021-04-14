package com.softvision.codingexercise.repository

import android.util.Log
import com.softvision.codingexercise.data.checkResponse
import com.softvision.codingexercise.domain.model.AlbumModel
import com.softvision.codingexercise.domain.repository.AlbumFetchResult
import com.softvision.codingexercise.domain.repository.AlbumsRepository
import com.softvision.codingexercise.repository.db.AppDatabase
import com.softvision.codingexercise.repository.mappers.mapToAlbumListDbModel
import com.softvision.codingexercise.repository.mappers.mapToAlbumListModel
import com.softvision.codingexercise.repository.network.RetrofitService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class AlbumsRepositoryImpl @Inject constructor(
    private val retrofitService: RetrofitService,
    private val database: AppDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : AlbumsRepository {

    @ExperimentalCoroutinesApi
    override fun getAlbums(): Flow<AlbumFetchResult> = merge(getAlbumsFromCache(), getAlbumsFromApi())

    override fun refreshAlbums(): Flow<AlbumFetchResult> = getAlbumsFromApi()

    private fun getAlbumsFromCache(): Flow<AlbumFetchResult> {
        Log.d(this@AlbumsRepositoryImpl.javaClass.simpleName, ".getAlbumsFromCache() retrieving albums from cache..")
        return database.albumsDao().getAlbums().map { AlbumFetchResult.Data(it.mapToAlbumListModel()) }
    }

    private fun getAlbumsFromApi(): Flow<AlbumFetchResult> {
        return flow {
            try {
                saveAlbums(getRemoteAlbums())
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is UnknownHostException -> "No internet connection"
                    is SocketTimeoutException -> "Timeout exception"
                    else -> "Unknown error"
                }
                emit(AlbumFetchResult.Error("Failed fetching albums due to $errorMessage"))
            }
        }.flowOn(dispatcher)
    }

    private suspend fun getRemoteAlbums(): List<AlbumModel> {
        Log.d(this@AlbumsRepositoryImpl.javaClass.simpleName, ".getRemoteAlbums() fetching albums from remote..")
        val response = withContext(dispatcher) {
            retrofitService.getAlbums()
        }

        return response.checkResponse().mapToAlbumListModel()
    }

    private suspend fun saveAlbums(albums: List<AlbumModel>) =
        database.albumsDao().upsert(albums.mapToAlbumListDbModel())
}
