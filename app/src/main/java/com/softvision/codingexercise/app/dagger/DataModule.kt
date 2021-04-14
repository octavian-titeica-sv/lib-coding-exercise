package com.softvision.codingexercise.app.dagger

import com.softvision.codingexercise.domain.repository.AlbumsRepository
import com.softvision.codingexercise.repository.AlbumsRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class DataModule {

    @Binds
    abstract fun bindAlbumsRepository(albumsRepositoryImpl: AlbumsRepositoryImpl): AlbumsRepository
}
