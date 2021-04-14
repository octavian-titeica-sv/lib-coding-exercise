package com.softvision.codingexercise.ui

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.softvision.codingexercise.R
import com.softvision.codingexercise.ToastMatcher
import com.softvision.codingexercise.domain.model.AlbumModel
import com.softvision.codingexercise.domain.repository.AlbumFetchResult
import com.softvision.codingexercise.domain.repository.AlbumsRepository
import com.softvision.codingexercise.presentation.MainViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainFragmentTest {

    private lateinit var fragmentScenario: FragmentScenario<MainFragment>

    @MockK
    private lateinit var repository: AlbumsRepository

    private val firstAlbum = AlbumModel(1, 1, "First album")
    private val secondAlbum = AlbumModel(2, 1, "Second album")
    private val albums = listOf(firstAlbum, secondAlbum)

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testTheMainContainerIsDisplayed() {
        every { repository.getAlbums() } returns flowOf()
        launchFragmentInContainer()
        onView(withId(R.id.main)).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun testCorrectAlbumsAreDisplayed() {
        every { repository.getAlbums() } returns flowOf(AlbumFetchResult.Data(albums))
        launchFragmentInContainer()
        onView(withId(R.id.albums_recycler_view)).check(ViewAssertions.matches(ViewMatchers.hasChildCount(albums.size)))
        onView(withId(R.id.albums_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.click()))
    }

    @Test
    fun testErrorIsDisplayed() {
        val errorMessage = "test error message"
        every { repository.getAlbums() } returns flowOf(AlbumFetchResult.Error(errorMessage))
        launchFragmentInContainer()
        onView(withText(errorMessage)).inRoot(ToastMatcher()).check(ViewAssertions.matches(isDisplayed()))
    }

    private fun launchFragmentInContainer() {
        fragmentScenario = launchFragmentInContainer {
            val fragment = MainFragment()
            fragment.viewModelFactory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return MainViewModel(repository) as T
                }
            }
            return@launchFragmentInContainer fragment
        }
    }
}
