package com.asmat.rolando.nightwing.ui.popularmovies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.asmat.rolando.nightwing.R
import com.asmat.rolando.nightwing.databinding.MoviesLoadStateFooterViewItemBinding

class MoviesLoadStateViewHolder(
    private val binding: MoviesLoadStateFooterViewItemBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = loadState.error.localizedMessage
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.errorMsg.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): MoviesLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.movies_load_state_footer_view_item, parent, false)
            val binding = MoviesLoadStateFooterViewItemBinding.bind(view)
            return MoviesLoadStateViewHolder(binding, retry)
        }
    }
}