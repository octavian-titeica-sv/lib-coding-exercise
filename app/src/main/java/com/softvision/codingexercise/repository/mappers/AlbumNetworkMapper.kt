package com.softvision.codingexercise.repository.mappers

import com.softvision.codingexercise.domain.model.AlbumModel
import com.softvision.codingexercise.repository.network.model.AlbumNetworkModel

fun List<AlbumNetworkModel>.mapToAlbumListModel() = this.map { it.mapToAlbumModel() }

fun AlbumNetworkModel.mapToAlbumModel() = AlbumModel(
    id = this.id,
    userId = this.userId,
    title = this.title
)
