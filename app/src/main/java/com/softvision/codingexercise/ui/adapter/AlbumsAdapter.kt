package com.softvision.codingexercise.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.softvision.codingexercise.domain.model.AlbumModel

class AlbumsAdapter : ListAdapter<AlbumModel, RecyclerView.ViewHolder>(AlbumsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AlbumsViewHolder(AlbumItemView(parent.context))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AlbumsViewHolder) {
            holder.bindItem(getItem(position))
        }
    }

    inner class AlbumsViewHolder(private val albumItemView: AlbumItemView) : RecyclerView.ViewHolder(albumItemView) {
        fun bindItem(album: AlbumModel) {
            albumItemView.setState(album)
        }
    }
}
