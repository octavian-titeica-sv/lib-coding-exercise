package com.softvision.codingexercise.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softvision.codingexercise.domain.model.AlbumModel
import com.softvision.codingexercise.domain.repository.AlbumFetchResult
import com.softvision.codingexercise.domain.repository.AlbumsRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(val repository: AlbumsRepository) : ViewModel() {

    val albumsLiveData: LiveData<List<AlbumModel>>
        get() = _albumsLiveData
    val errorLiveData: LiveData<String>
        get() = _errorLiveData
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveData

    private val _albumsLiveData = MutableLiveData<List<AlbumModel>>()
    private val _errorLiveData = MutableLiveData<String>()
    private val _loadingLiveData = MutableLiveData<Boolean>()

    init {
        getAlbums()
    }

    fun getAlbums() {
        viewModelScope.launch {
            repository.getAlbums()
                .onStart { _loadingLiveData.value = true }
                .collect {
                    when (it) {
                        is AlbumFetchResult.Data -> {
                            _albumsLiveData.value = it.list
                            _errorLiveData.value = ""
                        }
                        is AlbumFetchResult.Error -> _errorLiveData.value = it.message
                    }
                    _loadingLiveData.value = false
                }
        }
    }
}
