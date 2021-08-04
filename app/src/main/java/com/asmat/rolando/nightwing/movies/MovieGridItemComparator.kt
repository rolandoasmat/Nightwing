package com.asmat.rolando.nightwing.movies

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.asmat.rolando.nightwing.ui.moviegrid.MovieGridItemUiModel

object MovieGridItemComparator : DiffUtil.ItemCallback<MovieGridItemUiModel>() {
    override fun areItemsTheSame(oldItem: MovieGridItemUiModel, newItem: MovieGridItemUiModel): Boolean {
        // Id is unique.
        val areItemsTheSame = oldItem.id == newItem.id &&
                oldItem.title == newItem.title &&
                oldItem.posterURL == newItem.posterURL
        Log.v("RAA", "areItemsTheSame: $areItemsTheSame")
        return areItemsTheSame
    }

    override fun areContentsTheSame(oldItem: MovieGridItemUiModel, newItem: MovieGridItemUiModel): Boolean {
        return oldItem.title == newItem.title && oldItem.posterURL == newItem.posterURL
    }
}