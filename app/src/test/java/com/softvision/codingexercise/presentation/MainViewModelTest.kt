package com.softvision.codingexercise.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.softvision.codingexercise.domain.model.AlbumModel
import com.softvision.codingexercise.domain.repository.AlbumFetchResult
import com.softvision.codingexercise.domain.repository.AlbumsRepository
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelUnitTest {

    @get:Rule
    var instantRuleExecutor = InstantTaskExecutorRule()

    private lateinit var mainViewModel: MainViewModel

    @MockK
    private lateinit var albumsRepository: AlbumsRepository

    @MockK(relaxed = true)
    private lateinit var albumsObserver: Observer<List<AlbumModel>>

    @MockK(relaxed = true)
    private lateinit var loadingObserver: Observer<Boolean>

    @MockK(relaxed = true)
    private lateinit var errorObserver: Observer<String>

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        coEvery { albumsRepository.getAlbums() } returns flowOf()

        mainViewModel = MainViewModel(albumsRepository)
        mainViewModel.albumsLiveData.observeForever(albumsObserver)
        mainViewModel.loadingLiveData.observeForever(loadingObserver)
        mainViewModel.errorLiveData.observeForever(errorObserver)
    }

    @Test
    fun `given the app is opened, when getAlbums returns a list of albums, the corresponding state should be emitted`() {
        runBlockingTest {
            // given
            val album = AlbumModel(1, 1, "First album")
            val result = AlbumFetchResult.Data(listOf(album))
            coEvery { albumsRepository.getAlbums() } returns flowOf(result)

            clearMocks(albumsObserver, loadingObserver, errorObserver)

            // when
            mainViewModel.getAlbums()

            // then
            verifySequence {
                loadingObserver.onChanged(true)
                albumsObserver.onChanged(listOf(album))
                loadingObserver.onChanged(false)
            }
        }
    }

    @Test
    fun `given the app is opened, when getAlbums returns an error, the corresponding state should be emitted`() {
        runBlockingTest {
            // given
            val exceptionMessage = "expected exception message"
            val result = AlbumFetchResult.Error(exceptionMessage)
            coEvery { albumsRepository.getAlbums() } returns flowOf(result)

            clearMocks(albumsObserver, loadingObserver, errorObserver)

            // when
            mainViewModel.getAlbums()

            // then
            verifySequence {
                loadingObserver.onChanged(true)
                errorObserver.onChanged(exceptionMessage)
                loadingObserver.onChanged(false)
            }
        }
    }
}
