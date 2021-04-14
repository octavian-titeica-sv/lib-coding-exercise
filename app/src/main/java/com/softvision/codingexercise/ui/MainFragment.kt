package com.softvision.codingexercise.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.softvision.codingexercise.R
import com.softvision.codingexercise.presentation.MainViewModel
import com.softvision.codingexercise.ui.adapter.AlbumsAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject

open class MainFragment : DaggerFragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAlbumsAdapter()

        viewModel.apply {
            loadingLiveData.observe(viewLifecycleOwner, { isContentLoading -> renderLoading(isContentLoading) })
            errorLiveData.observe(viewLifecycleOwner, { errorMessage -> renderError(errorMessage) })
        }
    }

    private fun setupAlbumsAdapter() {
        albums_recycler_view.apply {
            val albumsAdapter = AlbumsAdapter()
            adapter = albumsAdapter
            layoutManager = LinearLayoutManager(requireContext())

            viewModel.albumsLiveData.observe(viewLifecycleOwner, { albums -> albumsAdapter.submitList(albums) })
        }
    }

    private fun renderLoading(isContentLoading: Boolean) {
        loading_progress_bar.apply {
            if (isContentLoading) show() else hide()
        }
    }

    private fun renderError(errorMessage: String) {
        if (errorMessage.isNotEmpty()) {
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
        }
    }
}
