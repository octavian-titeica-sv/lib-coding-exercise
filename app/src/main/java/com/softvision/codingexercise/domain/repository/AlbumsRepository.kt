package com.softvision.codingexercise.domain.repository

import com.softvision.codingexercise.domain.model.AlbumModel
import kotlinx.coroutines.flow.Flow

sealed class AlbumFetchResult {
    data class Data(val list: List<AlbumModel>) : AlbumFetchResult()
    data class Error(val message: String) : AlbumFetchResult()
}

interface AlbumsRepository {
    fun getAlbums(): Flow<AlbumFetchResult>
    fun refreshAlbums(): Flow<AlbumFetchResult>
}
