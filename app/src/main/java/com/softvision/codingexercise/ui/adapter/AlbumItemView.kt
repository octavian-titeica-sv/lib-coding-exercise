package com.softvision.codingexercise.ui.adapter

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.softvision.codingexercise.R
import com.softvision.codingexercise.domain.model.AlbumModel
import kotlinx.android.synthetic.main.ui_module_album_item_view.view.*

class AlbumItemView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs) {

    init {
        View.inflate(context, R.layout.ui_module_album_item_view, this)
        layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun setState(album: AlbumModel) {
        setupView(album)
    }

    private fun setupView(album: AlbumModel) {
        album_title_text_view.text = album.title
    }
}
