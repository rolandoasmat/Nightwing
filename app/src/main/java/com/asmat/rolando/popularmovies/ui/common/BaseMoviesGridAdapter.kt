package com.asmat.rolando.popularmovies.ui.common

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.asmat.rolando.popularmovies.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_grid_item.view.*

private typealias Movies = MutableList<MovieGridItemUiModel>

abstract class BaseMoviesGridAdapter(val callback: Callback? = null) : RecyclerView.Adapter<BaseMoviesGridAdapter.ViewHolder>() {

    private var showEmptyState = false
    private var movies: Movies = mutableListOf()

    abstract val emptyStateLayoutID: Int

    fun setMovies(movies: List<MovieGridItemUiModel>) {
        this.movies.clear()
        addMovies(movies)
    }

    fun addMovies(movies: List<MovieGridItemUiModel>) {
        this.movies.addAll(movies)
        showEmptyState = this.movies.isEmpty()
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (showEmptyState) {
            MovieGridItemType.EMPTY.ordinal
        } else {
            MovieGridItemType.REGULAR.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val layoutIdForGridItem = when (viewType) {
            MovieGridItemType.EMPTY.ordinal -> emptyStateLayoutID
            MovieGridItemType.REGULAR.ordinal ->  R.layout.movie_grid_item
            else -> R.layout.movie_grid_item
        }
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(layoutIdForGridItem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (!showEmptyState) {
            val movie = movies[position]
            holder.bind(movie)
        }
    }

    override fun getItemCount(): Int {
        return if (showEmptyState) {
            1
        } else {
            movies.size
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val poster: ImageView? = itemView.poster
        private val label: TextView? = itemView.label

        fun bind(uiModel: MovieGridItemUiModel) {
            uiModel.posterURL?.let { url ->
                Picasso.get()
                        .load(url)
                        .resize(340, 500)
                        .into(poster)
            } ?: run {
                val resources = itemView.resources
                val image = resources.getDrawable(R.drawable.ic_photo_default, null)
                poster?.setImageDrawable(image)
            }
            label?.text = uiModel.title
            poster?.setOnClickListener {
                callback?.itemPressed(adapterPosition)
            }
        }
    }

    interface Callback {
        fun itemPressed(index: Int)
    }

}