package com.softvision.codingexercise.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.softvision.codingexercise.domain.model.AlbumModel

class AlbumsDiffCallback : DiffUtil.ItemCallback<AlbumModel>() {
    override fun areItemsTheSame(oldItem: AlbumModel, newItem: AlbumModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AlbumModel, newItem: AlbumModel): Boolean {
        return oldItem == newItem
    }
}
