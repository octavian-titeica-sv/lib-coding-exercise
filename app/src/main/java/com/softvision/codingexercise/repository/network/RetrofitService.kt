package com.softvision.codingexercise.repository.network

import com.softvision.codingexercise.repository.network.model.AlbumNetworkModel
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitService {

    @GET("/albums")
    suspend fun getAlbums(): Response<List<AlbumNetworkModel>>
}
