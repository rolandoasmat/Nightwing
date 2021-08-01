package com.asmat.rolando.nightwing.movies

import androidx.recyclerview.widget.DiffUtil
import com.asmat.rolando.nightwing.ui.moviegrid.MovieGridItemUiModel

object MovieGridItemComparator : DiffUtil.ItemCallback<MovieGridItemUiModel>() {
    override fun areItemsTheSame(oldItem: MovieGridItemUiModel, newItem: MovieGridItemUiModel): Boolean {
        // Id is unique.
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieGridItemUiModel, newItem: MovieGridItemUiModel): Boolean {
        return oldItem == newItem
    }
}