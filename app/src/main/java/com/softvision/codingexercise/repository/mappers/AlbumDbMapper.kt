package com.softvision.codingexercise.repository.mappers

import com.softvision.codingexercise.domain.model.AlbumModel
import com.softvision.codingexercise.repository.db.model.AlbumDbModel

fun List<AlbumDbModel>.mapToAlbumListModel() = this.map { it.mapToAlbumModel() }

fun AlbumDbModel.mapToAlbumModel() = AlbumModel(
    id = this.id,
    userId = this.userId,
    title = this.title
)

fun List<AlbumModel>.mapToAlbumListDbModel() = this.map { it.mapToAlbumDbModel() }

fun AlbumModel.mapToAlbumDbModel() = AlbumDbModel(
    id = this.id,
    userId = this.userId,
    title = this.title
)
