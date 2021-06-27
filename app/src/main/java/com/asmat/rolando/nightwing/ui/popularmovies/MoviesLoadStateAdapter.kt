package com.asmat.rolando.nightwing.ui.popularmovies

import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView

class MoviesLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<MoviesLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        TODO("Not yet implemented")
    }




    inner class LoadStateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}